/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:49:05 * 
*/
package com.zhilutec.services.impl;


import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilutec.db.daos.PrisonerDao;
import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.services.IPrisonerService;

import tk.mybatis.mapper.entity.Example;


@Service
@Transactional
public class PrisonerServiceImpl implements IPrisonerService {

	@Autowired
	private PrisonerDao prisonerDao;

	/*
	 * 查询囚犯
	 */
	@Override
	public PrisonerEntity query(Long tagId) {
		PrisonerEntity prisoner = new PrisonerEntity();
		prisoner.setSex(null);
		prisoner.setIsdel(1);
		prisoner.setTagId(tagId);
		try {
			return prisonerDao.selectOne(prisoner);
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 查询囚犯
	 */
	@Override
	public List<PrisonerEntity> queryAll() {
		return prisonerDao.selectAll();
	}
	
	@Override
	public HashMap<String, Object> queryByTagId(Long tagId){
		return prisonerDao.queryByTagId(tagId);
	}
	
}