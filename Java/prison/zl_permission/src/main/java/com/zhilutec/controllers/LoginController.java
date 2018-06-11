package com.zhilutec.controllers;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.JwtHelper;
import com.zhilutec.common.utils.MD5Util;
import com.zhilutec.db.entities.UserEntity;
import com.zhilutec.services.UserService;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600, methods = { RequestMethod.POST })
@RestController
@RequestMapping(value = "/user")
@EnableAutoConfiguration
@Api(value = "Login")
public class LoginController {

	public final static Logger logger = LoggerFactory.getLogger(LoginController.class);
	private static RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private UserService userService;

	private String accountField="accountField";
	private String audience = "zl_prison";
	private String issuer = "prison";
	private Long ttlMillis = -1L;
	private Long redisTTL = 1800L;
	private String base64Security = "prisonSecurity";

	@ApiOperation(value = "登录接口", notes = "用户登录接口<br/>密码会被加密<br><hr/>", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "body", defaultValue = "admin"),
			@ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "body", defaultValue = "123456")})
	@RequestMapping(value = "/login", method = { RequestMethod.POST },produces = "application/json;charset=UTF-8")
	public String login(HttpServletRequest request, HttpServletResponse response, @RequestBody String requestBody) {

		Map<String, Object> loginMap = new HashMap<String, Object>();
		Map<String, Object> rsMap = new HashMap<String, Object>();

		try {
			JSONObject jsonObject = JSON.parseObject(requestBody);
			String finger = request.getHeader("finger");

			String username = jsonObject.getString("username");
			String password = jsonObject.getString("password");

			// 前端页面密文解码
			password = new String(decode(password));	
			password = MD5Util.string2MD5(password);
			UserEntity user = userService.query(username.trim());
			if (user==null) {
				Result.error(ResultCode.NODATA_ERR.getCode(),"未找到账户!");
			} 

			// 验证密码是否有效
			boolean iscorrect = password.trim().equals(user.getPassword().trim());
			if (iscorrect) {
				String userName = user.getUsername();
				String userId = String.valueOf(user.getId());
				String roleId = String.valueOf(user.getRole_id());

				// 登录日志
				// sendLog(userId, "login");

				String token = JwtHelper.createJWT(userName, userId, roleId, audience, issuer, ttlMillis,
						base64Security);
				try {

					String tokenFinger = token + finger;
					String key = MD5Util.string2MD5(tokenFinger);				
					
					//redis中添加hash字段hash-username-token			
					this.hashOperations.put(accountField, userName, key);
					//redis中设置redisTemplate超时时间
					this.redisTemplate.expire(accountField, redisTTL, TimeUnit.SECONDS);
					//redis中添加key-token字段和超时时间
					this.valueOperations.set(key, token, redisTTL, TimeUnit.SECONDS);

					// 获取用户权限
					// String url =
					// "http://172.16.140.79:8012/permission/role/queryRoleMenu";
					// JSONObject postData = new JSONObject();
					// postData.put("token", token);
					// System.out.println(postData);
					// JSONObject menus = restTemplate.postForEntity(url,
					// postData, JSONObject.class).getBody();

					// HttpHeaders headers = new HttpHeaders();
					// headers.add("token", token+finger);
					// HttpEntity<String> entity = new
					// HttpEntity<String>(postData.toJSONString(), headers);
					// String menus = restTemplate.postForObject(url, entity,
					// String.class);
					// JSONObject menuObject = JSONObject.parseObject(menus);
					// System.out.println(menuObject.getString("result"));

					// loginMap.put("menus", menuObject.getString("result"));
					loginMap.put("username", user.getUsername());			
					loginMap.put("token", tokenFinger);
					rsMap.put("result", loginMap);
					return Result.ok(rsMap).toJSONString();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					loginMap.put("errcode", ResultCode.Failed.getCode());
					loginMap.put("msg", "用户登录失败");
					return Result.error(loginMap).toJSONString();
				}
			} else {
				loginMap.put("errcode", ResultCode.Failed.getCode());
				loginMap.put("msg", "用户名或密码错误");
				return Result.ok(loginMap).toJSONString();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();	
			loginMap.put("errcode", ResultCode.Failed.getCode());
			loginMap.put("msg", "用户登录出现异常");
			return Result.error(loginMap).toJSONString();
		}

	}

	@ApiOperation(value = "登录验证", notes = "根据TOken状态判断用户是否登录成功<br><hr/>", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "Token", required = true, dataType = "String", paramType = "body") })
	@RequestMapping(value = "/islogin",method = { RequestMethod.POST },produces = "application/json;charset=UTF-8")
	public String islogin(HttpServletRequest request, HttpServletResponse response, @RequestBody String requestBody) {

		JSONObject jsonObject = JSON.parseObject(requestBody);
		String token = jsonObject.getString("token");
		Map<String, Object> isloginMap = new HashMap<String, Object>();
		Map<String, Object> rsMap = new HashMap<String, Object>();
		try {
			String key = MD5Util.string2MD5(token);
			String jwtString = String.valueOf(this.valueOperations.get(key));		
			if (jwtString == null ||jwtString == "null" || jwtString.length() <= 0) {
				isloginMap.put("errcode", ResultCode.Failed.getCode());
				isloginMap.put("msg", "用户未登录系统");
				return Result.error(isloginMap).toJSONString();
			} else {
				Claims claims = JwtHelper.parseJWT(jwtString, base64Security);			
				String username = String.valueOf(claims.get("unique_name"));
				String userId = String.valueOf(claims.get("userid"));
				String roleId = String.valueOf(claims.get("role"));
				UserEntity user = userService.query(username.trim());
				if (null == user) {
					rsMap.put("errcode", ResultCode.Failed.getCode());
					rsMap.put("msg", "用户名或密码错误");
					return Result.error(rsMap).toJSONString();
				} else {
					this.redisTemplate.expire(key, 1800, TimeUnit.SECONDS);
					isloginMap.put("username", username);
					isloginMap.put("userId", userId);
					isloginMap.put("roleId", roleId);			
			
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isloginMap.put("errcode", ResultCode.ROUTINE_ERR.getCode());
			isloginMap.put("msg", "请登录系统");
			return Result.error(isloginMap).toJSONString();
		}
		return Result.ok(ResultCode.SUCCESS.getCode(),isloginMap).toJSONString();
	}

	@ApiOperation(value = "用户退出", notes = "用户退出<br><hr/>", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "Token", required = true, dataType = "String", paramType = "body") })	
	@RequestMapping(value = "/logout",method = { RequestMethod.POST },produces = "application/json;charset=UTF-8")
	public String logout(HttpServletRequest request, HttpServletResponse response, @RequestBody String requestBody) {
		Map<String, Object> logoutMap = new HashMap<String, Object>();
		try {		
			JSONObject jsonObject = JSON.parseObject(requestBody);
			String token = jsonObject.getString("token");
			
			String key = MD5Util.string2MD5(token);
			/**当找不到key时返回是'null'字符串**/
			String jwtString = String.valueOf(this.valueOperations.get(key));	
			if (jwtString != null&&jwtString.equals("null")) {
				jwtString=null;
			}
			System.out.println(jwtString);
			
			if (jwtString != null && jwtString.length() > 0) {				
				Claims claims = JwtHelper.parseJWT(jwtString, base64Security);
				String username = String.valueOf(claims.get("unique_name"));				
				//删除字符串token
				this.redisTemplate.delete(key);	
				//删除hash token
				this.redisTemplate.opsForHash().delete(accountField,username);	
				Thread.sleep(1000);;
				jwtString = String.valueOf(this.valueOperations.get(key));				
				// 记录用户退出时间
				// sendLog(userid, "exit");
			}
			
			if (jwtString != null&&jwtString.equals("null")) {
				jwtString=null;
			}
			
			if (jwtString == null || jwtString.length() == 0) {
				logoutMap.put("errcode", ResultCode.SUCCESS.getCode());
				logoutMap.put("result", "success");
				return Result.ok(logoutMap).toJSONString();
			}else{
				logoutMap.put("errcode", ResultCode.Failed.getCode());
				logoutMap.put("msg", "用户退出失败");
				return Result.error(logoutMap).toJSONString();
			}				
		} catch (Exception e) {
			e.printStackTrace();
			logoutMap.put("errcode", ResultCode.ROUTINE_ERR.getCode());
			logoutMap.put("msg", "用户退出异常");
			return Result.error(logoutMap).toJSONString();
		}
	}

	public static void sendLog(String userid, String operation) {
		String url = "http://172.16.140.79:8084/systemlog/add";
		JSONObject postData = new JSONObject();
		postData.put("EmployId", userid);
		postData.put("operation", operation);
		System.out.println(postData);
		HttpEntity<String> entity = new HttpEntity<String>(postData.toJSONString());
		restTemplate.postForObject(url, entity, String.class);
	}

	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
			5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26,
			27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,
			-1, -1, -1 };

	/**
	 * 解密
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] decode(String str) {
		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while (i < len) {
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}

			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			do {
				b3 = data[i++];
				if (b3 == 61) {
					return buf.toByteArray();
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			do {
				b4 = data[i++];
				if (b4 == 61) {
					return buf.toByteArray();
				}
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		return buf.toByteArray();
	}

}
