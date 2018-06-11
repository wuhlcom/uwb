package com.zhilutec.services;

import com.zhilutec.db.entities.CoordinateEntity;

import java.util.List;

public interface ICoordinateService {


    /*
* 保存标签信息
*/
    int saveCoordinate(String jsonStr);

    /*
* 保存标签信息
*/
    int saveCoordinate(CoordinateEntity record);

    void saveBatch(List<CoordinateEntity> records);
}