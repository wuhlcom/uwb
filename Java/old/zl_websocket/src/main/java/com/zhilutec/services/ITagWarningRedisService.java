package com.zhilutec.services;

import java.util.List;

import com.zhilutec.common.result.Result;
import com.zhilutec.db.entities.TagWarningEntity;

public interface ITagWarningRedisService {



	List<TagWarningEntity> findByTag(String jsonStr);

	Result findByParam(String jsonStr);

	int saveWarning(TagWarningEntity warning);

	int saveWarning(String jsonStr);

}
