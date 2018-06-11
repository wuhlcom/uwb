package com.zhilutec.services;

import java.util.List;

import com.zhilutec.db.entities.BuildingEntity;

public interface IBuildingService {

	List<BuildingEntity> queryByDomain(String domainUniqueCode);

	String queryResult(String domainUniqueCode);


}
