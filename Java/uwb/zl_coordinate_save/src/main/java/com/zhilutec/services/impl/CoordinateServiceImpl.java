package com.zhilutec.services.impl;


import com.zhilutec.dbs.daos.CoordinateDao;
import com.zhilutec.dbs.entities.Coordinate;
import com.zhilutec.services.ICoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordinateServiceImpl implements ICoordinateService {

    @Autowired
    CoordinateDao coordinateDao;

    @Override
    public void batchSave(List<Coordinate> coordinates) {
        if (coordinates != null && coordinates.size() > 0)
            coordinateDao.insertList(coordinates);
    }

}
