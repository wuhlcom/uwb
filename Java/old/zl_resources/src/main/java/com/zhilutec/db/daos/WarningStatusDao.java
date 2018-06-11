package com.zhilutec.db.daos;


import java.util.List;
import java.util.Map;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.db.entities.AreaEntity;
import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.db.results.WarningStatusResult;



public interface WarningStatusDao extends MyMapper<WarningStatusEntity>,BaseDao<AreaEntity>{	
     //查询区域报警统计
	 List<Map<String,Object>> areaWarningStatistics(Map<String, Object> paramsMap);
	 //查询区域报警分类
	 List<WarningStatusResult> areaWarningsList(Map<String, Object> paramsMap);
	 //查询个人报警
	 List<WarningStatusResult> prisonerWarnings(Map<String, Object> paramsMap);
	 //查询区域缺勤列表
	 List<WarningStatusResult> areaAbsenceList(Map<String, Object> paramsMap);
	 //所有报警列表
	 List<WarningStatusResult> warningsList(Map<String, Object> paramsMap);	 
}
