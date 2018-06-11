package com.zhilutec.services;

import com.zhilutec.db.entities.RedisStatusEnitity;

public interface IRedisStatusService {
	public RedisStatusEnitity getStatusRedis(String jsonStr);
	boolean redisStatus(Long tagId);
	boolean absenceStatus(String code, Long tagId);

}
