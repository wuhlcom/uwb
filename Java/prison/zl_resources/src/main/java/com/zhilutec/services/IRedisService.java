package com.zhilutec.services;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;

public abstract class IRedisService<T> {
    
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;
    
     
    
    @Resource
    protected HashOperations<String, String, T> hashOperations;
    
     /**
     * 存入redis中的key
     */
    protected abstract String getRedisKey();
    
    protected String genKey(String keyPre,JSONObject jsonObj) {
		String id = jsonObj.getString("tag_id");
		String type = jsonObj.getString("type");
		String timestamp = jsonObj.getString("timestamp");
		return keyPre + ":" + id +":"+type+":" + timestamp;
	}
    
    protected String genIdKey(String keyPre,JSONObject jsonObj) {
		String id = jsonObj.getString("tag_id");
		return keyPre + ":" + id;
	}
    
     /**
     * 添加
     *
     * @param key    redis key
     * @param field  redis field
     * @param obj 序列化后的对象
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void put(String key,String field, T obj, long expire) {
        hashOperations.put(key, field, obj);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }
    
    public void put(String field, T obj, long expire) {
        hashOperations.put(getRedisKey(), field, obj);
        if (expire != -1) {
            redisTemplate.expire(getRedisKey(), expire, TimeUnit.SECONDS);
        }
    }
    
    /**
     * 删除
     * @param key    redis key
     * @param field  传入field的名称
     */
    public void remove(String key,String field) {
        hashOperations.delete(key, field);
    }
    
    public void remove(String field) {
        hashOperations.delete(getRedisKey(), field);
    }
    
        
    /**
     * 查询
     * @param key   redis key
     * @param field 查询的field
     * @return
     */
    public T get(String key,String field) {
        return hashOperations.get(key, field);
    }
    
    public T get(String field) {
        return hashOperations.get(getRedisKey(), field);
    }
    
    /**
     * 获取当前redis库下所有对象
     * @param key   redis key
     * @return
     */
    public List<T> getAll(String key) {
        return hashOperations.values(key);
    }
    
    public List<T> getAll() {
        return hashOperations.values(getRedisKey());
    }
    
    /**
     * 查询查询当前redis库下所有key
     * @param key   redis key
     * @return
     */
    public Set<String> getKeys(String key) {
        return hashOperations.keys(key);
    }
    
    public Set<String> getKeys() {
        return hashOperations.keys(getRedisKey());
    }
    
    /**
     * 判断key是否存在redis中
     *
     * @param key 传入key的名称
     * @param field field
     * @return
     */
    public boolean isKeyExists(String key,String field) {
        return hashOperations.hasKey(key, field);
    }
    
    public boolean isKeyExists(String field) {
        return hashOperations.hasKey(getRedisKey(), field);
    }
    
    /**
     * 查询当前key下缓存数量
     * @param key   redis key
     * @return
     */
    public long count(String key) {
        return hashOperations.size(key);
    }
    
    public long count() {
        return hashOperations.size(getRedisKey());
    }
    
    /**
     * 清空redis
     */
    public void empty(String key) {
        Set<String> set = hashOperations.keys(key);
        set.stream().forEach(field -> hashOperations.delete(key, field));
    }
    
    public void empty() {
        Set<String> set = hashOperations.keys(getRedisKey());
        set.stream().forEach(field -> hashOperations.delete(getRedisKey(), field));
    }


}