/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午6:25:29 * 
*/ 
package com.zhilutec.db.daos;


import java.util.HashMap;
import java.util.List;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.db.entities.PrisonerCfgEntity;
import com.zhilutec.db.results.AbsenceResult;
import com.zhilutec.db.results.StatusResult;
public interface PrisonerCfgDao extends MyMapper<PrisonerCfgEntity>,BaseDao<PrisonerCfgEntity> {
	 StatusResult queryStatus(Long tagId);
	 List<AbsenceResult> absenceStatistics(String areaCode);
	 List<HashMap<String,Object>> absenceList(String areaCode);	 
}
