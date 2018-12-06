package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Level;
import org.springframework.stereotype.Service;

@Service
public interface ILevelService {

    Level getCache(String levelCode);
}
