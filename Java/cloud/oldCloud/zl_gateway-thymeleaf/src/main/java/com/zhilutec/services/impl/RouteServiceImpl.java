package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.CommonRoute;
import com.zhilutec.dbs.entities.FilterInfo;
import com.zhilutec.dbs.entities.PathRoute;
import com.zhilutec.dbs.entities.RouteInfo;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.*;
import org.springframework.cloud.gateway.handler.predicate.*;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.*;

import static com.zhilutec.common.utils.ConstantUtil.*;
import static java.lang.Double.POSITIVE_INFINITY;
import static sun.misc.DoubleConsts.NEGATIVE_INFINITY;

/**
 * 使用Redis保存自定义路由配置（代替默认的InMemoryRouteDefinitionRepository）
 * 存在问题：每次请求都会调用getRouteDefinitions，当网关较多时，会影响请求速度，考虑放到本地Map中，使用消息通知Map更新。
 * routes:
 * - id: uwb-resources
 * uri: http://192.168.10.232:80
 * predicates:
 * - Path=/uwb/resources/**
 * # Normwal Websocket route
 * - id:uwb-websocket
 * uri: ws://192.168.10.232:80
 * predicates:
 * - Path=/uwb/websocket/**
 * - id: uwb-gateway
 * uri: http://192.168.10.188:11007
 * predicates:
 * - Path=/uwb/gateway/**
 * - Method=GET
 * filters:
 * - AddRequestHeader=X-Request-Foo, Bar
 * - AddRequestParameter=foo, bar
 * - name: Hystrix
 * args:
 * name: fallbackcmd
 * fallbackUri: forward:/incaseoffailureusethis
 * - RewritePath=/consumingserviceendpoint, /backingserviceendpoint
 */
@Service
@Component
public class RouteServiceImpl implements IRouteService, RouteDefinitionRepository {

    @Resource
    IRedisService redisService;

    // @Autowired
    // GatewayProperties gatewayProperties;

    @Resource
    DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator;

    //从配置文件中获取静态配置的路由
    // private List<RouteDefinition> getPropRoutes() {
    //     return this.gatewayProperties.getRoutes();
    // }

    //获取自动发现的路由
    private Flux<RouteDefinition> getDiscoverRoutes() {
        Flux<RouteDefinition> routeDefinitionFlux = this.discoveryClientRouteDefinitionLocator.getRouteDefinitions();
        return routeDefinitionFlux;
    }

    //从内存获取路由
    private Flux<RouteDefinition> getMemRoutes() {
        InMemoryRouteDefinitionRepository inMemoryRouteDefinitionRepository = new InMemoryRouteDefinitionRepository();
        Flux<RouteDefinition> routeFlux = inMemoryRouteDefinitionRepository.getRouteDefinitions();
        return routeFlux;
    }

    //获取redis中所有的路由缓存
    @Override
    public List<CommonRoute> getRedisRoutes(String keyPre) {
        List<CommonRoute> routes = new ArrayList<>();
        Set<String> keyValues = redisService.keys(keyPre);
        for (String keyValue : keyValues) {
            List<Object> routeStrlist = redisService.hashValues(keyValue);
            for (Object route : routeStrlist) {
                CommonRoute customerRoute = JSON.parseObject(route.toString(), CommonRoute.class);
                routes.add(customerRoute);
            }
        }
        return routes;
    }

    //获取redis中所有的路由缓存
    @Override
    public List<PathRoute> getPathRoutes(JSONObject jsonObject) {
        Long card = redisService.zSetCard(ConstantUtil.GW_SORT_KEY);
        Long offset = jsonObject.getLong("page");
        offset = offset - 1;
        if (offset == null) {
            offset = 0L;
        }
        Long limit = jsonObject.getLong("listRows");
        List<PathRoute> pathRoutes = new ArrayList<>();
        if (card.longValue() != 0L) {
            Set<String> routeIds = redisService.zSetRange(ConstantUtil.GW_SORT_KEY, offset, limit);

            //直接分页查询
            // Set<String> routeIds = redisService.zSetReverseRangeByScore(ConstantUtil.GW_SORT_KEY,
            //         NEGATIVE_INFINITY,
            //         POSITIVE_INFINITY,
            //         offset,
            //         limit);

            if (routeIds != null && !routeIds.isEmpty()) {
                List<Object> values = redisService.hashMGet(ConstantUtil.GW_KEY_PRE, routeIds);
                if (values != null && values.size() > 0) {
                    for (Object value : values) {
                        if (value != null) {
                            CommonRoute commonRoute = JSON.parseObject(value.toString(), CommonRoute.class);
                            PathRoute pathRoute = convertPathRoute(commonRoute);
                            if (pathRoute != null) {
                                pathRoutes.add(pathRoute);
                            }
                        }
                    }
                }
            }
        }
        return pathRoutes;
    }

