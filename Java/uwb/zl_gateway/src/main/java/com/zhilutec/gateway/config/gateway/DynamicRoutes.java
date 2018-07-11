package com.zhilutec.gateway.config.gateway;


import com.zhilutec.gateway.config.common.utils.ConstantUtil;
import com.zhilutec.gateway.db.CustomerRoute;
import com.zhilutec.gateway.services.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


// #      routes:
// #      - id: restfull_route
// #        uri: http://192.168.10.232
// #        predicates:
// #        - Path=/uwb/resources/**

/**
 * 动态获取路由
 **/
public class DynamicRoutes implements RouteDefinitionRepository {

    @Autowired
    IRedisService redisService;


    @Resource
    GatewayProperties properties;

    public DynamicRoutes(GatewayProperties properties) {
        this.properties = properties;
    }


    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        // Flux.fromIterable(this.properties.getRoutes());
        List<RouteDefinition> routeDefinitionList = this.properties.getRoutes();
        System.out.println("00000000000000000000000000");
        System.out.println(routeDefinitionList);
        List<CustomerRoute> customerRoutes = new ArrayList<>();

        customerRoutes = redisService.routes(ConstantUtil.GW_KEY_PRE);
        if (customerRoutes.size() == 0) {
            CustomerRoute customerRoute = new CustomerRoute();
            customerRoute.setId("restfull_route");
            customerRoute.setUri("http://192.168.10.232");
            customerRoute.setPath("/uwb/resources/**");
            customerRoute.setRouteType("path");
            customerRoutes.add(customerRoute);
        }

        // org.springframework.beans.BeanUtils.copyProperties(customerRoutes, properties);


        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId("test");
        URI uri = null;
        try {
            uri = new URI("http:/test");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        routeDefinition.setUri(uri);

        PredicateDefinition predicateDefinition = new PredicateDefinition();
        RoutePredicateFactory routePredicateFactory = new PathRoutePredicateFactory();
        routePredicateFactory.name();

        predicateDefinition.setName("predicates");
        Map<String, String> args = new LinkedHashMap();
        List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
        args.put("path", "test");
        predicateDefinition.setArgs(args);
        predicateDefinitionList.add(predicateDefinition);

        routeDefinition.setPredicates(predicateDefinitionList);

        routeDefinitionList.add(routeDefinition);

        Flux<RouteDefinition> routeDefinitionFlux = Flux.fromIterable(routeDefinitionList);

        System.out.println("1111111111111111111111");
        System.out.println(routeDefinitionFlux.toString());


        return routeDefinitionFlux;

    }


    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
