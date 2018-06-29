package com.zhilutec.services;


import com.zhilutec.dbs.entities.Level;
import org.springframework.stereotype.Service;

@Service
public interface ILevelService {

    Level getCache(String levelCode);
}