    @Override
    public String getPathRouteRs(JSONObject jsonObject) {
        List<PathRoute> pathRoutes = this.getPathRoutes(jsonObject);
        if (pathRoutes == null || pathRoutes.size() == 0) {
            return Result.error(ResultCode.NODATA_ERR).toJSONString();
        } else {
            return Result.ok(ResultCode.SUCCESS.getCode(), pathRoutes).toJSONString();
        }
    }

    /**
     * 由于目前只关注Path类型路由，且不关注过滤条件，
     * 所以这里将通用路由转成Path路由
     */
    private PathRoute convertPathRoute(CommonRoute commonRoute) {
        PathRoute pathRoute = new PathRoute();
        List<RouteInfo> routeInfos = commonRoute.getPredicates();
        Boolean flag = false;
        for (RouteInfo routeInfo : routeInfos) {
            String routeType = routeInfo.getRouteType();
            if (routeType.equals(ConstantUtil.PATH_ROUTE)) {
                String pathRouteStr = routeInfo.getRoute();
                pathRoute.setPath(pathRouteStr);
                flag = true;
                break;
            }
        }

        if (flag) {
            pathRoute.setRouteId(commonRoute.getRouteId());
            pathRoute.setAvailable(commonRoute.getAvailable());
            pathRoute.setOrder(commonRoute.getOrder());
            pathRoute.setUrl(commonRoute.getUrl());
        } else {
            pathRoute = null;
        }
        return pathRoute;
    }


    /**
     * 定义路由,包括路由断言和路由过滤
     */
    private RouteDefinition genRoute(CommonRoute customerRoute) {
        //路由定义
        RouteDefinition routeDefinition = new RouteDefinition();
        if (customerRoute != null) {
            if (customerRoute.getAvailable().intValue() == 1) {
                List<PredicateDefinition> predicateDefList = this.predicateList(customerRoute);
                if (predicateDefList == null || predicateDefList.size() <= 0) {
                    return null;
                }
                routeDefinition.setPredicates(predicateDefList);
                routeDefinition.setId(customerRoute.getRouteId());

                String url = customerRoute.getUrl();
                URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();
                routeDefinition.setUri(uri);

                Integer csOrder = customerRoute.getOrder();
                if (csOrder == null)
                    csOrder = 0;
                routeDefinition.setOrder(csOrder);

                List<FilterDefinition> filterDefinitionList = this.filterList(customerRoute);
                if (filterDefinitionList != null || filterDefinitionList.size() > 0) {
                    routeDefinition.setFilters(filterDefinitionList);
                }
            }
        }
        return routeDefinition;
    }


