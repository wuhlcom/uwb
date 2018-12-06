package com.zhilutec.services;

import java.util.List;
import java.util.Map;

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

    Map hashGetMap(String keyPre, Object o);

    /**
     * 删除
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    void delete(String key);


    void delete(String keyPre, Object o);

    /**
     * 删除指定的hash value
     * @param key   redis key
     * @param field 传入field的名称
     */
     Long hashDel(String key, String field);

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


    List<Object> hashValues(String key);
}