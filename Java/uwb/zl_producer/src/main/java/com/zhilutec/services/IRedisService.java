package com.zhilutec.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.zhilutec.netty.tcpServer.TcpHandler;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;


import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

public abstract class IRedisService<T> {
    @Resource
    HashOperations<String, String, String> hashOperations;

    @Resource
    RedisTemplate<String, String> redisTemplate;

    public void flushDb() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    public void put(String key, String field, T obj, long expire) {
        String jsonStr = JSON.toJSON(obj).toString();
        hashOperations.put(key, field, jsonStr);
        if (expire != -1L) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public String get(String key, String field) {
        return hashOperations.get(key, field);
    }

    public Long remove(String key, String field) {
        return hashOperations.delete(key, field);
    }

}