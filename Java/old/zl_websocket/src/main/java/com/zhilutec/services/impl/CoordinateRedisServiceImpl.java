package com.zhilutec.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.db.daos.CoordinateDao;
import com.zhilutec.db.entities.CoordinateEntity;
import com.zhilutec.db.params.PointParam;
import com.zhilutec.db.results.PointResult;
import com.zhilutec.services.ICoordinateRedisService;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IRedisStatusService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional
public class CoordinateRedisServiceImpl extends IRedisService<CoordinateEntity> implements ICoordinateRedisService {

	
	@Autowired
	private CoordinateDao coordinateDao;
	
	@Resource
	private IRedisStatusService redisStatusService;	

	private static final String COOR_REDIS_KEY = "TAG_INFO";

	@Override
	protected String getRedisKey() {
		return COOR_REDIS_KEY;
	}	
	
	/*
	 * 保存标签信息
	 */
	@Override
	public int saveCoordinate(String jsonStr) {
		CoordinateEntity record = JSON.parseObject(jsonStr, CoordinateEntity.class);
		int rs = coordinateDao.insertSelective(record);
		return rs;
	}
	
	/*
	 * 保存标签信息
	 */
	@Override
	public int saveCoordinate(CoordinateEntity record) {		
		int rs = coordinateDao.insertSelective(record);
		return rs;
	}
	
	/*
	 * 保存标签信息并刷新状态表
	 */
	@Override
	public int saveCoordinateAndStatus(String jsonStr) {
		CoordinateEntity record = JSON.parseObject(jsonStr, CoordinateEntity.class);
		int rs = coordinateDao.insertSelective(record);
		redisStatusService.updateRedisStatus(record);
		return rs;
	}
	

	/**
	 * 根据ID查询坐标
	 */
	@Override
	public CoordinateEntity findById(Long id) {
		CoordinateEntity coordinate = new CoordinateEntity();
		coordinate.setId(id);
		return coordinateDao.selectOne(coordinate);
	}

	/**
	 * @param tag
	 * 手环tag num 查询数量 根据tag倒序查询指定数量坐标
	 */
	@Override
	public List<CoordinateEntity> findByTag(Long tag, Integer num) {
		Example example = new Example(CoordinateEntity.class);
		Example.Criteria recordCriteria = example.createCriteria();
		example.setOrderByClause("id desc");
		recordCriteria.andEqualTo("tagId", tag);

		RowBounds rowBounds = new RowBounds(0, num);
		return coordinateDao.selectByExampleAndRowBounds(example, rowBounds);

	}

	/**
	 * @param jsonStr
	 *            {"tagId":1111,"amount":10} 根据tag倒序查询指定数量坐标
	 **/
	@Override
	public List<CoordinateEntity> findByTag(String jsonStr) {
		JSONObject params = JSON.parseObject(jsonStr);
		Example example = new Example(CoordinateEntity.class);
		Example.Criteria recordCriteria = example.createCriteria();
		example.setOrderByClause("id desc");
		recordCriteria.andEqualTo("tagId", params.getInteger("tagId"));

		RowBounds rowBounds = new RowBounds(0, params.getInteger("amount"));
		return coordinateDao.selectByExampleAndRowBounds(example, rowBounds);

	}

	/**
	 * 联表查询 返回指定手环对应的犯人的指定数量的最新坐标
	 **/
	@SuppressWarnings("rawtypes")
	@Override
	public Result findByParam(String jsonStr) {
		List<Map<String, Comparable>> onlyPoints = new ArrayList<Map<String, Comparable>>();
		Map<String, Comparable> newPoint = new HashMap<String, Comparable>();
		PointParam paramObj = JSON.parseObject(jsonStr, PointParam.class);

		List<PointResult> points = coordinateDao.queryPoints(paramObj);
		if (points == null || points.isEmpty()) {
			return Result.error(ResultCode.NODATA_ERR.getCode(), ResultCode.NODATA_ERR.getErrmsg());
		}

		PointResult firstPoint = points.get(0);
		for (PointResult pointEntity : points) {
			newPoint.put("posX", pointEntity.getPosX());
			newPoint.put("posY", pointEntity.getPosY());
			newPoint.put("posZ", pointEntity.getPosZ());
			newPoint.put("posCode", pointEntity.getCode());
			newPoint.put("timestamp", pointEntity.getTimestamp());
			onlyPoints.add(newPoint);
		}

		firstPoint.getTagId();
		Result r = new Result();
		r.put("name", firstPoint.getName());
		r.put("code", firstPoint.getCode());
		r.put("tagId", firstPoint.getTagId());
		r.put("points", onlyPoints);
		return r;
	}	

}