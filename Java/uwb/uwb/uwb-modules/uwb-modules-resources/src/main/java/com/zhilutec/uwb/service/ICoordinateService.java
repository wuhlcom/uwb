package com.zhilutec.uwb.service;

import com.alibaba.fastjson.JSONObject;


public interface ICoordinateService {


    String getCoordinates(JSONObject jsonObject);

    String getCoordiantesByTime(JSONObject jsonObject);
}
