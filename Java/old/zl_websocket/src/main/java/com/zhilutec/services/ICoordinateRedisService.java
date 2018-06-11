package com.zhilutec.services;



import java.util.List;

import com.zhilutec.common.result.Result;
import com.zhilutec.db.entities.CoordinateEntity;


public interface ICoordinateRedisService {


	public CoordinateEntity findById(Long id);

	List<CoordinateEntity> findByTag(Long tag, Integer num);
	List<CoordinateEntity> findByTag(String jsonStr);

	Result findByParam(String jsonStr);


	int saveCoordinate(String jsonStr);

	int saveCoordinateAndStatus(String jsonStr);

	int saveCoordinate(CoordinateEntity record);


}
