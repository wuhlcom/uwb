package com.zhilutec.uwb.service;

import com.zhilutec.uwb.entity.Level;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILevelService {
    void levelCacheInit();

    //只有两级级别，查询级别
    List<Level> getLevels();

    String getLevelsRs();

    String getlevel();

}
