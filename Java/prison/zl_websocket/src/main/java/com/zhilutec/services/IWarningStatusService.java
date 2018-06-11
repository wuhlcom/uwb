package com.zhilutec.services;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.entities.WarningStatusEntity;

public interface IWarningStatusService {

	int saveWarningStatus(WarningStatusEntity warningStatus);

	Map<String, Long> areaWarningsMap(String jsonStr) throws Exception;

	Map<String, Long> areaWarningsMap(JSONObject paramJson) throws Exception;

}
