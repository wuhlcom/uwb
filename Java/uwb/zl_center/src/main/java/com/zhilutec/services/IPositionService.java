package com.zhilutec.services;


import org.springframework.stereotype.Service;


@Service
public interface IPositionService {

    String redisGet(String keyPre, String positionCode);
}
