package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Strategy;
import com.zhilutec.dbs.entities.Warning;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IWarningService {

    List<Warning> getRedisWarnings(String key);

    Warning getReidsWarning(String key, String field);

    void  putRedisWarning(String key, String field, Warning warning, long expire);


    List<Warning> getRedisWarningList(String keyPre, Long tagId);


    List<String> getRedisStrList(String keyPre, Long tagId);

    void delRedisByKey(String keyPre, Long tagId);

    String warnings(JSONObject jsonObject);

    @Transactional
    String getStatistics(JSONObject jsonObject) throws Exception;

    String update(JSONObject jsonObject);

    //删除策略对应的报警,未变化的策略报警不要删除
    //首先取出所有报警,找到匹配的围栏,删除，其它报警要保留
    //区分组策略和单用户策略
    void deleteWarningCache(Strategy strategyOld);

    void deleteRedisWarningStr(String keyPre, Long tagId, String warningStr);

    String getWarningAmount();
}
