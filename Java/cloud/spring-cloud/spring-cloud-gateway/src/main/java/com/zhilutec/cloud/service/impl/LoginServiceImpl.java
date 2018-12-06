package com.zhilutec.cloud.service.impl;


import com.alibaba.fastjson.JSONObject;

import com.zhilutec.cloud.util.MD5Util;
import com.zhilutec.cloud.util.result.Result;
import com.zhilutec.cloud.util.result.ResultCode;
import com.zhilutec.cloud.common.utils.JWTUtil;

import com.zhilutec.cloud.entity.User;
import com.zhilutec.cloud.service.ILoginService;
import com.zhilutec.cloud.service.IRedisService;
import com.zhilutec.cloud.service.IUserService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    IUserService userService;

    @Resource
    IRedisService redisService;

    private static final String ROLE_Id = "ZL_GW_TEMP_ROLE";

    @Override
    public String login(JSONObject requestJson) {
        Map<String, Object> loginMap = new HashMap<String, Object>();

        String username = requestJson.getString("username").trim();
        String password = requestJson.getString("password").trim();

        User user = userService.getUser(requestJson);
        if (user == null) {
            return Result.error(ResultCode.NODATA_ERR.getCode(), "用户名或密码错误!").toJSONString();
        }

        // 前端页面密文解码
        // password = new String(MD5Util.decodeSimpleMd5(password));

        //密码加盐
        password = MD5Util.md5EncodeSalt(password, username, null);

        // 验证密码是否有效
        boolean iscorrect = password.equals(user.getPassword());
        if (iscorrect) {
            try {
                String userId = String.valueOf(user.getId());
                // String roleId = Long.toString(user.getRid());
                String roleId = ROLE_Id;

                //完整token
                String token = JWTUtil.createJWT(username, userId, roleId, null, null, null,
                        null);
                String tokenFinger = token;
                //截取完整token中的一部分进行md5运算
                String md5Token = MD5Util.string2MD5(tokenFinger);
                //md5Token作为key
                String key = JWTUtil.ACCOUNT_PRE + md5Token;

                //accountkey
                String accountkey = JWTUtil.ACCOUNT_PRE + username;

                //判断是否已经登录,若已登录删除旧的token,保证缓存中只记录一条有效的token
                String loginedToken = this.getLoginToken(accountkey);
                if (loginedToken != null) {
                    this.delTockenCache(loginedToken);
                }

                //redis中添加key-token字段和超时时间,缓存token用于后续token校验
                redisService.add(key, token, JWTUtil.REDIS_TTL, TimeUnit.SECONDS);
                //redis中直接缓存用户名和token key用来判断用户是否登录过
                redisService.add(accountkey, key, JWTUtil.REDIS_TTL, TimeUnit.SECONDS);

                loginMap.put("username", username);
                loginMap.put("token", tokenFinger);
                loginMap.put("roleId", roleId);
                return Result.ok(ResultCode.SUCCESS.getCode(), loginMap).toJSONString();
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("用户登录失败").toJSONString();
            }
        } else {
            return Result.error("用户名或密码错误").toJSONString();
        }

    }


    @Override
    public String loginStatus(JSONObject requestJson) {
        String token = requestJson.getString("token");
        return loginStatus(token);
    }


    @Override
    public String loginStatus(String token) {
        Map<String, Object> isloginMap = new HashMap<String, Object>();

        String md5Token = MD5Util.string2MD5(token);
        String key = JWTUtil.ACCOUNT_PRE + md5Token;

        String jwtString = String.valueOf(redisService.get(key));

        if (jwtString == null || jwtString == "null" || jwtString.length() <= 0) {
            return Result.error("用户未登录系统").toJSONString();
        } else {
            //缓存和数据库都有这个用户才返回用户登录正登录,防止缓存中有而数据库没有此用户或其它原因导致缓存与数据库不一致仍返回登录成功
            Claims claims = JWTUtil.parseJWT(jwtString, null);
            String username = String.valueOf(claims.get("username"));
            String userId = String.valueOf(claims.get("userId"));
            String roleId = String.valueOf(claims.get("roleId"));
            User user = userService.query(username.trim());
            if (null == user) {
                return Result.error("用户名或密码错误").toJSONString();
            } else {
                isloginMap.put("username", username);
                isloginMap.put("userId", userId);
                isloginMap.put("roleId", roleId);
                return Result.ok(ResultCode.SUCCESS.getCode(), isloginMap).toJSONString();
            }
        }
    }

    private String getLoginToken(String key) {
        String token = null;
        boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            token = (String) redisService.get(key);
        }
        return token;
    }

    private void delTockenCache(String md5TokenKey) {
        String token = String.valueOf(redisService.get(md5TokenKey));
        if (token == null || token == "null" || token.length() <= 0) {
        } else {
            Claims claims = JWTUtil.parseJWT(token, null);
            String username = String.valueOf(claims.get("username"));
            //删除字符串类型缓存
            redisService.delete(md5TokenKey);
            //删除账户缓存
            String accountKey = JWTUtil.ACCOUNT_PRE + username;
            redisService.delete(accountKey);
        }
    }

    @Override
    public String logout(JSONObject jsonObject) {
        String token = jsonObject.getString("token");
        String md5Token = MD5Util.string2MD5(token);
        String key = JWTUtil.ACCOUNT_PRE + md5Token;
        /**当找不到key时返回是'null'字符串**/
        String jwtString = String.valueOf(redisService.get(key));

        if (jwtString != null && jwtString.equals("null")) {
            jwtString = null;
        }

        if (jwtString != null && jwtString.length() > 0) {
            Claims claims = JWTUtil.parseJWT(jwtString, null);
            String username = String.valueOf(claims.get("username"));
            //删除字符串类型缓存
            redisService.delete(key);
            //删除账户缓存
            String accountKey = JWTUtil.ACCOUNT_PRE + username;
            redisService.delete(accountKey);
            //重新查询
            jwtString = String.valueOf(redisService.get(key));
        }

        if (jwtString != null && jwtString.equals("null")) {
            jwtString = null;
        }

        if (jwtString == null || jwtString.length() == 0) {
            Map logoutMap = new HashMap();
            logoutMap.put("errcode", ResultCode.SUCCESS.getCode());
            logoutMap.put("result", "success");
            return Result.ok(logoutMap).toJSONString();
        } else {
            return Result.error("用户退出失败").toJSONString();
        }

    }

}
