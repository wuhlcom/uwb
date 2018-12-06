package com.zhilutec.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    List<Object> hashMGet(String key, Collection<String> collection);

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

    Set<String> matchedKeys(String keyPre);

    Set<String> keys(String keyPre);

    Boolean zSetAdd(String keyPre, String value, Double score);

    Boolean zSetAdd(String keyPre, Set<String> members);

    void zSetDel(String keyPre, Set<String> members);

    Set<String> zSetRange(String keyPre, Long min, Long max);

    Set<String> zSetRangeByScore(String keyPre, double min, double max, long offset, long limit);

    Set<String> zSetReverseRangeByScore(String keyPre, double min, double max, long offset, long limit);

    Long zSetRank(String keypre, String member);

    Long zSetReverseRank(String keypre, String member);

    Long zSetCount(String keypre, Double min, Double max);

    Long zSetCard(String keypre);
}