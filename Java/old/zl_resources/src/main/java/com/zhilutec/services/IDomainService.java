package com.zhilutec.services;

import java.util.List;

import com.zhilutec.db.entities.DomainEntity;

public interface IDomainService {

	List<DomainEntity> query();

	String queryResult();


}
