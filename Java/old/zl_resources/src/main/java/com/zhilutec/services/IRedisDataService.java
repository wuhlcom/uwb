package com.zhilutec.services;

import com.zhilutec.db.entities.WarningStatusEntity;

public interface IRedisDataService {

	WarningStatusEntity getRedisData(String jsonStr);

	void updateRedisData(WarningStatusEntity data);

}
