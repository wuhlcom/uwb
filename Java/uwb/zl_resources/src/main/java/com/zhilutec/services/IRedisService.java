package com.zhilutec.services;

import org.springframework.data.redis.core.Cursor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface IRedisService {


    /**
     * 清空当前表
     */
    void flushdb();


    /**生成key*/
    String genRedisKey(String keyPre, Object o);

    Object hashGet(String key, String field);

      void hashAdd(String key, String field, Object obj, long expire);

    void hashAddMap(String key, Map map, Long expire);

    Map<String, Object> hashGetMap(String key);

    /**
     * 删除
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    void delete(String key);


    void delete(Collection<String> collection);

    void delete(String keyPre, Object o);

    /**
     * 删除指定的hash value
     * @param key   redis key
     * @param field 传入field的名称
     */
     Long hashDel(String key, String field);

    /**
     * 查询指定的key,field的hash value
     *
     * @param key   redis key
     * @param field 查询的field
     * @return
     */
    // T hashGet(String key, String field);
    //
    //
    // /**
    //  * 获取某个key下所有对象
    //  *
    //  * @param key redis key
    //  * @return
    //  */
    //  List<T> hashGetAll(String key);

    /**
     * 判断key是否存在redis中
     *
     * @param key   传入key的名称
     * @param field field
     * @return
     */
     boolean isHashKey(String key, String field);


    /**
     * 查询当前key下缓存数量
     *
     * @param key redis key
     * @return
     */
     long hashCount(String key);

    /**
     * 清空指定hash key的所有field对应的value
     */
    void hashEmpty(String key);

    //根据获取单个list中的第一个对象
    Object firstList(String key);

    //在list头添加单个对象
    void lelfPush(String key, Object obj, long expire);

    //在list头添加多个对象
    void lelfPush(String key, List<?> objLs, long expire);

    void add(String key, String value, Long expireTime, TimeUnit timeUnit);

    String get(String key);

    Boolean hasKey(String key);

    Set<String> keys(String keyPre);

    void delByKeys(String keyPattern);

    void delAll();

    void initRedis() throws InterruptedException;
}