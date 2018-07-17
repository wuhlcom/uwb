package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.CustomerRoute;
import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

public interface IRouteService {
    //从配置文件中获取静态配置的路由
    List<RouteDefinition> getPoperRoutes();

    //定义路由
    RouteDefinition genRoute(CustomerRoute customerRoute, Map<String, String> filterMap);

    //添加路由
    Mono<Void> add(CustomerRoute customerRoute, Map<String, String> filterMap);

    //添加路由
    Mono<Void> add(JSONObject jsonObject);

}
