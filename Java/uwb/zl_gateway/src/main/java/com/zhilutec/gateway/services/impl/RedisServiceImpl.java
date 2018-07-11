package com.zhilutec.gateway.services.impl;

import com.zhilutec.gateway.db.CustomerRoute;
import com.zhilutec.gateway.services.IRedisService;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    public Object hashGet(String key, String field) {
        return hashOperations.get(key, field);
    }

    @Override
    public void hashAdd(String key, String field, Object obj, long expire) {
        hashOperations.put(key, field, obj);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    @Override
    public void hashAddMap(String key, Map map, Long expire) {
        hashOperations.putAll(key, map);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
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

    /**
     * 删除
     *
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(String keyPre, Object o) {
        String key = this.genRedisKey(keyPre, o);
        delete(key);
    }


    /**
     * 删除指定的hash value
     *
     * @param key   redis key
     * @param field 传入field的名称
     */
    @Override
    public Long hashDel(String key, String field) {
        return hashOperations.delete(key, field);
    }


    /**
     * 判断key是否存在redis中
     *
     * @param key   传入key的名称
     * @param field field
     * @return
     */
    @Override
    public boolean isHashKey(String key, String field) {
        return hashOperations.hasKey(key, field);
    }


    /**
     * 查询当前key下缓存数量
     *
     * @param key redis key
     * @return
     */
    @Override
    public long hashCount(String key) {
        return hashOperations.size(key);
    }


    @Override
    public List<Object> hashValues(String key) {
        List<Object> objects = new ArrayList<>();
        List<Object> redisObjects = hashOperations.values(key);
        if (redisObjects == null) {
            return objects;
        }
        return redisObjects;
    }


    @Override
    public Set<String> keys(String keyPre) {
        String keyPattern = keyPre + "*";
        return redisTemplate.keys(keyPattern);
    }

    @Override
    public List<CustomerRoute> routes(String keyPre) {
        List<CustomerRoute> routes = new ArrayList<>();
        Set<String> keyValues = keys(keyPre);
        for (String keyValue : keyValues) {
            CustomerRoute constomerRoute = (CustomerRoute) hashGetMap(keyValue);
            routes.add(constomerRoute);
        }

        return routes;

    }


}