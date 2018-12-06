package com.zhilutec.uwb.service.impl;


import com.zhilutec.uwb.service.IWarningService;
import com.zhilutec.uwb.dao.WarningDao;
import com.zhilutec.uwb.entity.Warning;
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
