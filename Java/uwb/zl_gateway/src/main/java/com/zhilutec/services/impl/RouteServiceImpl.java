package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.common.validators.RouteValidator;
import com.zhilutec.dbs.entities.CommonRoute;
import com.zhilutec.dbs.entities.FilterInfo;
import com.zhilutec.dbs.entities.PathRoute;
import com.zhilutec.dbs.entities.RouteInfo;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IRouteRefreshService;
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
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Filter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * 这里通用路由是指redis中保存的路由，支持Gateway实际路由的11种路由匹配条件和23种过滤条件
 * 在使用时需要根据情况转换成Gateway需要的真实路由格式，目前只实现path类型真实路由转换
 */
@Service
@Component
public class RouteServiceImpl implements IRouteService, RouteDefinitionRepository {

    @Resource
    IRedisService redisService;

    // @Resource
    // DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator;

    //从配置文件中获取静态配置的路由
    // private List<RouteDefinition> getPropRoutes() {
    //     return this.gatewayProperties.getRoutes();
    // }

    //获取自动发现的路由
    // private Flux<RouteDefinition> getDiscoverRoutes() {
    //     Flux<RouteDefinition> routeDefinitionFlux = this.discoveryClientRouteDefinitionLocator.getRouteDefinitions();
    //     return routeDefinitionFlux;
    // }

    //从内存获取路由
    private Flux<RouteDefinition> getMemRoutes() {
        InMemoryRouteDefinitionRepository inMemoryRouteDefinitionRepository = new InMemoryRouteDefinitionRepository();
        Flux<RouteDefinition> routeFlux = inMemoryRouteDefinitionRepository.getRouteDefinitions();
        return routeFlux;
    }

