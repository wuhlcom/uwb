package com.zhilutec.services;


import com.zhilutec.dbs.entities.Coordinate;

public interface ICoordinateService {


    Coordinate getReidsCoordinate(String key, String field);

    Coordinate getReidsCoordinate(Long tagId);

    void deleteRedisCoordinate(String key, String field);


    void deleteRedisCoordinate(Long tagId);

    void addRedisCoordinate(Coordinate coordinate, Long expire);
}
