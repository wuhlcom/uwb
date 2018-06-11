package com.zhilutec.services;

import java.util.List;

import com.zhilutec.common.result.Result;
import com.zhilutec.db.entities.TagStatusEntity;

public interface ITagStatusRedisService {

	int saveStatus(String jsonStr);

	TagStatusEntity findById(Long id);

	List<TagStatusEntity> findByTag(Long tag, Integer num);

	List<TagStatusEntity> findByTag(String jsonStr);

	Result findByParam(String jsonStr);

	int saveStatus(TagStatusEntity status);

}
