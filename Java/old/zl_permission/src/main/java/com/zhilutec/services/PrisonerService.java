/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:48:29 * 
*/ 
package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.db.entities.PrisonerEntity;
public interface PrisonerService {

	Result add(JSONObject jsonObj);

	Result update(JSONObject jsonObj);

	Result delete(JSONObject jsonObj);

	PrisonerEntity query(JSONObject jsonObj);

	PrisonerEntity query(String number);

}
