package com.zhilutec.services;

import com.zhilutec.dbs.entities.Coordinate;

import java.util.List;

public interface ICoordinateService {

    void batchSave(List<Coordinate> coordinates);
}
