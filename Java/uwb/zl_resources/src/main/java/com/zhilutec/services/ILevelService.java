package com.zhilutec.services;

import com.zhilutec.dbs.entities.Level;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILevelService {
    void levelCacheInit();

    //只有两级级别，查询级别
    List<Level> getLevels();

    String getLevelsRs();

    String getlevel();

    void redisAdd(String keyPre, String leveCode, String obj);
}
