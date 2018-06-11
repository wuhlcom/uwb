/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:49:05 * 
*/
package com.zhilutec.services.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilutec.db.daos.PrisonerDao;
import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IRedisStatusService;

import tk.mybatis.mapper.entity.Example;


@Service
@Transactional
public class PrisonerServiceImpl implements IPrisonerService {

	@Autowired
	private PrisonerDao prisonerDao;
	
	@Resource
	private IRedisStatusService redisStatusService;

	/*
	 * 查询囚犯
	 */
	@Override
	public PrisonerEntity query(Long tagId) {
		PrisonerEntity prisoner = new PrisonerEntity();
		prisoner.setSex(null);
		prisoner.setIsdel(1);
		prisoner.setTagId(tagId);
		try {
			return prisonerDao.selectOne(prisoner);
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 查询囚犯
	 */
	@Override
	public List<PrisonerEntity> queryAll() {
		return prisonerDao.selectAll();
	}
	
	@Override
	public HashMap<String, Object> queryByTagId(Long tagId){
		return prisonerDao.queryByTagId(tagId);
	}
	
	/*
	 * 查询一个监仓的所有囚犯信息
	 */
	@Override
	public List<PrisonerEntity> queryByArea(String areaCode) {
		Example example = new Example(PrisonerEntity.class);
		Example.Criteria recordCriteria = example.createCriteria();
		example.orderBy("id asc");
		recordCriteria.andEqualTo("isdel", 1).andEqualTo("areaCode", areaCode);		
		return prisonerDao.selectByExample(example);
	}
	
	
	/**
	 * 统计单个监仓人数
	 */
	@Override
	public Integer countArea(String areaCode) {
		// JSONObject paramObj = JSON.parseObject(jsonStr);
		Example example = new Example(PrisonerEntity.class);
		Example.Criteria recordCriteria = example.createCriteria();
		recordCriteria.andEqualTo("isdel", 1).andEqualTo("areaCode", areaCode);
		return prisonerDao.selectCountByExample(example);
	}
	
	/*
	 * 监仓考勤统计，总人数，实到人数，缺勤人数*****
	 * 
	 * 缺勤, 离线，电子围栏，串仓，配置任一存在，就认为缺勤****
	 * 
	 */
	@Override
	public Map<String, Integer> checkingIn(String areaCode) {
		int absence = 0;
		Map<String, Integer> resultMap = new HashMap<>();
		List<PrisonerEntity> prisoners = this.queryByArea(areaCode);
		// 计算缺勤人数
		for (PrisonerEntity prisoner : prisoners) {
			String code = prisoner.getCode();
			Long tagId = prisoner.getTagId();
			boolean absenceStatus = redisStatusService.isAbsence(code,tagId);
			if (absenceStatus) {
				absence += 1;
			}
		}

		int total = this.countArea(areaCode);
		int attendances = total - absence;
		resultMap.put("total", total);
		resultMap.put("attendances", attendances);
		resultMap.put("absence", absence);
		return resultMap;
	}

	
}