/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:48:29 * 
*/ 
package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.db.entities.UserEntity;

public interface UserService {

	Result add(JSONObject jsonObj);

	UserEntity query(JSONObject jsonObj);

	UserEntity query(String username);

	Result update(JSONObject jsonObj);

	Result delete(JSONObject jsonObj);

}
