package com.zhilutec.db.daos;

import java.util.HashMap;
import java.util.List;


import com.zhilutec.common.mappers.*;
import com.zhilutec.db.entities.CoordinateEntity;
import com.zhilutec.db.results.PointResult;
public interface CoordinateDao extends MyMapper<CoordinateEntity>,BaseDao<CoordinateEntity> {
	
	 List<PointResult> queryPoints(HashMap<String, Object> paramsMap);	
}
