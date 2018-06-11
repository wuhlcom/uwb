package com.zhilutec.services;

import java.util.List;

import com.zhilutec.db.entities.FloorEntity;

public interface IFloorService {

	String queryResult(String buildingUniqueCode);

	List<FloorEntity> queryByBuilding(String buildingUniqueCode);


}
