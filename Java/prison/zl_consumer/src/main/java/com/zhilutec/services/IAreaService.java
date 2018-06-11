package com.zhilutec.services;


import java.util.List;

import com.zhilutec.db.entities.AreaEntity;

public interface IAreaService {

    AreaEntity queryArea(String areaCode);

    /**
     * 查询所有监仓信息
     */
    List<AreaEntity> queryAreas();

    /*
     * 查询所有监仓信息
     */
    List<AreaEntity> queryAll();


}
