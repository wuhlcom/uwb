/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午6:25:29 * 
*/ 
package com.zhilutec.dbs.daos;


import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.UwbPermission;
import com.zhilutec.dbs.pojos.PermissionRs;


import java.util.Map;

public interface PermissionDao extends MyMapper<UwbPermission>,BaseDao<UwbPermission> {
    PermissionRs getPermissionByUser(Map<String, Object> map);
}
