package com.zhilutec.services;

import java.util.Map;

import com.zhilutec.db.entities.CoordinateEntity;
import com.zhilutec.db.entities.RedisStatusEnitity;

public interface IRedisStatusService {
	public void updateRedisStatus(CoordinateEntity record);
	public RedisStatusEnitity getStatusRedis(String jsonStr);
	void updateRedisStatus(String jsonStr);
	boolean isOffline(Long tagId);
	boolean isAbsence(Long tagId);
	boolean isAbsence(String code, Long tagId);

}
