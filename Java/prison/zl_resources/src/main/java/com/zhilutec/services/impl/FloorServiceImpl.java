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
import com.zhilutec.db.daos.FloorDao;
import com.zhilutec.db.entities.FloorEntity;
import com.zhilutec.services.IFloorService;

import tk.mybatis.mapper.entity.Example;

@Service
public class FloorServiceImpl implements IFloorService {
	@Autowired
	private FloorDao floorDao;

	/*
	 * 查询楼房的楼层信息
	 */
	@Override
	public List<FloorEntity> queryByBuilding(String buildingUniqueCode) {
		Example example = new Example(FloorEntity.class);	
		example.orderBy("id asc");
		Example.Criteria recordCriteria = example.createCriteria();
		recordCriteria.andEqualTo("buildingUniqueCode",buildingUniqueCode);
		return floorDao.selectByExample(example);
	}
	
	/*
	 * 查询指定区间楼栋信息
	 */
	@Override
	public String queryResult(String jsonStr) {
		JSONObject paramJson = JSON.parseObject(jsonStr);
		String buildingUniqueCode=paramJson.getString("codeValue");
		List<FloorEntity> floors= this.queryByBuilding(buildingUniqueCode);	
		for (FloorEntity floorEntity : floors) {
			floorEntity.setId(null);
			floorEntity.setCode(null);
			floorEntity.setRemark(null);
			floorEntity.setBuildingUniqueCode(null);
		}
		return Result.ok().put("result", floors).toJSONString();
	}

}