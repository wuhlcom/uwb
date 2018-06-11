package com.zhilutec.services.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhilutec.db.daos.CoordinateDao;
import com.zhilutec.db.results.PointResult;
import com.zhilutec.services.ICoordinateService;

@Service
@Transactional
public class CoordinateServiceImpl implements ICoordinateService {
	
	@Autowired
	private CoordinateDao coordinateDao;
	
	/**
	 *查询一组坐标 ***/	
 //	select * from pr_coordinates where id in(select max(id) from pr_coordinates  where tag_id in(1,100)group by tag_id);


	/*单个监仓的最后一次坐标*/
	@Override
	public List<PointResult> queryPoints(String jsonStr) throws Exception {
		Map<String, Object> paramsMap = JSON.parseObject(jsonStr);
		HashMap<String, Object> paramsHs = new HashMap<>();
		for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			paramsHs.put(entry.getKey(), entry.getValue());
		}	
		return coordinateDao.queryPoints(paramsHs);
	}
	
//	@Override
//	public List<Map> queryPoints(Map mapStr) throws Exception {			
//		return coordinateDao.queryPoints(mapStr);
//	}
	
}