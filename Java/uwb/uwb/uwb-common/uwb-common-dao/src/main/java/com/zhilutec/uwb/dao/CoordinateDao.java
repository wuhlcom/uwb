package com.zhilutec.uwb.dao;


import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.Coordinate;
import com.zhilutec.uwb.common.pojo.CoordinateRs;


import java.util.List;
import java.util.Map;

public interface CoordinateDao extends MyMapper<Coordinate> {
    List<CoordinateRs> getCoordinates(Map<String, Object> map);

    List<Coordinate> getCoordinatesByTime(Map<String, Object> map);

}
