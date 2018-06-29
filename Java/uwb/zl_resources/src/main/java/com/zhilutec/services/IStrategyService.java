package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Strategy;
import com.zhilutec.dbs.pojos.RedisPolicy;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

public interface IStrategyService {
    //初始化策略缓存
    @Transactional
    void policyCacheInit() throws InterruptedException;

    @Transactional
    String add(JSONObject jsonObject) throws ParseException, InterruptedException;

    String strategies(JSONObject jsonObject);

    @Transactional
    String policySwitch(JSONObject jsonObject) throws InterruptedException;

    String update(JSONObject jsonObject) throws ParseException, InterruptedException;

    @Transactional
    String delete(JSONObject jsonObject);

    @Transactional
    void deleteByDptUser(List<String> strategyUserIds);

    void addTagidPolicy(Long tagId, String strategyCode, RedisPolicy redisPolicy);

    //删除单个标签下的某个策略缓存,一个标签可能有多个策略缓存
    void delTagidPolicy(Long tagId, String strategyCode);

    List<Strategy> getStrategyByFenceCodes(List<String> fenceCodes);

    List<Strategy> getStrategiesByFenceCode(String fenceCode, Integer available);

    List<Strategy> getStrategyByUserId(String strategyUserId);

}
