/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午6:25:29 * 
*/ 
package com.zhilutec.db.daos;


import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.db.results.PrisonerResult;

public interface PrisonerDao extends MyMapper<PrisonerEntity>,BaseDao<PrisonerEntity> {
	 PrisonerResult queryByCode(String code);
}
