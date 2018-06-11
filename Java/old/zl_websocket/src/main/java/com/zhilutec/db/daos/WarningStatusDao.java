package com.zhilutec.db.daos;


import java.util.List;
import java.util.Map;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.db.entities.AreaEntity;
import com.zhilutec.db.entities.WarningStatusEntity;


public interface WarningStatusDao extends MyMapper<WarningStatusEntity>,BaseDao<AreaEntity>{	
     //查询区域报警统计
	 List<Map<String,Object>> areaWarningStatistics(Map<String, Object> paramsMap);
	
}
