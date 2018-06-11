package com.zhilutec.services;

import com.zhilutec.dbs.entities.Warning;

import java.util.List;

public interface IWarningService {

    List<Warning> getRedisWarnings(String key);

    Warning getReidsWarning(String key, String field);

    Warning getReidsWarning(Long tagId, String strategyCode, Integer level);

    void  deleteRedisWarning(String key, String field);

    void deleteRedisWarning(Long tagId, String strategyCode, Integer level);

    void deleteRedisWarning(Long tagId, Integer warningType);

    void putRedisWarning(Long tagId, String strategyCode, Integer level, Warning warning, long expire);

    void addRedisWarning(Long tagId, Integer warningType, Warning warning, long expire);



    List<Warning> getRedisWarningList(Long tagId);


    List<String> getRedisStrList(Long tagId);

    void addRedisWarning(Long tagId, Warning warning);



    void addRedisWarning(Long tagId, String str);

    void deleteRedisWarning(Long tagId, Warning warning);



    void deleteRedisWarning(Long tagId, String str);
}
