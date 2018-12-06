package com.zhilutec.uwb.service.impl;

import com.zhilutec.uwb.dao.StatusDao;
import com.zhilutec.uwb.entity.Status;
import com.zhilutec.uwb.service.IStatusService;
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
