package com.zhilutec.cloud.service;

import com.alibaba.fastjson.JSONObject;

import com.zhilutec.cloud.pojo.CommonRoute;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface IRouteService {
    //获取redis中所有的路由缓存
    List<CommonRoute> getRedisRoutes(String keyPre);


    //获取redis中所有的路由缓存
    //直接分页查询
    // Set<String> routeIds = redisService.zSetReverseRangeByScore(ConstantUtil.GW_SORT_KEY,
    //         NEGATIVE_INFINITY,
    //         POSITIVE_INFINITY,
    //         offset,
    //         limit);
    Map<String, Object> getPathRoutes(JSONObject jsonObject);

    String getPathRouteRs(JSONObject jsonObject);

    //直接添加路由到redis
    String addRedisRoute(JSONObject jsonObject);

    String updateRedisRoute(JSONObject jsonObject);

    void delRedisRoute(JSONObject jsonObject);

    //添加路由到gateway
    Mono<Void> add(JSONObject jsonObject) throws URISyntaxException;

}
