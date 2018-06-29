package com.zhilutec.services;


import com.zhilutec.dbs.entities.Coordinate;

public interface ICoordinateService {

    Coordinate getCache(Long tagId);


    void addRedisCoordinate(Coordinate coordinate, Long expire);
}
