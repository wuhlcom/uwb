package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Strategy;
import org.springframework.transaction.annotation.Transactional;

public interface IWarningService {

    String warnings(JSONObject jsonObject);

    @Transactional
    String getStatistics(JSONObject jsonObject) throws Exception;

    String update(JSONObject jsonObject);


    String getWarningAmount();

    void delWarningCache(Strategy strategyOld);
}