    /**
     * 过滤器
     * 根据传入的hashmap生成对应的路由过滤条件,可能会有多个过滤条件
     * FilterDefinition过滤器支持类型有AddRequestHeader、AddRequestParameter、PrefixPath、RequestRateLimiter等，即filterDefinition.setName支持的类型
     * GatewayFilterFactory 过滤器工厂有很多实现类
     * 见http://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.0.0.RELEASE/single/spring-cloud-gateway.html
     * 示例:
     * 过滤名是固定的，spring gateway会根据名称找对应的FilterFactory
     * filterDefinition.setName("RequestRateLimiter");
     * // 每秒最大访问次数
     * filterParams.put("redis-rate-limiter.replenishRate", "2");
     * // 令牌桶最大容量
     * filterParams.put("redis-rate-limiter.burstCapacity", "3");
     * // 限流策略(#{@BeanName})
     * filterParams.put("key-resolver", "#{@hostAddressKeyResolver}");
     * // 自定义限流器(#{@BeanName})
     * filterParams.put("rate-limiter", "#{@redisRateLimiter}");
     * filterDefinition.setArgs(filterParams);
     */
    private List<FilterDefinition> filterList(CommonRoute customerRoute) {
        // GatewayFilterFactory
        List<FilterInfo> filterInfos = customerRoute.getFilters();
        List<FilterDefinition> filterDefinitions = new ArrayList<>();
        if (filterInfos != null && filterInfos.size() > 0) {
            for (FilterInfo filterInfo : filterInfos) {
                Map<String, String> filterParams = new HashMap<>(8);
                FilterDefinition filterDefinition = new FilterDefinition();
                String filterType = filterInfo.getFilterType();
                filterDefinition.setName(filterType);
                String filter = filterInfo.getFilter();
                if (filterType.equals(ADD_REQUEST_HEADER_FILTER) ||
                        filterType.equals(ADD_REQUEST_PARAMETER_FILTER) ||
                        filterType.equals(ADD_RESPONSE_HEADER_FILTER)) {
                    // filters:
                    // - AddRequestHeader=X-Request-Foo, Bar
                    String values[] = filter.split(",");
                    filterParams.put(GatewayFilter.NAME_KEY, values[0]);
                    filterParams.put(GatewayFilter.VALUE_KEY, values[1]);
                } else if (filterType.equals(HYSTRIX_FILTER)) {
                    // {"name":"xxx","fallbackUri":"xxx","setter":""}
                    // {"name":"xxx"}
                    filterParams = JSON.parseObject(filter, HashMap.class);
                } else if (filterType.equals(PREFIX_PATH_FILTER)) {
                    // filters:
                    // - PrefixPath=/mypath
                    filterParams.put(PrefixPathGatewayFilterFactory.PREFIX_KEY, filter);
                } else if (filterType.equals(PRESERVE_HOST_HEADER_FILTER)) {
                    // filters:
                    // - PreserveHostHeader
                    // 此过滤不需要参数
                } else if (filterType.equals(REUQEST_RATE_LIMITER_FILTER)) {
                    // 每秒最大访问次数
                    // filterParams.put("redis-rate-limiter.replenishRate", "2");
                    // 令牌桶最大容量
                    // filterParams.put("redis-rate-limiter.burstCapacity", "3");
                    // 限流策略(#{@BeanName})
                    // filterParams.put("key-resolver", "#{@hostAddressKeyResolver}");
                    // 自定义限流器(#{@BeanName})
                    //filterParams.put("rate-limiter", "#{@redisRateLimiter}");
                    // filterParams.put(PrefixPathGatewayFilterFactory.PREFIX_KEY, filter);
                    filterParams = JSON.parseObject(filter, HashMap.class);
                } else if (filterType.equals(REDIRECT_TO_FILTER)) {
                    filterParams.put(PrefixPathGatewayFilterFactory.PREFIX_KEY, filter);
                } else if (filterType.equals(REMOVE_REQUEST_HEADER_FILTER) ||
                        filterType.equals(REMOVE_RESPONSE_HEADER_FILTER)) {
                    // filters:
                    // - RemoveResponseHeader=X-Response-Foo
                    filterParams.put(GatewayFilter.NAME_KEY, filter);
                } else if (filterType.equals(REWRITE_PATH_FILTER)) {
                    // filters:
                    // - RewritePath=/foo/(?<segment>.*), /$\{segment}
                    String values[] = filter.split(",");
                    filterParams.put(RewritePathGatewayFilterFactory.REGEXP_KEY, filter);
                    filterParams.put(RewritePathGatewayFilterFactory.REPLACEMENT_KEY, filter);
                } else if (filterType.equals(SAVE_SESSION_FILTER)) {
                    // filters:
                    // - RewritePath=/foo/(?<segment>.*), /$\{segment}
                    // filters:
                    // - SaveSession
                    // 不需要传参数
                } else if (filterType.equals(SET_PATH_FILTER)) {
                    // predicates:
                    // - Path=/foo/{segment}
                    // filters:
                    // - SetPath=/{segment}
                    filterParams.put(SetPathGatewayFilterFactory.TEMPLATE_KEY, filter);
                } else if (filterType.equals(SET_RESPONSE_HEADER_FILTER)) {
                    // filters:
                    // - SetResponseHeader=X-Response-Foo, Bar
                    String values[] = filter.split(",");
                    filterParams.put(GatewayFilter.NAME_KEY, values[0]);
                    filterParams.put(GatewayFilter.VALUE_KEY, values[1]);
                } else if (filterType.equals(SET_STATUS_FILTER)) {
                    // filters:
                    // - SetStatus=401
                    filterParams.put(SetStatusGatewayFilterFactory.STATUS_KEY, filter);
                } else if (filterType.equals(STRIP_PREFIX_FILTER)) {
                    // routes:
                    // - id: nameRoot
                    // uri: http://nameservice
                    // predicates:
                    // - Path=/name/**
                    //  filters:
                    //  - StripPrefix=2
                    //  When a request is made through the gateway to /name/bar/foo the request made to nameservice
                    // will look like http://nameservice/foo.
                    filterParams.put(StripPrefixGatewayFilterFactory.PARTS_KEY, filter);
                } else if (filterType.equals(RETRY_FILTER)) {
                    // filters:
                    // - name: Retry
                    // args:
                    // retries: 3
                    ////////////////////////////////////////
                    // statuses: BAD_GATEWAY
                    // retries: the number of retries that should be attempted
                    // statuses: the HTTP status codes that should be retried, represented using org.springframework.http.HttpStatus
                    // methods: the HTTP methods that should be retried, represented using org.springframework.http.HttpMethod
                    // series: the series of status codes to be retried, represented using org.springframework.http.HttpStatus.Series
                    // {"retries":3,"statuses":"401","methods":"get,post","serices":"1,2,3"}
                    filterParams = JSON.parseObject(filter, HashMap.class);
                }

                filterDefinition.setArgs(filterParams);
                filterDefinitions.add(filterDefinition);
            }
        }
        return filterDefinitions;
    }

