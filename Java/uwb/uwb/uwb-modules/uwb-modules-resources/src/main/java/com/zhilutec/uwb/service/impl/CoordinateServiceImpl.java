package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.zhilutec.uwb.common.validators.CoordinateValidator;

import com.zhilutec.uwb.dao.CoordinateDao;


import com.zhilutec.uwb.entity.Coordinate;
import com.zhilutec.uwb.common.pojo.CoordinateRs;
import com.zhilutec.uwb.service.ICoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoordinateServiceImpl implements ICoordinateService {

    @Autowired
    CoordinateDao coordinateDao;

    /**
     * 根据人员分组获取坐标点信息
     */
    @Override
    public String getCoordinates(JSONObject jsonObject) {
        Result rs = CoordinateValidator.coordinatesVal(jsonObject);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode())
            return rs.toJSONString();
        List<CoordinateRs> coordinates = coordinateDao.getCoordinates(jsonObject);
        return Result.ok(coordinates).toJSONString();
    }

    /**
     * 根据时间查询坐标，并返回开始和结束时间
     */
    @Override
    public String getCoordiantesByTime(JSONObject jsonObject) {
        Map<String, Object> rsMap = new HashMap<>();
        Long startTime = 0L;
        Long endTime = 0L;
        Result rs = CoordinateValidator.coordinatesVal(jsonObject);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode())
            return rs.toJSONString();

        List<Coordinate> coordinates = coordinateDao.getCoordinatesByTime(jsonObject);
        Integer size = coordinates.size();
        if (size > 0) {
            if (size == 1) {
                Coordinate first = coordinates.get(0);
                startTime = first.getTimestamp();
                endTime = startTime;
            } else {
                Coordinate first = coordinates.get(0);
                Coordinate last = coordinates.get(coordinates.size() - 1);
                startTime = first.getTimestamp();
                endTime = last.getTimestamp();
            }
            rsMap.put("coordinates", coordinates);
            rsMap.put("startTime", startTime);
            rsMap.put("endTime", endTime);
            return Result.ok(rsMap).toJSONString();
        } else {
            return Result.error(ResultCode.NODATA_ERR).toJSONString();
        }
    }

}
