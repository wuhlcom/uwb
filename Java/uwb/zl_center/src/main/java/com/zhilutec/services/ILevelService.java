package com.zhilutec.services;


import org.springframework.stereotype.Service;

@Service
public interface ILevelService {

    String redisGet(String keyPre, String levelCode);
}