    /**
     * 生成路由断言,路由判断规则
     * 路由PredicateDefinition支持的类型有After、Before、Between、Cookie、Header、Host、Method、Path、Query、RemoteAddr
     * 见http://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.0.0.RELEASE/single/spring-cloud-gateway.html
     * 具体参考RoutePredicateFactory路由工厂,有很多实现类
     * <p>
     * 同一个地址可能有不同维度的路由
     */
    private List<PredicateDefinition> predicateList(CommonRoute customerRoute) {
        //predicateDefinition定义,路由参数定义
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();

        List<RouteInfo> routeInfos = customerRoute.getPredicates();
        if (routeInfos != null && routeInfos.size() > 0) {
            //predica名称目前支持的有,After、Before、Between、Cookie、Header、Host、Method、Path、Query、RemoteAddr
            for (RouteInfo routeInfo : routeInfos) {
                PredicateDefinition predicateDefinition = new PredicateDefinition();
                Map<String, String> predicateParams = new HashMap<>(8);
                predicateDefinition.setName(routeInfo.getRouteType());
                String routeType = routeInfo.getRouteType();
                if (routeType.equals(AFTER_ROUTE)) {
                    // predicates:
                    // - After=2017-01-20T17:42:47.789-07:00[America/Denver]
                    predicateParams.put(AfterRoutePredicateFactory.DATETIME_KEY, routeInfo.getRoute());
                } else if (routeType.equals(BEFORE_ROUTE)) {
                    predicateParams.put(BeforeRoutePredicateFactory.DATETIME_KEY, routeInfo.getRoute());
                } else if (routeType.equals(BETWEEN_ROUTE)) {
                    String values[] = routeInfo.getRoute().split(",");
                    predicateParams.put(BetweenRoutePredicateFactory.DATETIME1_KEY, values[0]);
                    predicateParams.put(BetweenRoutePredicateFactory.DATETIME2_KEY, values[1]);
                } else if (routeType.equals(COOKIE_ROUTE)) {
                    String values[] = routeInfo.getRoute().split(",");
                    predicateParams.put(CookieRoutePredicateFactory.NAME_KEY, values[0]);
                    predicateParams.put(CookieRoutePredicateFactory.PATTERN_KEY, values[1]);
                } else if (routeType.equals(HEADER_ROUTE)) {
                    String values[] = routeInfo.getRoute().split(",");
                    predicateParams.put(HeaderRoutePredicateFactory.HEADER_KEY, values[0]);
                    predicateParams.put(HeaderRoutePredicateFactory.REGEXP_KEY, values[1]);
                } else if (routeType.equals(HOST_ROUTE)) {
                    predicateParams.put(HostRoutePredicateFactory.PATTERN_KEY, routeInfo.getRoute());
                } else if (routeType.equals(METHOD_ROUTE)) {
                    predicateParams.put(MethodRoutePredicateFactory.METHOD_KEY, routeInfo.getRoute());
                } else if (routeType.equals(PATH_ROUTE)) {
                    // routes:
                    // - id: host_route
                    // uri: http://example.org
                    // predicates:
                    // - Path=/foo/{segment}
                    predicateParams.put(PathRoutePredicateFactory.PATTERN_KEY, routeInfo.getRoute());
                    predicateParams.put("pathPattern", routeInfo.getRoute());
                } else if (routeType.equals(QUERY_ROUTE)) {
                    String values[] = routeInfo.getRoute().split(",");
                    if (values.length > 1) {
                        predicateParams.put(QueryRoutePredicateFactory.PARAM_KEY, values[0]);
                        predicateParams.put(QueryRoutePredicateFactory.REGEXP_KEY, values[1]);
                    } else {
                        predicateParams.put(QueryRoutePredicateFactory.PARAM_KEY, routeInfo.getRoute());
                        predicateParams.put(QueryRoutePredicateFactory.REGEXP_KEY, null);
                    }
                } else if (routeType.equals(REMOTE_ROUTE)) {
                    predicateParams.put("sources", routeInfo.getRoute());
                }

                predicateDefinition.setArgs(predicateParams);
                predicateDefinitions.add(predicateDefinition);
            }
        }

        return predicateDefinitions;
    }

