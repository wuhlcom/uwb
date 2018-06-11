package com.zhilutec.services.impl;


import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.JWTUtil;
import com.zhilutec.common.utils.MD5Util;
import com.zhilutec.dbs.entities.User;
import com.zhilutec.services.ILoginService;
import com.zhilutec.services.IPermissionService;

import com.zhilutec.services.IUserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    IUserService userService;

    @Resource
    IPermissionService permissionService;

    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String login(HttpServletRequest request, JSONObject requestJson) {
        Map<String, Object> loginMap = new HashMap<String, Object>();

        String finger = request.getHeader("finger");
        String username = requestJson.getString("username").trim();
        String password = requestJson.getString("password").trim();

        User user = userService.getUser(requestJson);
        if (user == null) {
            return Result.error(ResultCode.NODATA_ERR.getCode(), "未找到账户!").toJSONString();
        }

        // 前端页面密文解码
        // password = new String(MD5Util.decodeSimpleMd5(password));
        password = MD5Util.md5EncodeSalt(password, username, null);

        // 验证密码是否有效
        boolean iscorrect = password.equals(user.getPassword());
        if (iscorrect) {
            try {
                String userId = String.valueOf(user.getId());
                String roleId = Long.toString(user.getRoleId());
                String token = JWTUtil.createJWT(username, userId, roleId, null, null, null,
                        null);

                String tokenFinger = token + finger; //
                String md5Token = MD5Util.string2MD5(tokenFinger);
                String key = JWTUtil.ACCOUNT_PRE + md5Token;
                String hashkey = JWTUtil.ACCOUNT_PRE + username;
                //redis中添加key-token字段和超时时间,缓存token用于后续token校验
                this.valueOperations.set(key, token, JWTUtil.REDIS_TTL, TimeUnit.SECONDS);
                //redis中直接缓存用户名和token key用来判断用户是否登录过
                this.hashOperations.put(hashkey, username, key);
                this.redisTemplate.expire(hashkey, JWTUtil.REDIS_TTL, TimeUnit.SECONDS);
                // 获取用户权限
                List<String> menus = permissionService.getUserMenus(requestJson);

                loginMap.put("username", username);
                loginMap.put("token", tokenFinger);
                loginMap.put("roleId", roleId);
                loginMap.put("menus", menus);
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
    public String isLogin(JSONObject requestJson) {
        String token = requestJson.getString("token");
        Map<String, Object> isloginMap = new HashMap<String, Object>();

        String md5Token = MD5Util.string2MD5(token);
        String key = JWTUtil.ACCOUNT_PRE + md5Token;
        String jwtString = String.valueOf(this.valueOperations.get(key));

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

    @Override
    public String logout(JSONObject jsonObject) {
        String token = jsonObject.getString("token");
        String md5Token = MD5Util.string2MD5(token);
        String key = JWTUtil.ACCOUNT_PRE + md5Token;
        /**当找不到key时返回是'null'字符串**/
        String jwtString = String.valueOf(this.valueOperations.get(key));

        if (jwtString != null && jwtString.equals("null")) {
            jwtString = null;
        }

        if (jwtString != null && jwtString.length() > 0) {
            Claims claims = JWTUtil.parseJWT(jwtString, null);
            String username = String.valueOf(claims.get("username"));
            //删除字符串类型缓存
            this.redisTemplate.delete(key);
            //删除账户缓存
            String hashkey = JWTUtil.ACCOUNT_PRE + username;
            this.redisTemplate.opsForHash().delete(hashkey, username);
            //重新查询
            jwtString = String.valueOf(this.valueOperations.get(key));
        }

        if (jwtString != null && jwtString.equals("null")) {
            jwtString = null;
        }

        if (jwtString == null || jwtString.length() == 0) {
            Map logoutMap = new HashMap();
            logoutMap.put("errcode",ResultCode.SUCCESS.getCode());
            logoutMap.put("result","success");
            return Result.ok(logoutMap).toJSONString();
        } else {
            return Result.error("用户退出失败").toJSONString();
        }

    }

}
