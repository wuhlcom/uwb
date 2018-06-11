package com.zhilutec.services.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.WarningUtils;
import com.zhilutec.common.utils.ZlTimeUtil;
import com.zhilutec.db.daos.WarningStatusDao;
import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.services.IWarningStatusService;



@Service
@Transactional
public class WarningStatusServiceImpl implements IWarningStatusService {

	@Autowired
	private WarningStatusDao warningStatusDao;

	@Override
	public int saveWarningStatus(WarningStatusEntity warningStatus){
		int rs = warningStatusDao.insertSelective(warningStatus);
		return rs;
	}	
	
	/**
	 * 区域报警分类统计
	 * 
	 * @return
	 * @throws Exception
	 **/

	private List<Map<String, Object>> warningStatistics(String jsonStr) throws Exception {
		JSONObject paramJson = JSON.parseObject(jsonStr);
		return this.warningStatistics(paramJson);
	}

	private List<Map<String, Object>> warningStatistics(JSONObject paramJson) throws Exception {
		Long startTime = paramJson.getLong("startTime");
		Long endTime = paramJson.getLong("endTime");
		if (startTime == null) {
			startTime = ZlTimeUtil.specStamp();
			paramJson.put("startTime", startTime);
		}
		if (endTime == null) {
			endTime = System.currentTimeMillis() / 1000;
			paramJson.put("endTime", endTime);
		}
		return warningStatusDao.areaWarningStatistics(paramJson);
	}

	/** 区域报警分级统计 **/
	@Override
	public Map<String, Long> areaWarningsMap(String jsonStr) throws Exception {
		JSONObject paramJson = JSON.parseObject(jsonStr);
		return 	this.areaWarningsMap(paramJson);
	}

	/** 区域报警分级统计 **/
	@Override
	public Map<String, Long> areaWarningsMap(JSONObject paramJson) throws Exception {
		Map<String, Long> rsMap = new HashMap<String, Long>();
		rsMap.put(WarningUtils.URGENCY_CODE, 0L);
		rsMap.put(WarningUtils.COMMON_CODE, 0L);
		rsMap.put(WarningUtils.TIP_CODE, 0L);

		List<Map<String, Object>> warnings = this.warningStatistics(paramJson);
		for (Map<String, Object> map : warnings) {
			String key = (String) map.get("level");
			Long value = (Long) map.get("amount");
			if (key.equals(WarningUtils.URGENCY_CODE)) {
				rsMap.put(WarningUtils.URGENCY_CODE, value);
			} else if (key.equals(WarningUtils.COMMON_CODE)) {
				rsMap.put(WarningUtils.COMMON_CODE, value);
			} else if (key.equals(WarningUtils.TIP_CODE)) {
				rsMap.put(WarningUtils.TIP_CODE, value);
			}
		}
		return rsMap;
	}
	

}