    /**
     * 直接添加路由到redis,更新也使用此接口直接覆盖
     * 并使用Zset给路由排序
     */
    // @Transactional
    @Override
    public void addRedisRoute(JSONObject jsonObject) {
        double score = (double) (System.currentTimeMillis() / 1000);
        CommonRoute customerRoute = JSON.parseObject(jsonObject.toJSONString(), CommonRoute.class);
        String routeId = customerRoute.getRouteId();
        //hash保存路由
        redisService.hashAdd(ConstantUtil.GW_KEY_PRE,
                routeId,
                JSON.toJSONString(customerRoute),
                ConstantUtil.REDIS_DEFAULT_TTL);
        //sortedset排序
        redisService.zSetAdd(ConstantUtil.GW_SORT_KEY, routeId, score);
    }

    // @Transactional
    @Override
    public void delRedisRoute(JSONObject jsonObject) {
        CommonRoute customerRoute = JSON.parseObject(jsonObject.toJSONString(), CommonRoute.class);
        String routeId = customerRoute.getRouteId();
        redisService.hashDel(ConstantUtil.GW_KEY_PRE, routeId);
        Set routeIdSet = new HashSet();
        routeIdSet.add(routeId);
        redisService.zSetDel(ConstantUtil.GW_KEY_PRE, routeIdSet);
    }


    //添加路由到gateway
    @Override
    public Mono<Void> add(JSONObject jsonObject) {
        CommonRoute customerRoute = JSON.parseObject(jsonObject.toJSONString(), CommonRoute.class);
        RouteDefinition routeDefinition = this.genRoute(customerRoute);
        Mono<RouteDefinition> monoRouteDef = null;
        if (routeDefinition != null) {
            monoRouteDef = Mono.just(routeDefinition);
        }

        if (monoRouteDef == null) {
            return null;
        } else {
            return this.save(monoRouteDef);
        }
    }


    /**
     * 从redis获取路由,这里redis使用StringRedisSerializer序列化，这样手工直接在redis中添加的路由也可以使用
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<CommonRoute> customerRoutes = new ArrayList<>();
        List<RouteDefinition> routeDefinitionList = new ArrayList<>();
        customerRoutes = this.getRedisRoutes(ConstantUtil.GW_KEY_PRE);
        if (customerRoutes != null && customerRoutes.size() > 0) {
            for (CommonRoute customerRoute : customerRoutes) {
                RouteDefinition routeDefinition = this.genRoute(customerRoute);
                if (routeDefinition != null) {
                    routeDefinitionList.add(routeDefinition);
                }
            }
        }
        return Flux.fromIterable(routeDefinitionList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition -> {
            redisService.hashAdd(ConstantUtil.GW_KEY_PRE,
                    routeDefinition.getId(),
                    JSON.toJSONString(routeDefinition),
                    ConstantUtil.REDIS_DEFAULT_TTL);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (redisService.isHashKey(ConstantUtil.GW_KEY_PRE, id)) {
                redisService.hashDel(ConstantUtil.GW_KEY_PRE, id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

}
