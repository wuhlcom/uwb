/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:49:05 * 
*/
package com.zhilutec.services.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.db.daos.BuildingDao;
import com.zhilutec.db.entities.BuildingEntity;

import com.zhilutec.services.IBuildingService;


import tk.mybatis.mapper.entity.Example;

@Service
public class BuildingServiceImpl implements IBuildingService {
	@Autowired
	private BuildingDao buildingDao;

	/*
	 * 查询指定区间楼栋信息
	 */
	@Override
	public List<BuildingEntity> queryByDomain(String domainUniqueCode) {
		Example example = new Example(BuildingEntity.class);	
		example.orderBy("id asc");
		Example.Criteria recordCriteria = example.createCriteria();
		recordCriteria.andEqualTo("domainUniqueCode",domainUniqueCode);
		return buildingDao.selectByExample(example);
	}
	
	/*
	 * 查询指定区间楼栋信息
	 */
	@Override
	public String queryResult(String jsonStr) {
		JSONObject paramJson = JSON.parseObject(jsonStr);
		String domainUniqueCode=paramJson.getString("codeValue");
		List<BuildingEntity> buildings= this.queryByDomain(domainUniqueCode);	
		for (BuildingEntity buildingEntity : buildings) {
			buildingEntity.setId(null);
			buildingEntity.setCode(null);
			buildingEntity.setDomainUniqueCode(null);
			buildingEntity.setRemark(null);
		}
		return Result.ok().put("result", buildings).toJSONString();
	}

}