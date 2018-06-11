package com.zhilutec.services.impl;
import org.springframework.stereotype.Service;

import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.services.IRedisDataService;
import com.zhilutec.services.IRedisService;

@Service
public class RedisDataServiceImpl extends IRedisService<WarningStatusEntity> implements IRedisDataService{
	
	private static final String REDIS_DATA_KEY = "DATA_INFO";

	@Override
	protected String getRedisKey() {
		return REDIS_DATA_KEY;
	}
	
	
	/**
	 *  根据tagId查询单个犯人当前考勤状态
	 * */
	@Override
	public WarningStatusEntity getRedisData(String tagId)  {	
		return get(tagId);
	}
	
	/**刷新单个囚犯最后一条信息*/
	@Override
	public void updateRedisData(WarningStatusEntity data) {		
			put(data.getTagId().toString(), data, -1);
	
	}
}
