package com.zhilutec.services;


import com.zhilutec.dbs.entities.Position;
import org.springframework.stereotype.Service;


@Service
public interface IPositionService {
    Position getCache(String levelCode);
}
