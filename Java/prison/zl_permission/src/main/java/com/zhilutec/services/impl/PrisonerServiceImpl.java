/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月24日 下午6:49:05 * 
*/
package com.zhilutec.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.MD5Util;
import com.zhilutec.db.daos.PrisonerDao;
import com.zhilutec.db.daos.ResourceDao;
import com.zhilutec.db.daos.UserDao;
import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.db.entities.UserEntity;
import com.zhilutec.services.PrisonerService;
import com.zhilutec.services.UserService;

import tk.mybatis.mapper.entity.Example;

@Service
public class PrisonerServiceImpl implements PrisonerService {

	@Autowired
	private PrisonerDao prisonerDao;

	@Override
	public Result add(JSONObject jsonObj) {
		PrisonerEntity record = new PrisonerEntity();		
		record.setSex(null);	

		// 囚徒编码已存在则不添加
		record.setNumber(jsonObj.getString("number"));
		record.setIsdel(0);
		int exist = prisonerDao.selectCount(record);
		if (exist >= 1) {
			return Result.error(ResultCode.REPETITION_ERR.getCode(), "囚徒编码已经存在");
		}

		// 身份证已存在则不添加
		String idcard = jsonObj.getString("idcard");
		if (idcard != null && !idcard.isEmpty()) {
			record.setNumber(null);	
			record.setIdcard(idcard);
			record.setIsdel(0);
			exist = prisonerDao.selectCount(record);
			if (exist >= 1) {
				return Result.error(ResultCode.REPETITION_ERR.getCode(), "身份证已经存在");
			}
		}

		record = JSON.parseObject(jsonObj.toJSONString(), PrisonerEntity.class);
		record.setIsdel(0);
		record.setCreated_at(System.currentTimeMillis() / 1000);

		// MD5加密		
		int rs = prisonerDao.insertSelective(record);
		if (rs >= 1) {
			return Result.ok(ResultCode.SUCCESS.getCode(), "添加囚徒成功");
		} else {
			return Result.error(ResultCode.Failed.getCode(), "添加囚徒失败");
		}
	}

	@Override
	public PrisonerEntity query(JSONObject jsonObj) {
		String number = jsonObj.getString("number");
		Long prisonerId = jsonObj.getLong("id");
		PrisonerEntity prisoner = new PrisonerEntity();
		prisoner.setSex(null);
		prisoner.setIsdel(0);

		if (number != null && !number.trim().isEmpty()) {
			prisoner.setNumber(number);
		} else if (prisonerId != null) {
			prisoner.setId(prisonerId);
		} else {
			System.out.println("参数错误!");
			return null;
		}

		try {
			return prisonerDao.selectOne(prisoner);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public PrisonerEntity query(String number) {
		PrisonerEntity prisoner = new PrisonerEntity();
		prisoner.setSex(null);
		prisoner.setIsdel(0);

		if (number != null && !number.trim().isEmpty()) {
			prisoner.setNumber(number);
		} else {
			System.out.println("参数错误!");
			return null;
		}

		try {
			return prisonerDao.selectOne(prisoner);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Result update(JSONObject jsonObj) {
		PrisonerEntity record = new PrisonerEntity();
		record.setSex(null);	
		
		String number = jsonObj.getString("number");
		Long prisonerId = jsonObj.getLong("id");
	     if (prisonerId != null) {
			record.setId(prisonerId);
			record.setIsdel(0);
			int exist = prisonerDao.selectCount(record);
			if (exist > 1) {
				return Result.error(ResultCode.NODATA_ERR.getCode(), "囚徒不存在");
			}
		}

		Example paramsExample = new Example(PrisonerEntity.class);
		Example.Criteria paramsCriteria = paramsExample.createCriteria();
		// 设置查询条件 多个andEqualTo串联表示 and条件查询
		if (number != null && !number.trim().isEmpty()) {
			paramsCriteria.andNotEqualTo("id", prisonerId).andEqualTo("isdel", 0).andEqualTo("number", number);
			int exist = prisonerDao.selectCountByExample(paramsExample);
			if (exist >= 1) {
				return Result.error(ResultCode.REPETITION_ERR.getCode(), "此编号已存在");
			}
		}


		String idcard = jsonObj.getString("idcard");
		if (idcard != null && !idcard.trim().isEmpty()) {
			Example paramsExample2 = new Example(PrisonerEntity.class);
			Example.Criteria paramsCriteria2 = paramsExample2.createCriteria();
			paramsCriteria2 = paramsExample2.createCriteria();
			paramsCriteria2.andNotEqualTo("id", prisonerId).andEqualTo("isdel", 0).andEqualTo("idcard", idcard);
			int exist = prisonerDao.selectCountByExample(paramsExample2);
			if (exist >= 1) {
				return Result.error(ResultCode.REPETITION_ERR.getCode(), "身份证已经存在");
			}
		}

		record = JSON.parseObject(jsonObj.toJSONString(), PrisonerEntity.class);
		try {
			// 创建example
			Example example = new Example(PrisonerEntity.class);
			// 创建查询条件
			Example.Criteria criteria = example.createCriteria();

			// 设置查询条件 多个andEqualTo串联表示 and条件查询
			if (prisonerId != null) {
				criteria.andEqualTo("id", prisonerId).andEqualTo("isdel", 0);
			} else {
				return Result.error(ResultCode.Failed.getCode(), "更新囚徒时参数错误");
			}
		
			int rs = prisonerDao.updateByExampleSelective(record, example);
			if (rs >= 1) {
				return Result.ok(ResultCode.SUCCESS.getCode(), "更新囚徒成功");
			} else {
				return Result.error(ResultCode.Failed.getCode(), "更新囚徒失败");
			}
		} catch (Exception e) {
			return Result.error(ResultCode.UNKNOW_ERR.getCode(), "更新囚徒信息出错");
		}
	}
	
	@Override
	public Result delete (JSONObject jsonObj){
		PrisonerEntity record = new PrisonerEntity();
		record.setSex(null);
		String number = jsonObj.getString("number");
		Long prisonerId = jsonObj.getLong("id");

		if (number != null) {
			record.setNumber(number);
			record.setIsdel(1);
		} else if (prisonerId != null) {
			record.setId(prisonerId);
			record.setIsdel(1);
		}

		// 创建example
		Example example = new Example(PrisonerEntity.class);
		// 创建查询条件
		Example.Criteria recordCriteria = example.createCriteria();
		// 设置查询条件 多个andEqualTo串联表示 and条件查询
		try {
			if (number != null) {
				recordCriteria.andEqualTo("number", number).andEqualTo("isdel", 0);
			} else if (prisonerId != null) {
				recordCriteria.andEqualTo("id", prisonerId).andEqualTo("isdel", 0);
			}
			int rs = prisonerDao.updateByExampleSelective(record, example);
			if (rs >= 1) {
				return Result.ok(ResultCode.SUCCESS.getCode(), "删除囚徒信息成功");
			} else {
				return Result.error(ResultCode.Failed.getCode(), "删除囚徒信息失败");
			}
		} catch (Exception e) {
			return Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除囚徒信息出错");
		}
	}

}