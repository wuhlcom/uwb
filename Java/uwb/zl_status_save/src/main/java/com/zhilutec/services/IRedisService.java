package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public abstract class IRedisService<T> {

    @Resource
    protected HashOperations<String, String, T> hashOperations;

      /**
     * 存入redis中的key
     */
    protected abstract String getRedisKey();


    public T get(String key, String field) {
        return hashOperations.get(key, field);
    }




}