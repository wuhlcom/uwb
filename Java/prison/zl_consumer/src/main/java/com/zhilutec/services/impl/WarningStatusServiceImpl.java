package com.zhilutec.services.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilutec.db.daos.WarningStatusDao;
import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.services.IWarningStatusService;


@Service
@Transactional
public class WarningStatusServiceImpl implements IWarningStatusService {

    @Autowired
    private WarningStatusDao warningStatusDao;

    @Override
    public int saveWarningStatus(WarningStatusEntity warningStatus) {
        int rs = warningStatusDao.insertSelective(warningStatus);
        return rs;
    }

    @Override
    public void saveBatch(List<WarningStatusEntity> records) {
        if (records != null && !records.isEmpty())
            warningStatusDao.insertList(records);
    }

}