package com.zhilutec.services;

import java.util.List;
import java.util.Map;

public interface IRedisService {

    /**
     * 清空当前表
     */
    void flushdb();

    /**
     * 生成key
     */
    String genRedisKey(String keyPre, Object o);


    Map<String, Object> hashGetMap(String key);

    Map hashGetMap(String keyPre, Object o);

    }