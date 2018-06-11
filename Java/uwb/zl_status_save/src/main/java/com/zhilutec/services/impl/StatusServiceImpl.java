package com.zhilutec.services.impl;


import com.zhilutec.dbs.daos.StatusDao;
import com.zhilutec.dbs.entities.Status;
import com.zhilutec.services.IStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements IStatusService {

    @Autowired
    StatusDao statusDao;

    @Override
    public void batchSave(List<Status> statuses) {
        if (statuses != null && statuses.size() > 0) {
            statusDao.insertList(statuses);
        }

    }

}
