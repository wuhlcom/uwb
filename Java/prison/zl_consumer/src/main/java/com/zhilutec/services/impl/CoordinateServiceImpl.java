package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.zhilutec.db.daos.CoordinateDao;
import com.zhilutec.db.entities.CoordinateEntity;
import com.zhilutec.services.ICoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CoordinateServiceImpl implements ICoordinateService {

    @Autowired
    private CoordinateDao coordinateDao;

    /*
     * 保存标签信息
     */
    @Override
    public int saveCoordinate(String jsonStr) {
        CoordinateEntity record = JSON.parseObject(jsonStr, CoordinateEntity.class);
        int rs = coordinateDao.insertSelective(record);
        return rs;
    }

    /*
     * 保存标签信息
     */
    @Override
    public int saveCoordinate(CoordinateEntity record) {
        int rs = coordinateDao.insertSelective(record);
        return rs;
    }

    @Override
    public void saveBatch(List<CoordinateEntity> records) {
        if (records != null && !records.isEmpty())
            coordinateDao.insertList(records);
    }


}