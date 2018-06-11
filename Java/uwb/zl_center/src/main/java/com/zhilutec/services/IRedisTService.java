package com.zhilutec.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


public abstract class IRedisTService<T> {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Resource
    protected ValueOperations<String, T> valueOperations;

    /**
     * 存入redis中的key
     */
    protected abstract String getRedisKey(String keyPre,Object obj);


    /**
     * 添加
     *
     * @param key    redis key
     * @param obj    序列化后的对象
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void set(String key, T obj, long expire) {
        valueOperations.set(key, obj);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public T getValue(String key) {
        return valueOperations.get(key);
    }

    /**
     * 删除
     *
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }




}