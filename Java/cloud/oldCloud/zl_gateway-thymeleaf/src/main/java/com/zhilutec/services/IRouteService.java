package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.CommonRoute;
import com.zhilutec.dbs.entities.PathRoute;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IRouteService {
    //获取redis中所有的路由缓存
    List<CommonRoute> getRedisRoutes(String keyPre);

    //获取redis中所有的路由缓存
    List<PathRoute> getPathRoutes(JSONObject jsonObject);

    String getPathRouteRs(JSONObject jsonObject);

    //直接添加路由到redis
    void addRedisRoute(JSONObject jsonObject);

    void delRedisRoute(JSONObject jsonObject);

    //添加路由到gateway
    Mono<Void> add(JSONObject jsonObject);

}
