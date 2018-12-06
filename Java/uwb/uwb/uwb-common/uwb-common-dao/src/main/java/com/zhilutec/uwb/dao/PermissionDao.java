/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午6:25:29 * 
*/ 
package com.zhilutec.uwb.dao;


import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.UwbPermission;
import com.zhilutec.uwb.common.pojo.PermissionRs;

import java.util.Map;

public interface PermissionDao extends MyMapper<UwbPermission> {
    PermissionRs getPermissionByUser(Map<String, Object> map);
}
