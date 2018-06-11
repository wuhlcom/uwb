package com.zhilutec.services;

public interface IStatusService {

    void redisAdd(String keyPre, Long tagId, String msg);

    void redisDel(String keyPre, Long tagId);

    String  redisGet(String keyPre, Long tagId);
}
