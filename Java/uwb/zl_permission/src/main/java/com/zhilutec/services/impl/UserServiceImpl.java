/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月24日 下午6:49:05 *
 */
package com.zhilutec.services.impl;

import com.zhilutec.dbs.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.MD5Util;
import com.zhilutec.dbs.daos.UserDao;
import com.zhilutec.services.IUserService;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements IUserService {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Override
    public Result add(JSONObject jsonObj) {
        int rs = 0;
        User record = new User();
        // 用户名已存在则不添加
        String username = jsonObj.getString("username");
        record.setUsername(username);
        record.setSex(null);
        record.setIsdel(1);
        int exist = userDao.selectCount(record);
        if (exist >= 1) {
            return Result.error(ResultCode.REPETITION_ERR.getCode(), "用户名已经存在");
        }

        // 邮箱已存在则不添加
        record.setUsername(null);
        String email = jsonObj.getString("email");
        if (email != null && !email.isEmpty()) {
            record.setEmail(email);
            exist = userDao.selectCount(record);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "邮箱已经存在");
            }
        }

        // 电话号码已存在则不添加
        record.setEmail(null);
        String tel = jsonObj.getString("telephone");
        if (tel != null && !tel.isEmpty()) {
            record.setTelephone(tel);
            exist = userDao.selectCount(record);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "电话号码已经存在");
            }
        }

        // 身份证已存在则不添加
        record.setTelephone(null);
        String idcard = jsonObj.getString("idcard");
        if (idcard != null && !idcard.isEmpty()) {
            record.setIdcard(idcard);
            exist = userDao.selectCount(record);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "身份证已经存在");
            }
        }

        record = JSON.parseObject(jsonObj.toJSONString(), User.class);
        record.setIsdel(1);
        record.setCreatedAt(System.currentTimeMillis() / 1000);


        String password = record.getPassword();
        // MD5加密
        // record.setPassword(MD5Util.md5Encode(password));
        record.setPassword(MD5Util.md5EncodeSalt(password, username, null));
        rs = userDao.insertSelective(record);
        if (rs >= 1) {
            return Result.ok(ResultCode.SUCCESS.getCode(), "添加用户成功");
        } else {
            return Result.error(ResultCode.FAILED.getCode(), "添加用户失败");
        }
    }

    @Override
    public User query(JSONObject jsonObj) {
        String username = jsonObj.getString("username");
        Long userId = jsonObj.getLong("id");
        User user = new User();
        user.setSex(null);
        user.setIsdel(1);

        if (username != null && !username.trim().isEmpty()) {
            user.setUsername(username);
        } else if (userId != null) {
            user.setId(userId);
        } else {
            logger.info("查询用户时参数错误!");
            return null;
        }

        try {
            return userDao.selectOne(user);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public User getUser(JSONObject jsonObject) {
        return userDao.getUserByName(jsonObject);
    }

    @Override
    public User query(String username) {
        User user = new User();
        user.setSex(null);
        user.setIsdel(1);

        if (username != null && !username.trim().isEmpty()) {
            user.setUsername(username);
        } else {
            logger.info("查询用户时参数错误!");
            return null;
        }
        try {
            return userDao.selectOne(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Result update(JSONObject jsonObj) {
        User record = new User();
        record.setSex(null);
        record.setAvailable(null);
        Long parent_user = jsonObj.getLong("parent_user");
        String username = jsonObj.getString("username");
        String password = jsonObj.getString("password");// 密码为空时不修改密码

        Long id = jsonObj.getLong("id");
        if (username != null) {
            record.setUsername(username);
            record.setIsdel(1);
            int exist = userDao.selectCount(record);
            if (exist > 1) {
                return Result.error(ResultCode.NODATA_ERR.getCode(), "用户不存在");
            }
        } else if (id != null) {
            record.setId(id);
            record.setIsdel(1);
            int exist = userDao.selectCount(record);
            if (exist > 1) {
                return Result.error(ResultCode.NODATA_ERR.getCode(), "用户不存在");
            }
        }

        String email = jsonObj.getString("email");
        Example paramsExample = new Example(User.class);
        Example.Criteria paramsCriteria = paramsExample.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        if (email != null && !email.trim().isEmpty()) {
            paramsCriteria.andNotEqualTo("id", id).andEqualTo("isdel", 1).andEqualTo("email", email);
            int exist = userDao.selectCountByExample(paramsExample);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "邮箱已经存在");
            }
        }

        String telephone = jsonObj.getString("telephone");
        if (telephone != null && !telephone.trim().isEmpty()) {
            Example paramsExample1 = new Example(User.class);
            Example.Criteria paramsCriteria1 = paramsExample1.createCriteria();
            paramsCriteria1.andNotEqualTo("id", id).andEqualTo("isdel", 0).andEqualTo("telephone", telephone);
            int exist = userDao.selectCountByExample(paramsExample);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "电话已经存在");
            }
        }

        String idcard = jsonObj.getString("idcard");
        if (idcard != null && !idcard.trim().isEmpty()) {
            Example paramsExample2 = new Example(User.class);
            Example.Criteria paramsCriteria2 = paramsExample2.createCriteria();
            paramsCriteria2 = paramsExample2.createCriteria();
            paramsCriteria2.andNotEqualTo("id", id).andEqualTo("isdel", 1).andEqualTo("idcard", idcard);
            int exist = userDao.selectCountByExample(paramsExample2);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "身份证已经存在");
            }
        }

        record = JSON.parseObject(jsonObj.toJSONString(), User.class);
        try {
            // 创建example
            Example example = new Example(User.class);
            // 创建查询条件
            Example.Criteria criteria = example.createCriteria();

            // 设置查询条件 多个andEqualTo串联表示 and条件查询
            if (id != null) {
                criteria.andEqualTo("id", id).andEqualTo("isdel", 1).andEqualTo("parent_user", parent_user);
            } else if (username != null) {
                criteria.andEqualTo("username", username).andEqualTo("isdel", 1).andEqualTo("parent_user", parent_user);
            } else {
                return Result.error(ResultCode.FAILED.getCode(), "更新用户时参数错误");
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
                return Result.error(ResultCode.FAILED.getCode(), "更新用户失败");
            }
        } catch (Exception e) {
            return Result.error(ResultCode.UNKNOW_ERR.getCode(), "更新用户信息出错");
        }
    }

    @Override
    public Result delete(JSONObject jsonObj) {
        User record = new User();
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
        Example example = new Example(User.class);
        // 创建查询条件
        Example.Criteria recordCriteria = example.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        try {
            if (username != null) {
                recordCriteria.andEqualTo("username", username).andEqualTo("parent_user", parent_user)
                        .andEqualTo("isdel", 1);
            } else if (id != null) {
                recordCriteria.andEqualTo("id", id).andEqualTo("parent_user", parent_user).andEqualTo("isdel", 1);
            }
            int rs = userDao.updateByExampleSelective(record, example);
            if (rs >= 1) {
                return Result.ok(ResultCode.SUCCESS.getCode(), "删除用户成功");
            } else {
                return Result.error(ResultCode.FAILED.getCode(), "删除用户失败");
            }
        } catch (Exception e) {
            return Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除用户出错");
        }
    }

}