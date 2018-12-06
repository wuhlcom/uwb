package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Warning;

import java.util.List;

public interface IWarningService {


    //获取缓存中围栏报警
    List<Warning> getFenceWarnings(String key);

    //获取缓存中其它报警
    Warning getCache(String key);

    Warning getCache(String keyPre, Object o);

    List<Warning> getFenceWarnings(Long tagId);

    void addRedisWarning(Long tagId, String strategyCode, Warning warning);

    //删除单个报警缓存
    Long delRedisWarning(String strategyCode, Long tagId, String warningKeyPre);

    void addCache(String keyPre, Long tagId, Warning warning);
}
