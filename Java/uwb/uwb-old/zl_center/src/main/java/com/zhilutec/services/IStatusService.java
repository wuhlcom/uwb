package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Status;

public interface IStatusService {


    void addCache(String keyPre, Long tagId, String field, Object o);

    void addCache(String key, String field, Object o);

    void addCache(Long tagId, Status status);

    Status getCache(Long tagId);

    Object getCache(String key, String field);

}
