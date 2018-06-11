package com.zhilutec.services;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.entities.WarningStatusEntity;

public interface IWarningStatusService {

	int saveWarningStatus(WarningStatusEntity warningStatus);

    void saveBatch(List<WarningStatusEntity> records);
}
