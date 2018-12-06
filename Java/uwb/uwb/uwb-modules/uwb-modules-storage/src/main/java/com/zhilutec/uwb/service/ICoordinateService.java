package com.zhilutec.uwb.service;

import com.zhilutec.uwb.entity.Coordinate;

import java.util.List;

public interface ICoordinateService {

    void batchSave(List<Coordinate> coordinates);
}
