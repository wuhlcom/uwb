/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:48:29 * 
*/ 
package com.zhilutec.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhilutec.db.entities.PrisonerEntity;

public interface IPrisonerService {

	PrisonerEntity query(Long tagId);

    /*
* 查询囚犯
*/
    List<PrisonerEntity> queryAll();

    HashMap<String, Object> queryByTagId(Long tagId);

	List<PrisonerEntity> queryByArea(String areaCode);

	Map<String, Integer> checkingIn(String areaCode);

	Integer countArea(String areaCode);


}
