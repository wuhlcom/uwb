package com.zhilutec.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface IRedisService {


    /**
     * 清空当前表
     */
    void flushdb();


    /**
     * 生成key
     */
    String genRedisKey(String keyPre, Object o);


    /**
     * 删除
     *
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    void delete(String key);

    void add(String key, String value, Long expireTime, TimeUnit timeUnit);

    String get(String key);

    Boolean hasKey(String key);
}