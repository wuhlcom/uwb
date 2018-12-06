package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.Coordinate;
import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.dbs.pojos.CoordinateRs;
import com.zhilutec.dbs.pojos.WarningRs;

import java.util.List;
import java.util.Map;

public interface CoordinateDao extends MyMapper<Coordinate>, BaseDao<Coordinate> {
    List<CoordinateRs> getCoordinates(Map<String, Object> map);

    List<Coordinate> getCoordinatesByTime(Map<String, Object> map);

}
