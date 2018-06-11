/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:48:29 * 
*/ 
package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.pojos.results.PermissionRs;

import java.util.List;

public interface IPermissionService {


    String getUserPermissions(JSONObject jsonObject);

    List<String> getUserMenus(JSONObject jsonObject);
}
