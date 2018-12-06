package com.zhilutec.uwb.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IRedisService {

    /**
     * 清空当前表
     */
    void flushdb();


    /**
     * 生成key
     */
    String genRedisKey(String keyPre, Object o);


    void hashAddMap(String key, Map map, Long expire);

    Map<String, Object> hashGetMap(String key);

    /**
     * 删除
     *
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    void delete(String key);

    void delete(String keyPre, Object o);

    void delete(Collection<String> collection);

    /**
     * 删除指定的hash value
     *
     * @param key   redis key
     * @param field 传入field的名称
     */
    Long hashDel(String key, String field);


    Set<String> keys(String keyPre);

    void delByKeys(String keyPattern);

    void delAll();
}