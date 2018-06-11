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

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.AbsenceUtil;
import com.zhilutec.db.daos.AreaDao;
import com.zhilutec.db.entities.AreaEntity;
import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.db.results.AbsenceResult;
import com.zhilutec.services.IAreaService;
import com.zhilutec.services.IPrisonerCfgService;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IRedisStatusService;
import com.zhilutec.services.IWarningStatusService;
import tk.mybatis.mapper.entity.Example;


@Service
public class AreaServiceImpl implements IAreaService {

	@Autowired
	private AreaDao areaDao;
	
	@Resource
	IPrisonerService prisonerService;

	@Resource
	IRedisStatusService redisStatusService;

	@Resource 
	IPrisonerCfgService prisonerCfgService;
	
	@Resource 
	IWarningStatusService warningStatusService;
	
	/*
	 * 查询单个监仓位置信息
	 */
	@Override
	public AreaEntity queryArea(String areaCode) {
		AreaEntity area = new AreaEntity();
		area.setAreaCode(areaCode);
		return areaDao.selectOne(area);
	}

	/*
	 * 查询所有监仓信息
	 */
	@Override
	public List<AreaEntity> queryAreas() {
		Example example = new Example(AreaEntity.class);
		Example.Criteria recordCriteria = example.createCriteria();
		example.orderBy("sort asc");
		recordCriteria.andEqualTo("type", 0).orIsNull("type");
		return areaDao.selectByExample(example);
	}

	/**缺勤人数和缺勤分类统计*/
	private Map<String, Object> statisticsInfo(String areaCode){
		List<PrisonerEntity> prisoners = prisonerService.queryByArea(areaCode);
		Map<String, Object> rsMap = new HashMap<>();
		Map<String, Integer> absences = new HashMap<>();
		Map<String, Integer> checkingIn = new HashMap<>();
		/**缺勤和其它是同一类*/
		int other = 0;
		// 判断当前tagId是否在配置中
		for (PrisonerEntity prisoner : prisoners) {
			String code = prisoner.getCode();
			Long tagId = prisoner.getTagId();
			
			boolean absenceStatus = redisStatusService.isAbsence(code,tagId);
			if(absenceStatus){
			  other++;	
			}
		}
		
		/**考勤人数统计**/
		int total = prisonerService.countArea(areaCode);
		int attendances = total - other;
		checkingIn.put("total", total);
		checkingIn.put("attendances", attendances);
		checkingIn.put("absence", other);
		
       /**将其它类型缺勤的合并到一起**/
		List<AbsenceResult> absenceResults = prisonerCfgService.absence(areaCode);
		for (AbsenceResult absenceResult : absenceResults) {
			if (absenceResult.getType().equals("9")) {
				other = other+absenceResult.getAmount();
			}			
		}		
		
		AbsenceResult absenceResult = new AbsenceResult();
		absenceResult.setType("9");
		absenceResult.setAmount(other);
		absenceResults.add(absenceResult);
		absences = this.absenceMap(absenceResults);
		rsMap.put("checkingIn", checkingIn);
		rsMap.put("abStatistics", absences);
		return rsMap;
	}
	
	@Override
	public Map<String, Object> getStatisticsInfo(String areaCode) throws Exception{
		JSONObject paramJson = new JSONObject();
		 paramJson.put("areaCode", areaCode);
		 Map<String, Object> statistics= this.statisticsInfo(areaCode);
		 Map<String, Long> warnings =warningStatusService.areaWarningsMap(paramJson);
		 statistics.put("warning", warnings);
		 return statistics;
	}
	
		
	@Override
	public Map<String, Integer> absenceMap(List<AbsenceResult> absences) {
		Map<String, Integer> rsMap = new HashMap<>();
		rsMap.put(AbsenceUtil.HOSPITAL, 0);
		rsMap.put(AbsenceUtil.CELL, 0);
		rsMap.put(AbsenceUtil.MEET, 0);
		rsMap.put(AbsenceUtil.OTHER, 0);
		rsMap.put(AbsenceUtil.OUTSIDE, 0);
		rsMap.put(AbsenceUtil.PUNISHMENT, 0);
		rsMap.put(AbsenceUtil.SEE_DOCTOR, 0);
		rsMap.put(AbsenceUtil.TEACHING_BUILDING, 0);
		rsMap.put(AbsenceUtil.SINGLE, 0);
		for (AbsenceResult absenceResult : absences) {
			String type = absenceResult.getType();
			Integer amount = absenceResult.getAmount();
			if (type.equals(AbsenceUtil.HOSPITAL)) {
				rsMap.put(AbsenceUtil.HOSPITAL, amount);
			} else if (type.equals(AbsenceUtil.CELL)) {
				rsMap.put(AbsenceUtil.CELL, amount);
			} else if (type.equals(AbsenceUtil.MEET)) {
				rsMap.put(AbsenceUtil.MEET, amount);
			} else if (type.equals(AbsenceUtil.SINGLE)) {
				rsMap.put(AbsenceUtil.SINGLE, amount);
			} else if (type.equals(AbsenceUtil.OUTSIDE)) {
				rsMap.put(AbsenceUtil.OUTSIDE, amount);
			} else if (type.equals(AbsenceUtil.PUNISHMENT)) {
				rsMap.put(AbsenceUtil.PUNISHMENT, amount);
			} else if (type.equals(AbsenceUtil.SEE_DOCTOR)) {
				rsMap.put(AbsenceUtil.SEE_DOCTOR, amount);
			} else if (type.equals(AbsenceUtil.TEACHING_BUILDING)) {
				rsMap.put(AbsenceUtil.TEACHING_BUILDING, amount);
			} else {
				rsMap.put(AbsenceUtil.OTHER, amount);
			}
		}
		return rsMap;
	}
	
	
}