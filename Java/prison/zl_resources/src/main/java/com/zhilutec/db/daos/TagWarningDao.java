package com.zhilutec.db.daos;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhilutec.common.mappers.*;
import com.zhilutec.db.entities.TagWarningEntity;
import com.zhilutec.db.results.WarningStatusResult;

public interface TagWarningDao extends MyMapper<TagWarningEntity>,BaseDao<TagWarningEntity> {
	//查询报警统计
	 List<Map<String,Object>> queryAreaWarnings(HashMap<String, Object> paramsMap);
	 //查询报警列表
	 List<WarningStatusResult> areaWarningsList(Map<String, Object> paramsMap);
}
