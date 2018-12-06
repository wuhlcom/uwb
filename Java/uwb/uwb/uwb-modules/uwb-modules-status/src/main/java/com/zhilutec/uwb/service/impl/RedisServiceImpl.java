package com.zhilutec.uwb.service.impl;


import com.zhilutec.uwb.service.IRedisService;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RedisServiceImpl implements IRedisService {

    @Resource
    protected RedisTemplate<String, Object> redisTemplate;

    @Resource
    protected HashOperations<String, String, Object> hashOperations;


    /**
     * 获取redis连接
     */
    private RedisConnection getConn() {
        RedisConnectionFactory redisConnectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        return redisConnection;
    }

    /**
     * 清空当前表
     */
    @Override
    public void flushdb() {
        this.getConn().flushDb();
    }

    /**
     * 生成key
     */
    @Override
    public String genRedisKey(String keyPre, Object o) {
        return keyPre + ":" + o.toString();
    }


    @Override
    public Map hashGetMap(String key) {
        Map rs = hashOperations.entries(key);
        if (rs == null || rs.isEmpty()) {
            return null;
        }
        return rs;
    }

    @Override
    public Map hashGetMap(String keyPre, Object o) {
        String key = this.genRedisKey(keyPre, o);
        return hashGetMap(key);
    }



}