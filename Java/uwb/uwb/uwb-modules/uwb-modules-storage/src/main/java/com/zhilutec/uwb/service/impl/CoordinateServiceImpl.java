package com.zhilutec.uwb.service.impl;



import com.zhilutec.uwb.service.ICoordinateService;
import com.zhilutec.uwb.dao.CoordinateDao;
import com.zhilutec.uwb.entity.Coordinate;
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
