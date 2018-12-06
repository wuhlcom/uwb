package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Coordinate;

public interface ICoordinateService {

    Coordinate getCache(Long tagId);


    void addRedisCoordinate(Coordinate coordinate, Long expire);
}
