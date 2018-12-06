package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Warning;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICoordinateService {


    String getCoordinates(JSONObject jsonObject);

    String getCoordiantesByTime(JSONObject jsonObject);
}
