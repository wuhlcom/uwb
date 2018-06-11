/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:49:05 * 
*/
package com.zhilutec.services.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhilutec.common.result.Result;
import com.zhilutec.db.daos.DomainDao;
import com.zhilutec.db.entities.DomainEntity;
import com.zhilutec.services.IDomainService;

import tk.mybatis.mapper.entity.Example;

@Service
public class DomainServiceImpl implements IDomainService {
	@Autowired
	private DomainDao domainDao;

	/*
	 * 查询所有大区间信息
	 */
	@Override
	public List<DomainEntity> query() {
		Example example = new Example(DomainEntity.class);	
		example.orderBy("id asc");
		return domainDao.selectByExample(example);
	}
	
	/*
	 * 查询所有大区间信息
	 */
	@Override
	public String queryResult() {
		List<DomainEntity> domains= this.query();	
		for (DomainEntity domainEntity : domains) {
			domainEntity.setId(null);
			domainEntity.setRemark(null);
		}
		return Result.ok().put("result", domains).toJSONString();
	}

}