package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Position;
import org.springframework.stereotype.Service;


@Service
public interface IPositionService {
    Position getCache(String levelCode);
}
