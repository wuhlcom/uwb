package com.zhilutec.services.impl;

import com.zhilutec.dbs.daos.WarningDao;
import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.services.IWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningServiceImpl  implements IWarningService {

    @Autowired
    WarningDao warningDao;

    @Override
    public void batchSave(List<Warning> warnings) {
        if (warnings != null && warnings.size() > 0)
            warningDao.insertList(warnings);
    }
}
