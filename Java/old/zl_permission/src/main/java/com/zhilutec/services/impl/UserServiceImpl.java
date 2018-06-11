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
import com.zhilutec.common.utils.MD5Util;
import com.zhilutec.db.daos.UserDao;
import com.zhilutec.db.entities.UserEntity;
import com.zhilutec.services.UserService;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public Result add(JSONObject jsonObj) {
		int rs = 0;
		UserEntity record = new UserEntity();
		// 用户名已存在则不添加
		String username = jsonObj.getString("username");
		record.setUsername(username);
		record.setSex(null);
		record.setIsdel(0);
		int exist = userDao.selectCount(record);
		if (exist >= 1) {
			return Result.error(ResultCode.REPETITION_ERR.getCode(), "用户名已经存在");
		}

		// 邮箱已存在则不添加
		record.setUsername(null);
		record.setEmail(jsonObj.getString("email"));
		record.setIsdel(0);
		exist = userDao.selectCount(record);
		if (exist >= 1) {
			return Result.error(ResultCode.REPETITION_ERR.getCode(), "邮箱已经存在");
		}

		// 电话号码已存在则不添加
		String tel = jsonObj.getString("telephone");
		if (tel != null && !tel.isEmpty()) {
			record.setUsername(null);
			record.setTelephone(tel);
			record.setIsdel(0);
			exist = userDao.selectCount(record);
			if (exist >= 1) {
				return Result.error(ResultCode.REPETITION_ERR.getCode(), "电话号码已经存在");
			}
		}

		// 身份证已存在则不添加
		String idcard = jsonObj.getString("idcard");
		if (idcard != null && !idcard.isEmpty()) {
			record.setUsername(null);
			record.setTelephone(null);
			record.setIdcard(idcard);
			record.setIsdel(0);
			exist = userDao.selectCount(record);
			if (exist >= 1) {
				return Result.error(ResultCode.REPETITION_ERR.getCode(), "身份证已经存在");
			}
		}

		record = JSON.parseObject(jsonObj.toJSONString(), UserEntity.class);
		record.setIsdel(0);
		record.setCreated_at(System.currentTimeMillis() / 1000);

		// MD5加密
		String password = record.getPassword();
		password = MD5Util.md5Encode(password);
		record.setPassword(password);
		rs = userDao.insertSelective(record);
		if (rs >= 1) {
			return Result.ok(ResultCode.SUCCESS.getCode(), "添加用户成功");
		} else {
			return Result.error(ResultCode.Failed.getCode(), "添加用户失败");
		}
	}

	@Override
	public UserEntity query(JSONObject jsonObj) {
		String username = jsonObj.getString("username");
		Long userId = jsonObj.getLong("id");
		UserEntity user = new UserEntity();
		user.setSex(null);
		user.setIsdel(0);

		if (username != null && !username.trim().isEmpty()) {
			user.setUsername(username);
		} else if (userId != null) {
			user.setId(userId);
		} else {
			System.out.println("参数错误!");
			return null;
		}

		try {
			return userDao.selectOne(user);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public UserEntity query(String username) {
		UserEntity user = new UserEntity();
		user.setSex(null);
		user.setIsdel(0);

		if (username != null && !username.trim().isEmpty()) {
			user.setUsername(username);
		} else {
			System.out.println("参数错误!");
			return null;
		}

		try {		
			user = userDao.selectOne(user);		
			return userDao.selectOne(user);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Result update(JSONObject jsonObj) {
		UserEntity record = new UserEntity();
		record.setSex(null);
		record.setAvailable(null);
		Long parent_user = jsonObj.getLong("parent_user");
		String username = jsonObj.getString("username");
		String password = jsonObj.getString("password");// 密码为空时不修改密码

		Long id = jsonObj.getLong("id");
		if (username != null) {
			record.setUsername(username);
			record.setIsdel(0);
			int exist = userDao.selectCount(record);
			if (exist > 1) {
				return Result.error(ResultCode.NODATA_ERR.getCode(), "用户不存在");
			}
		} else if (id != null) {
			record.setId(id);
			record.setIsdel(0);
			int exist = userDao.selectCount(record);
			if (exist > 1) {
				return Result.error(ResultCode.NODATA_ERR.getCode(), "用户不存在");
			}
		}

		String email = jsonObj.getString("email");
		Example paramsExample = new Example(UserEntity.class);
		Example.Criteria paramsCriteria = paramsExample.createCriteria();
		// 设置查询条件 多个andEqualTo串联表示 and条件查询
		if (email != null && !email.trim().isEmpty()) {
			paramsCriteria.andNotEqualTo("id", id).andEqualTo("isdel", 0).andEqualTo("email", email);
			int exist = userDao.selectCountByExample(paramsExample);
			if (exist >= 1) {
				return Result.error(ResultCode.REPETITION_ERR.getCode(), "邮箱已经存在");
			}
		}

		String telephone = jsonObj.getString("telephone");
		if (telephone != null && !telephone.trim().isEmpty()) {
			Example paramsExample1 = new Example(UserEntity.class);
			Example.Criteria paramsCriteria1 = paramsExample1.createCriteria();
			paramsCriteria1.andNotEqualTo("id", id).andEqualTo("isdel", 0).andEqualTo("telephone", telephone);
			int exist = userDao.selectCountByExample(paramsExample);
			if (exist >= 1) {
				return Result.error(ResultCode.REPETITION_ERR.getCode(), "电话已经存在");
			}
		}

		String idcard = jsonObj.getString("idcard");
		if (idcard != null && !idcard.trim().isEmpty()) {
			Example paramsExample2 = new Example(UserEntity.class);
			Example.Criteria paramsCriteria2 = paramsExample2.createCriteria();
			paramsCriteria2 = paramsExample2.createCriteria();
			paramsCriteria2.andNotEqualTo("id", id).andEqualTo("isdel", 0).andEqualTo("idcard", idcard);
			int exist = userDao.selectCountByExample(paramsExample2);
			if (exist >= 1) {
				return Result.error(ResultCode.REPETITION_ERR.getCode(), "身份证已经存在");
			}
		}

		record = JSON.parseObject(jsonObj.toJSONString(), UserEntity.class);
		try {
			// 创建example
			Example example = new Example(UserEntity.class);
			// 创建查询条件
			Example.Criteria criteria = example.createCriteria();

			// 设置查询条件 多个andEqualTo串联表示 and条件查询
			if (id != null) {
				criteria.andEqualTo("id", id).andEqualTo("isdel", 0).andEqualTo("parent_user", parent_user);
			} else if (username != null) {
				criteria.andEqualTo("username", username).andEqualTo("isdel", 0).andEqualTo("parent_user", parent_user);
			} else {
				return Result.error(ResultCode.Failed.getCode(), "更新用户时参数错误");
			}

			if (password == null || password.trim() == "" || password.isEmpty()) {
				record.setPassword(null);
			} else {
				// MD5加密
				password = MD5Util.md5Encode(password);
				record.setPassword(password);
			}

			int rs = userDao.updateByExampleSelective(record, example);
			if (rs >= 1) {
				return Result.ok(ResultCode.SUCCESS.getCode(), "更新用户成功");
			} else {
				return Result.error(ResultCode.Failed.getCode(), "更新用户失败");
			}
		} catch (Exception e) {
			return Result.error(ResultCode.UNKNOW_ERR.getCode(), "更新用户信息出错");
		}
	}
	
	@Override
	public Result delete (JSONObject jsonObj){
		UserEntity record = new UserEntity();
		record.setSex(null);
		record.setAvailable(null);
		String username = jsonObj.getString("username");
		Long parent_user = jsonObj.getLong("parent_user");
		Long id = jsonObj.getLong("id");

		if (username != null) {
			record.setUsername(username);
			record.setIsdel(1);
		} else if (id != null) {
			record.setId(id);
			record.setIsdel(1);
		}

		// 创建example
		Example example = new Example(UserEntity.class);
		// 创建查询条件
		Example.Criteria recordCriteria = example.createCriteria();
		// 设置查询条件 多个andEqualTo串联表示 and条件查询
		try {
			if (username != null) {
				recordCriteria.andEqualTo("username", username).andEqualTo("parent_user", parent_user)
						.andEqualTo("isdel", 0);
			} else if (id != null) {
				recordCriteria.andEqualTo("id", id).andEqualTo("parent_user", parent_user).andEqualTo("isdel", 0);
			}
			int rs = userDao.updateByExampleSelective(record, example);
			if (rs >= 1) {
				return Result.ok(ResultCode.SUCCESS.getCode(), "删除用户成功");
			} else {
				return Result.error(ResultCode.Failed.getCode(), "删除用户失败");
			}
		} catch (Exception e) {
			return Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除用户出错");
		}
	}

}