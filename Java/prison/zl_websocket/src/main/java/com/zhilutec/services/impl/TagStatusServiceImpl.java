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
import com.zhilutec.db.daos.TagStatusDao;
import com.zhilutec.db.entities.TagStatusEntity;
import com.zhilutec.db.params.StatusParam;
import com.zhilutec.db.results.StatusResult;
import com.zhilutec.services.ITagStatusService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional
public class TagStatusServiceImpl implements ITagStatusService {
	
	@Autowired
	private TagStatusDao tagStatusDao;

	@Override
	public int saveStatus(String jsonStr) {	
		TagStatusEntity record = JSON.parseObject(jsonStr, TagStatusEntity.class);
		int rs = tagStatusDao.insertSelective(record);
		return rs;
	}
	
	@Override
	public int saveStatus(TagStatusEntity status) {	
		int rs = tagStatusDao.insertSelective(status);
		return rs;
	}

	/**
	 * 根据ID查询状态
	 * */
	@Override
	public TagStatusEntity findById(Long id) {
		TagStatusEntity record = new TagStatusEntity();
		record.setId(id);		
		return tagStatusDao.selectOne(record);
	}
		
	/**
	 * @param
	 * tag 手环tag
	 * num 查询数量
	 * 根据tag倒序查询状态
	 * */
	@Override
	public List<TagStatusEntity> findByTag(Long tag, Integer num) {
		Example example = new Example(TagStatusEntity.class);
		Example.Criteria recordCriteria = example.createCriteria();
		example.setOrderByClause("id desc");
		recordCriteria.andEqualTo("tagId", tag);

		RowBounds rowBounds = new RowBounds(0, num);
		return tagStatusDao.selectByExampleAndRowBounds(example, rowBounds);

	}

	/**
	 * @param
	 * jsonStr {"tagId":1111,"amount":10}
	  * 根据tag倒序查询指定数量状态
	 * **/
	@Override
	public List<TagStatusEntity> findByTag(String jsonStr) {
		JSONObject params = JSON.parseObject(jsonStr);

		Example example = new Example(TagStatusEntity.class);
		Example.Criteria recordCriteria = example.createCriteria();
		example.setOrderByClause("id desc");
		recordCriteria.andEqualTo("tagId", params.getInteger("tagId"));
		
		RowBounds rowBounds = new RowBounds(0, params.getInteger("amount"));
		return tagStatusDao.selectByExampleAndRowBounds(example, rowBounds);

	}

	/**
	 * 联表查询 
	 * 返回指定手环对应的犯人的指定数量的最新状态
	 * **/
	@Override
	public Result findByParam(String jsonStr) {
		List<Map<String, Number>> onlyStatus = new ArrayList<Map<String, Number>>();
		Map<String, Number> newStatus = new HashMap<String, Number>();
		StatusParam paramObj = JSON.parseObject(jsonStr, StatusParam.class);
		
		List<StatusResult> status = tagStatusDao.queryStatus(paramObj);
		if (status == null || status.isEmpty()) {
			return Result.error(ResultCode.NODATA_ERR.getCode(), ResultCode.NODATA_ERR.getErrmsg());
		}		
		
		StatusResult firstStatus = status.get(0);
		for (StatusResult s : status) {
			newStatus.put("status", s.getStatus());
			newStatus.put("timestamp",s.getTimestamp());
			onlyStatus.add(newStatus);
		}

		firstStatus.getTagId();
		Result r = new Result();
		r.put("name", firstStatus.getName());
		r.put("code", firstStatus.getCode());
		r.put("tagId", firstStatus.getTagId());
		r.put("allStatus", onlyStatus);
		return r;
	}
}