    //获取redis中有效的路由缓存，availabel为0路由会报错这里要去除
    @Override
    public List<CommonRoute> getRedisRoutes(String keyPre) {
        List<CommonRoute> routes = new ArrayList<>();
        Set<String> keys = redisService.keys(keyPre);
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                if (key != null) {
                    List<Object> routeStrlist = redisService.hashValues(key);
                    for (Object route : routeStrlist) {
                        if (route != null) {
                            CommonRoute commonRoute = JSON.parseObject(route.toString(), CommonRoute.class);

                            //url处理前缀
                            String url = commonRoute.getUrl();
                            String urlType = commonRoute.getUrlType();
                            if (urlType != null) {
                                urlType = urlType.toUpperCase();
                            }

                            //大小写处理
                            commonRoute.setUrlType(urlType);
                            if (url != null && urlType != null) {
                                if (urlType.equals(ConstantUtil.GW_URI_LB)) {
                                    // 验证规则
                                    String regEx = "^lb://.*";
                                    // 编译正则表达式,忽略大小写的写法
                                    Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                                    Matcher matcher = pattern.matcher(url);
                                    // 字符串是否与正则表达式相匹配
                                    // 查找字符串中是否有匹配正则表达式的字符/字符串
                                    boolean rs = matcher.find();
                                    if (!rs) {
                                        commonRoute.setUrl(ConstantUtil.GW_LB_PRE + url);
                                    }
                                } else {
                                    // 验证规则
                                    String regEx = "^http://.*";
                                    // 编译正则表达式,忽略大小写的写法
                                    Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                                    Matcher matcher = pattern.matcher(url);
                                    // 字符串是否与正则表达式相匹配
                                    // 查找字符串中是否有匹配正则表达式的字符/字符串
                                    boolean rs = matcher.find();
                                    if (!rs) {
                                        commonRoute.setUrl(ConstantUtil.GW_HTTP_PRE + url);
                                    }
                                }
                            } else {
                                continue;
                            }

                            Integer available = commonRoute.getAvailable();
                            if (available.intValue() == 1) {
                                routes.add(commonRoute);
                            }
                        }
                    }
                }
            }
        }
        return routes;
    }

    /**
     * 获取redis中所有的路由缓存
     * 直接分页查询,这NEGATIVE_INFINITY POSITIVE_INFINITY这两个值对应-inf +inf但目前不生效
     * Set<String> routeIds = redisService.zSetReverseRangeByScore(ConstantUtil.GW_SORT_KEY,
     * NEGATIVE_INFINITY,
     * POSITIVE_INFINITY,
     * offset,
     * limit);
     * 替代方案
     * 在sorted set中在保存路由id,以时间为score排序
     * 并在sorted set中自定义实现分页查询功能
     * 支持按路由id模糊查询
     */
    @Override
    public Map<String, Object> getPathRoutes(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        Integer total = 0;
        Long card = redisService.zSetCard(ConstantUtil.GW_SORT_KEY);
        Long page = jsonObject.getLong("page");
        //默认起始页处理
        if (page == null) {
            page = 0L;
        } else if (page > 0) {
            page = page - 1;
        }

        Long listRows = jsonObject.getLong("listRows");

        //计算实际查询时起始编号
        Long offset = page * listRows;
        //计算实际查询时结束编号
        Long limit = (page + 1) * listRows - 1;
        String rId = jsonObject.getString("routeId");

        List<PathRoute> pathRoutes = new ArrayList<>();
        if (card.longValue() > 0L) {
            Set<String> routeIdSome = new HashSet<>();
            Set<String> routeIds = new HashSet<>();
            //按routeId搜索处理，模糊匹配
            if (rId != null && !rId.isEmpty()) {
                //先查出所有的路由
                Set<String> routeIdAll = redisService.zSetReverseRange(ConstantUtil.GW_SORT_KEY, 0L, card);
                for (String routeId : routeIdAll) {
                    // 验证规则
                    String regEx = ".*" + rId.trim() + ".*";
                    // 编译正则表达式,忽略大小写的写法
                    Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(routeId);
                    // 字符串是否与正则表达式相匹配
                    // 查找字符串中是否有匹配正则表达式的字符/字符串
                    boolean find = matcher.find();
                    //找出所有匹配的记录
                    if (find) {
                        routeIdSome.add(routeId);
                    }
                }

                //分页处理
                Integer index = 0;
                if (routeIdSome.size() > 0) {
                    for (String routeId : routeIdSome) {
                        if (index.intValue() == offset.intValue()) {
                            routeIds.add(routeId);
                            if (routeIds.size() == listRows) {
                                break;
                            }
                        } else {
                            index += 1;
                        }
                    }
                    total = routeIdSome.size();
                }
            } else {
                //无过滤条件查询
                routeIds = redisService.zSetReverseRange(ConstantUtil.GW_SORT_KEY, offset, limit);
                total = card.intValue();
            }

            if (routeIds != null && routeIds.size() > 0) {
                List<Object> values = redisService.hashMGet(ConstantUtil.GW_KEY_PRE, routeIds);
                if (values != null && values.size() > 0) {
                    for (Object value : values) {
                        if (value != null) {
                            CommonRoute commonRoute = JSON.parseObject(value.toString(), CommonRoute.class);
                            //目前只支持path route,所以直接转成pathRoute不做类型判断，后续扩展了其它类型路由再做判断
                            PathRoute pathRoute = this.convert2PathRoute(commonRoute);
                            if (pathRoute != null) {
                                pathRoutes.add(pathRoute);
                            }
                        }
                    }
                }
            }
        }

        result.put("routes", pathRoutes);
        result.put("total", total);

        return result;
    }


    @Override
    public String getPathRouteRs(JSONObject jsonObject) {
        Map routeMap = this.getPathRoutes(jsonObject);
        List<PathRoute> pathRoutes = (List<PathRoute>) routeMap.get("routes");
        if (pathRoutes == null || pathRoutes.size() == 0) {
            return Result.error(ResultCode.NODATA_ERR).toJSONString();
        } else {
            Map<String, Object> rs = new HashMap<>();
            rs.put("totalRows", routeMap.get("total"));
            rs.put("result", pathRoutes);
            return Result.ok(rs).toJSONString();
        }
    }

    /**
     * 由于目前只关注Path类型路由，且不关注过滤条件，
     * 所以这里将通用路由转成Path路由
     */
    private PathRoute convert2PathRoute(CommonRoute commonRoute) {
        PathRoute pathRoute = new PathRoute();
        List<RouteInfo> routeInfos = commonRoute.getPredicates();
        Boolean flag = false;
        for (RouteInfo routeInfo : routeInfos) {
            String routeType = routeInfo.getRouteType();
            if (routeType.equals(ConstantUtil.PATH_ROUTE)) {
                String pathRouteStr = routeInfo.getRoute();
                pathRoute.setRoute(pathRouteStr);
                flag = true;
                break;
            }
        }

        if (flag) {
            pathRoute.setRouteId(commonRoute.getRouteId());
            pathRoute.setAvailable(commonRoute.getAvailable());
            pathRoute.setOrder(commonRoute.getOrder());
            pathRoute.setUrl(commonRoute.getUrl());
            pathRoute.setUrlType(commonRoute.getUrlType());
        } else {
            pathRoute = null;
        }
        return pathRoute;
    }

    /**
     * 将非通用的路由转成通用路由
     * 如 将json转成通用路由
     */
    private CommonRoute convert2CommonRoute(JSONObject jsonObject) {
        PathRoute pathRoute = JSON.parseObject(jsonObject.toJSONString(), PathRoute.class);
        CommonRoute commonRoute = new CommonRoute();
        RouteInfo routeInfo = new RouteInfo();
        List<RouteInfo> predicates = new ArrayList<>();

        String routeId = pathRoute.getRouteId();
        String url = pathRoute.getUrl();
        String route = pathRoute.getRoute();

        if (routeId == null || routeId.isEmpty() || url == null || url.isEmpty() || route == null || route.isEmpty()) {
            return null;
        }
        commonRoute.setRouteId(routeId);
        commonRoute.setAvailable(pathRoute.getAvailable());
        commonRoute.setOrder(pathRoute.getOrder());
        commonRoute.setUrl(url);
        commonRoute.setUrlType(pathRoute.getUrlType());

        routeInfo.setRouteType(pathRoute.getRouteType());
        routeInfo.setRoute(route);
        predicates.add(routeInfo);

        commonRoute.setPredicates(predicates);
        commonRoute.setFilters(null);

        return commonRoute;
    }

    private CommonRoute convert2CommonRoute(PathRoute pathRoute) {
        CommonRoute commonRoute = new CommonRoute();
        RouteInfo routeInfo = new RouteInfo();
        List<RouteInfo> predicates = new ArrayList<>();
        String routeId = pathRoute.getRouteId();
        String url = pathRoute.getUrl();
        String route = pathRoute.getRoute();
        if (routeId == null || routeId.isEmpty() || url == null || url.isEmpty() || route == null || route.isEmpty()) {
            return null;
        }
        commonRoute.setRouteId(routeId);
        commonRoute.setAvailable(pathRoute.getAvailable());
        commonRoute.setOrder(pathRoute.getOrder());
        commonRoute.setUrl(url);
        commonRoute.setUrlType(pathRoute.getUrlType());

        routeInfo.setRouteType(pathRoute.getRouteType());
        routeInfo.setRoute(route);
        predicates.add(routeInfo);

        commonRoute.setPredicates(predicates);
        commonRoute.setFilters(null);

        return commonRoute;
    }


    /**
     * 定义路由,包括路由断言和路由过滤
     */
    private RouteDefinition genRoute(CommonRoute customerRoute) throws URISyntaxException {
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
                //不能处理lb类型
                // URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();
                //可以实例化lb,http类型
                URI uri = new URI(url);
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
                    filterParams.put(RewritePathGatewayFilterFactory.REGEXP_KEY, values[0]);
                    filterParams.put(RewritePathGatewayFilterFactory.REPLACEMENT_KEY, values[1]);
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
     * rewrite过滤是默认的过滤规则当过滤条件为空是添加rewrite过滤
     * 当过滤条件不为空且有rewrite过滤则不添加re
     */
    public void addRedisRouteDefault(JSONObject jsonObject) {
        double score = (double) (System.currentTimeMillis() / 1000);
        CommonRoute customerRoute = JSON.parseObject(jsonObject.toJSONString(), CommonRoute.class);
        List<RouteInfo> routeInfos = customerRoute.getPredicates();
        List<FilterInfo> filters = customerRoute.getFilters();
        if (filters == null) {
            filters = new ArrayList<>();
        }

        //只对Path类型添加默认filters
        Boolean flag = false;
        for (RouteInfo routeInfo : routeInfos) {
            String routeType = routeInfo.getRouteType();
            if (routeType.equals(ConstantUtil.PATH_ROUTE)) {
                String route = routeInfo.getRoute();
                if (filters.size() <= 0) {
                    FilterInfo filterInfo = new FilterInfo();
                    filterInfo.setFilterType(ConstantUtil.REWRITE_PATH_FILTER);
                    filterInfo.setFilter(route + "," + route);
                    filters.add(filterInfo);
                    customerRoute.setFilters(filters);
                }
                flag = true;
            }
            if (flag) {
                break;
            }
        }
        String routeId = customerRoute.getRouteId();
        //hash保存路由
        redisService.hashAdd(ConstantUtil.GW_KEY_PRE,
                routeId,
                JSON.toJSONString(customerRoute),
                ConstantUtil.REDIS_DEFAULT_TTL);
        //sortedset排序
        redisService.zSetAdd(ConstantUtil.GW_SORT_KEY, routeId, score);
    }


    /**
     * 直接添加路由到redis
     * 并使用Zset给路由排序
     */
    @Override
    public String addRedisRoute(JSONObject jsonObject) {
        Result validator = RouteValidator.add(jsonObject);
        String rs = null;
        if ((Integer) validator.get("errcode") != ResultCode.SUCCESS.getCode()) {
            rs = validator.toJSONString();
        } else {
            try {
                double score = (double) (System.currentTimeMillis() / 1000);
                String urlType = jsonObject.getString("urlType").toUpperCase();

                jsonObject.put("urlType", urlType);
                if (urlType.equals(ConstantUtil.GW_URI_LB)) {
                    String url = jsonObject.getString("url").toUpperCase();
                    jsonObject.put("url", url);
                }

                CommonRoute commonRoute = this.convert2CommonRoute(jsonObject);
                if (commonRoute == null) {
                    rs = Result.error(ResultCode.FAILED.getCode(), "转换路由格式失败").toJSONString();
                } else {
                    String routeId = commonRoute.getRouteId();
                    //hash保存路由
                    redisService.hashAdd(
                            ConstantUtil.GW_KEY_PRE,
                            routeId,
                            JSON.toJSONString(commonRoute),
                            ConstantUtil.REDIS_DEFAULT_TTL);
                    //sorted set保存路由id并按时间对路由进行排序
                    Boolean isSetAdd = redisService.zSetAdd(ConstantUtil.GW_SORT_KEY, routeId, score);
                    if (isSetAdd) {
                        rs = Result.ok(ResultCode.SUCCESS).toJSONString();
                    } else {
                        rs = Result.ok(ResultCode.FAILED.getCode(), "添加路由失败或路由已存在").toJSONString();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.ok(ResultCode.UNKNOW_ERR).toJSONString();
            }
        }
        return rs;
    }


    /**
     * 路由Id不允许修改
     * 只修改route url available order urlType
     */
    @Override
    public String updateRedisRoute(JSONObject jsonObject) {
        Result validator = RouteValidator.update(jsonObject);
        if ((Integer) validator.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return validator.toJSONString();
        }

        String rId = jsonObject.getString("routeId");
        //通过rank来判断路由是否存在
        Long rank = redisService.zSetRank(ConstantUtil.GW_SORT_KEY, rId);
        if (rank == null) {
            return Result.error(ResultCode.FAILED.getCode(), "找不到对应的路由").toJSONString();
        } else {
            Object obj = redisService.hashGet(ConstantUtil.GW_KEY_PRE, rId);
            CommonRoute commonRoute = JSON.parseObject(obj.toString(), CommonRoute.class);
            //目前只支持path route,所以直接转成pathRoute不做类型判断，后续扩展了其它类型路由再做判断
            PathRoute pathRoute = this.convert2PathRoute(commonRoute);
            // PathRoute newPathRoute =JSONObject.parseObject(jsonObject.toJSONString(),PathRoute.class);
            //更新路由
            Boolean flag = false;
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.equals("route") && value != null) {
                    pathRoute.setRoute(value.toString());
                    flag = true;
                }

                if (key.equals("url") && value != null) {
                    pathRoute.setUrl(value.toString());
                    flag = true;
                }

                if (key.equals("urlType") && value != null) {
                    pathRoute.setUrlType(value.toString());
                    flag = true;
                }

                if (key.equals("order") && value != null) {
                    pathRoute.setOrder(Integer.parseInt(String.valueOf(value)));
                    flag = true;
                }

                if (key.equals("available") && value != null) {
                    pathRoute.setAvailable(Integer.parseInt(String.valueOf(value)));
                    flag = true;
                }
            }
            if (flag) {
                double score = (double) (System.currentTimeMillis() / 1000);
                CommonRoute newCommonRoute = convert2CommonRoute(pathRoute);
                //hash更新路由
                redisService.hashAdd(ConstantUtil.GW_KEY_PRE,
                        rId,
                        JSON.toJSONString(newCommonRoute),
                        ConstantUtil.REDIS_DEFAULT_TTL);
                //更新路由时间
                redisService.zSetAdd(ConstantUtil.GW_SORT_KEY, rId, score);
            }
            return Result.ok(ResultCode.SUCCESS).toJSONString();
        }
    }

    @Override
    public void delRedisRoute(JSONObject jsonObject) {
        CommonRoute customerRoute = JSON.parseObject(jsonObject.toJSONString(), CommonRoute.class);
        String routeId = customerRoute.getRouteId();
        redisService.hashDel(ConstantUtil.GW_KEY_PRE, routeId);
        redisService.zSetDel(ConstantUtil.GW_SORT_KEY, routeId);
    }


    //添加路由到gateway
    @Override
    public Mono<Void> add(JSONObject jsonObject) throws URISyntaxException {
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
                RouteDefinition routeDefinition = null;
                try {
                    routeDefinition = this.genRoute(customerRoute);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
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
