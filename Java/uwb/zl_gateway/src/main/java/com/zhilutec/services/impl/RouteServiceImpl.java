package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.config.common.utils.ConstantUtil;
import com.zhilutec.db.CustomerRoute;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IRouteService;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;

/**
 * 使用Redis保存自定义路由配置（代替默认的InMemoryRouteDefinitionRepository）
 * 存在问题：每次请求都会调用getRouteDefinitions，当网关较多时，会影响请求速度，考虑放到本地Map中，使用消息通知Map更新。
 * routes:
 * - id: restfull_route
 * uri: http://192.168.10.232:80
 * predicates:
 * - Path=/uwb/resources/**
 */
@Service
@Component
public class RouteServiceImpl implements IRouteService, RouteDefinitionRepository {

    @Resource
    IRedisService redisService;

    @Resource
    GatewayProperties properties;

    @Resource
    RouteDefinitionRepository routeDefinitionRepository;


    //从配置文件中获取静态配置的路由
    @Override
    public List<RouteDefinition> getPoperRoutes() {
        return this.properties.getRoutes();
    }

    /**
     * 定义路由,包括路由断言和路由过滤
     */
    @Override
    public RouteDefinition genRoute(CustomerRoute customerRoute, Map<String, String> filterMap) {
        //路由定义
        RouteDefinition routeDefinition = new RouteDefinition();
        if (customerRoute != null) {
            List<PredicateDefinition> predicateDefList = this.predicate(customerRoute);
            routeDefinition.setPredicates(predicateDefList);

            routeDefinition.setId(customerRoute.getRouteId());

            String url = customerRoute.getUrl();
            Integer port = customerRoute.getPort();
            String urlPort = url + ":" + port;
            URI uri = UriComponentsBuilder.fromHttpUrl(urlPort).build().toUri();
            routeDefinition.setUri(uri);

            Integer csOrder = customerRoute.getOrder();
            if (csOrder == null)
                csOrder = 0;
            routeDefinition.setOrder(csOrder);
        }

        if (filterMap != null) {
            List<FilterDefinition> filterDefinitionList = this.filter(filterMap);
            routeDefinition.setFilters(filterDefinitionList);
        }
        return routeDefinition;
    }


    /**
     * 过滤器
     * 根据传入的hashmap生成对应的路由过滤条件
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
     * GatewayFilterFactory过滤器工厂有很多实现类
     */
    private List<FilterDefinition> filter(Map<String, String> filterMap) {
        Map<String, String> filterParams = new HashMap<>(8);
        FilterDefinition filterDefinition = new FilterDefinition();
        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            if (entry.getKey().equals("name")) {
                filterDefinition.setName(entry.getValue());
            } else {
                filterParams.put(entry.getKey(), entry.getValue());
            }
        }
        filterDefinition.setArgs(filterParams);
        return Arrays.asList(filterDefinition);
    }

    /**
     * 路由断言
     * 生成路由判断规则
     * RoutePredicateFactory路由工厂,有很多实现类
     */
    private List<PredicateDefinition> predicate(CustomerRoute customerRoute) {
        //predicateDefinition定义,路由参数定义
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        //predica名称目前支持的有,After、Before、Between、Cookie、Header、Host、Method、Path、Query、RemoteAddr
        predicateDefinition.setName(customerRoute.getRouteType());
        predicateParams.put("pattern", customerRoute.getPath());
        predicateParams.put("pathPattern", customerRoute.getPath());
        predicateDefinition.setArgs(predicateParams);
        return Arrays.asList(predicateDefinition);
    }

    //添加路由
    @Override
    public Mono<Void> add(CustomerRoute customerRoute, Map<String, String> filterMap) {
        RouteDefinition routeDefinition = genRoute(customerRoute, filterMap);
        Mono<RouteDefinition> monoRouteDef = Mono.just(routeDefinition);
        return routeDefinitionRepository.save(monoRouteDef);
    }


    //添加路由
    @Override
    public Mono<Void> add(JSONObject jsonObject) {
        CustomerRoute customerRoute = JSON.parseObject(jsonObject.toJSONString(), CustomerRoute.class);
        Map filterMap = JSON.parseObject(jsonObject.toJSONString(), Map.class);
        return add(customerRoute, filterMap);
    }


    /**从redis获取路由,这里redis使用StringRedisSerializer序列化，这样手工直接在redis中添加的路由也可以使用*/
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<CustomerRoute> customerRoutes = new ArrayList<>();
        List<RouteDefinition> routeDefinitionList = new ArrayList<>();
        // redisTemplate.opsForHash().values(ConstantUtil.GW_KEY_PRE).stream().forEach(routeDefinition -> {
        //     routeDefinitions.add(JSON.parseObject(routeDefinition.toString(), RouteDefinition.class));
        // });
        customerRoutes = redisService.routes(ConstantUtil.GW_KEY_PRE);
        for (CustomerRoute customerRoute : customerRoutes) {
            RouteDefinition routeDefinition = this.genRoute(customerRoute, null);
            routeDefinitionList.add(routeDefinition);
        }
        return Flux.fromIterable(routeDefinitionList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route
                .flatMap(routeDefinition -> {
                    // redisTemplate.opsForHash().put(ConstantUtil.GW_KEY_PRE, routeDefinition.getId(),
                    //         JSON.toJSONString(routeDefinition));
                    redisService.hashAdd(ConstantUtil.GW_KEY_PRE, routeDefinition.getId(),
                            JSON.toJSONString(routeDefinition),
                            ConstantUtil.REDIS_DEFAULT_TTL);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            // if (redisTemplate.opsForHash().hasKey(ConstantUtil.GW_KEY_PRE, id)) {
            //     redisTemplate.opsForHash().delete(ConstantUtil.GW_KEY_PRE, id);
            //     return Mono.empty();
            // }
            if (redisService.isHashKey(ConstantUtil.GW_KEY_PRE, id)) {
                redisService.hashDel(ConstantUtil.GW_KEY_PRE, id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

}
