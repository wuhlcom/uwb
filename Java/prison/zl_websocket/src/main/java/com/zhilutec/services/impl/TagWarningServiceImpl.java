package com.zhilutec.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.db.daos.TagWarningDao;
import com.zhilutec.db.entities.TagWarningEntity;
import com.zhilutec.db.params.WarningParam;
import com.zhilutec.db.results.WarningResult;
import com.zhilutec.services.ITagWarningService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional
public class TagWarningServiceImpl implements ITagWarningService {
	
	@Autowired
	private TagWarningDao tagWarningDao;

	// private static final String WORNING_REDIS_KEY = "WORNING_INFO";
    //
	// @Override
	// protected String getRedisKey() {
	// 	return WORNING_REDIS_KEY;
	// }

	@Override
	public int saveWarning(String jsonStr) {
		TagWarningEntity record = JSON.parseObject(jsonStr, TagWarningEntity.class);
		int rs = tagWarningDao.insertSelective(record);
		return rs;
	}
	
	@Override
	public int saveWarning(TagWarningEntity warning) {
		int rs = tagWarningDao.insertSelective(warning);
		return rs;
	}


	/**
	 * @param
	 * jsonStr {"tagId":1111,"amount":10}
	  * 根据tag倒序查询指定数量报警
	 * **/
	@Override
	public List<TagWarningEntity> findByTag(String jsonStr) {
		JSONObject params = JSON.parseObject(jsonStr);

		Example example = new Example(TagWarningEntity.class);
		Example.Criteria recordCriteria = example.createCriteria();
		example.setOrderByClause("id desc");
		recordCriteria.andEqualTo("tagId", params.getInteger("tagId"));
		
		RowBounds rowBounds = new RowBounds(0, params.getInteger("amount"));
		return tagWarningDao.selectByExampleAndRowBounds(example, rowBounds);

	}

	/**
	 * 联表查询 
	 * 返回指定手环对应的犯人的指定数量的最新报警
	 * **/
	@SuppressWarnings("rawtypes")
	@Override
	public Result findByParam(String jsonStr) {
		List<Map<String, Comparable>> onlyWarnings = new ArrayList<Map<String, Comparable>>();
		Map<String, Comparable> newWarning = new HashMap<String, Comparable>();
		WarningParam paramObj = JSON.parseObject(jsonStr, WarningParam.class);
		
		List<WarningResult> warnings = tagWarningDao.queryWarnings(paramObj);
		if (warnings == null || warnings.isEmpty()) {
			return Result.error(ResultCode.NODATA_ERR.getCode(), ResultCode.NODATA_ERR.getErrmsg());
		}		
		
		WarningResult firstWorning = warnings.get(0);
		for (WarningResult worning : warnings) {
			newWarning.put("msg", worning.getMsg());
			newWarning.put("level", worning.getLevel());
			newWarning.put("timestamp", worning.getTimestamp());
			onlyWarnings.add(newWarning);
		}

		firstWorning.getTagId();
		Result r = new Result();
		r.put("name", firstWorning.getName());
		r.put("code", firstWorning.getCode());
		r.put("tagId", firstWorning.getTagId());
		r.put("warnings", onlyWarnings);
		return r;
	}
}