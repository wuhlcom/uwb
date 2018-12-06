/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:48:29 * 
*/ 
package com.zhilutec.uwb.service;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.entity.User;
import com.zhilutec.uwb.result.Result;


public interface IUserService {

	Result add(JSONObject jsonObj);

	User query(JSONObject jsonObj);

    User getUser(JSONObject jsonObject);

    User query(String username);

	Result update(JSONObject jsonObj);

	Result delete(JSONObject jsonObj);

}
