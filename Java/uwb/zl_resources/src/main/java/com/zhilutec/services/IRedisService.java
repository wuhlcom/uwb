package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Strategy;
import com.zhilutec.dbs.pojos.RedisPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class IRedisService<T> {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Resource
    protected HashOperations<String, String, T> hashOperations;

    @Resource
    protected ListOperations<String, T> listOperations;

    @Resource
    ListOperations<String, String> listStr;

    @Resource
    protected JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 存入redis中的key
     */
    protected abstract String getRedisKey();


    /**
     * 获取一个redis连接 客户端
     */
    public RedisConnection getRedisConn() {
        // JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        return redisConnection;
    }
    /**
     * 清空当前表
     */
    public void flushdb() {
        this.getRedisConn().flushDb();
    }

    protected String genIdKey(String keyPre, Long tagId) {
        return keyPre + ":" + tagId.toString();
    }

    //根据获取单个list中的对象
    public T firstList(String key) {
        return this.getList(key, 0);
    }

    //根据获取单个list中的对象
    public T getList(String key, long index) {
        return listOperations.index(key, index);
    }

    //查询所有
    public List<T> listAll(String key) {
        return this.getSomeList(key, 0, -1);
    }

    public List<T> getSomeList(String key, long start, long end) {
        return listOperations.range(key, start, end);
    }


    //根据获取单个list中的对象
    public String getElStr(String key, long index) {
        return listStr.index(key, index);
    }

    //在list头添加单个对象
    public void lelfPushStr(String key, String obj, long expire) {
        listStr.leftPush(key, obj);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    //查询所有
    public List<String> listStr(String key) {
        return this.getSomeStr(key, 0, -1);
    }

    public List<String> getSomeStr(String key, long start, long end) {
        return listStr.range(key, start, end);
    }

    //删除list中某个对象
    public Long remove(String key, long count, String obj) {
        return listStr.remove(key, count, obj);
    }

    //删除list中某个对象
    public Long remove(String key, long count, T obj) {
        return listOperations.remove(key, count, obj);
    }


    /**
     * 删除
     *
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }


    /**
     * 添加
     *
     * @param key    redis key
     * @param field  redis field
     * @param obj    序列化后的对象
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void put(String key, String field, T obj, long expire) {
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

    public void strSet(String key, String value, Long expire) {
        stringRedisTemplate.opsForValue().set(key, value);
        if (expire != -1) {
            stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }


    /**
     * 删除
     *
     * @param key   redis key
     * @param field 传入field的名称
     */
    public Long remove(String key, String field) {
        return hashOperations.delete(key, field);
    }

    public void remove(String field) {
        hashOperations.delete(getRedisKey(), field);
    }


    /**
     * 查询
     *
     * @param key   redis key
     * @param field 查询的field
     * @return
     */
    public T get(String key, String field) {
        return hashOperations.get(key, field);
    }

    public T get(String field) {
        return hashOperations.get(getRedisKey(), field);
    }

    /**
     * 获取当前redis库下所有对象
     *
     * @param key redis key
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
     *
     * @param key redis key
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
     * @param key   传入key的名称
     * @param field field
     * @return
     */
    public boolean isKeyExists(String key, String field) {
        return hashOperations.hasKey(key, field);
    }

    public boolean isKeyExists(String field) {
        return hashOperations.hasKey(getRedisKey(), field);
    }

    /**
     * 查询当前key下缓存数量
     *
     * @param key redis key
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