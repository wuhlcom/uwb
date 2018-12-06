/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月24日 下午6:49:05 *
 */
package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.MD5Util;
import com.zhilutec.dbs.daos.UserDao;
import com.zhilutec.dbs.entities.User;
import com.zhilutec.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;

@Service
public class UserServiceImpl implements IUserService {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Override
    public String add(JSONObject jsonObj) {
        int rs = 0;
        User record = new User();
        // 用户名已存在则不添加
        String username = jsonObj.getString("username");
        record.setUsername(username);
        int exist = userDao.selectCount(record);
        if (exist >= 1) {
            return Result.error(ResultCode.REPETITION_ERR.getCode(), "用户名已经存在").toJSONString();
        }

        record = JSON.parseObject(jsonObj.toJSONString(), User.class);
        record.setLocked(0);
        Long createTime = System.currentTimeMillis() / 1000;
        record.setCreatedAt(createTime);
        record.setUpdatedAt(createTime);

        String password = record.getPassword();
        // MD5加密
        record.setPassword(MD5Util.md5EncodeSalt(password, username, null));
        rs = userDao.insertSelective(record);
        if (rs >= 1) {
            return Result.ok(ResultCode.SUCCESS.getCode(), "添加用户成功").toJSONString();
        } else {
            return Result.error(ResultCode.FAILED.getCode(), "添加用户失败").toJSONString();
        }
    }


    private User query(JSONObject jsonObj) {
        String username = jsonObj.getString("username");
        Long userId = jsonObj.getLong("id");
        User user = new User();
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
    public String update(JSONObject jsonObj) {
        User record = new User();
        String username = jsonObj.getString("username");
        String password = jsonObj.getString("password");// 密码为空时不修改密码

        Long id = jsonObj.getLong("id");
        if (username != null) {
            record.setUsername(username);
            int exist = userDao.selectCount(record);
            if (exist > 1) {
                return Result.error(ResultCode.NODATA_ERR.getCode(), "用户不存在").toJSONString();
            }
        } else if (id != null) {
            record.setId(id);
            int exist = userDao.selectCount(record);
            if (exist > 1) {
                return Result.error(ResultCode.NODATA_ERR.getCode(), "用户不存在").toJSONString();
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
                criteria.andEqualTo("id", id);
            } else if (username != null) {
                criteria.andEqualTo("username", username);
            } else {
                return Result.error(ResultCode.FAILED.getCode(), "更新用户时参数错误").toJSONString();
            }

            if (password == null || password.trim() == "" || password.isEmpty()) {
                record.setPassword(null);
            } else {
                // MD5加密
                record.setPassword(MD5Util.md5EncodeSalt(password, username, null));
            }

            Long updateTime = System.currentTimeMillis() / 1000;
            record.setUpdatedAt(updateTime);
            int rs = userDao.updateByExampleSelective(record, example);
            if (rs >= 1) {
                return Result.ok(ResultCode.SUCCESS.getCode(), "更新用户成功").toJSONString();
            } else {
                return Result.error(ResultCode.FAILED.getCode(), "更新用户失败").toJSONString();
            }
        } catch (Exception e) {
            return Result.error(ResultCode.UNKNOW_ERR.getCode(), "更新用户信息出错").toJSONString();
        }
    }

    @Override
    public String delete(JSONObject jsonObj) {
        User record = new User();
        String username = jsonObj.getString("username");
        Long id = jsonObj.getLong("id");

        if (username != null) {
            record.setUsername(username);
        } else if (id != null) {
            record.setId(id);
        }

        // 创建example
        Example example = new Example(User.class);
        // 创建查询条件
        Example.Criteria recordCriteria = example.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        try {
            if (username != null) {
                recordCriteria.andEqualTo("username", username);
            } else if (id != null) {
                recordCriteria.andEqualTo("id", id);
            }
            int rs = userDao.deleteByExample(example);
            if (rs >= 1) {
                return Result.ok(ResultCode.SUCCESS.getCode(), "删除用户成功").toJSONString();
            } else {
                return Result.error(ResultCode.FAILED.getCode(), "删除用户失败").toJSONString();
            }
        } catch (Exception e) {
            return Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除用户出错").toJSONString();
        }
    }

}