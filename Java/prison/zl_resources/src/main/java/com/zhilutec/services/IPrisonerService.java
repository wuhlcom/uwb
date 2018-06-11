/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:48:29 * 
*/ 
package com.zhilutec.services;


import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.db.entities.PrisonerEntity;

public interface IPrisonerService {

	public String add(JSONObject jsonObj);

	Result update(JSONObject jsonObj);

	Result delete(JSONObject jsonObj);

	PrisonerEntity query(JSONObject jsonObj);

	PrisonerEntity query(String code);

	List<PrisonerEntity> queryByArea(String areaCode);

	Integer countArea(String jsonStr);

	Map<String, Integer> checkingIn(String jsonStr);

	PrisonerEntity queryByTagId(Long tagId);

	String qureyResult(String code);

	String qureyResult(Long tagId);

	PrisonerEntity getPrisonerByCode(String code);

	String queryByAreaResult(String areaCode);

	String getPrisonerInfo(String jsonStr);

	String getAttendanceStatistics(String areaCode);

    List<PrisonerEntity> getPrisoners(String areaCode);

    List<PrisonerEntity> queryByAreaList(String areaCode);

}
