/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月26日 下午5:46:34 * 
*/ 
package com.zhilutec.controllers;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.db.entities.UserEntity;
import com.zhilutec.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/user")
@Api(value = "User")
public class UserController {
	
	@Resource
	private UserService userService;

	@ApiOperation(value = "查询用户信息", notes = "查询用户信息<br><hr/>", response = Result.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", required = false, dataType = "String", paramType = "body", defaultValue = "")
			})	
	@RequestMapping(value = "/query", method = { RequestMethod.POST })
	public UserEntity query(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody) {			
		System.out.println("RequestBody=" + requestBody);	
		UserEntity user = new UserEntity();	
		try {
			JSONObject jsonObject = JSON.parseObject(requestBody);
			String username = jsonObject.getString("username");
			user = userService.query(username.trim());			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	@ApiOperation(value = "查询用户信息", notes = "查询用户信息<br><hr/>", response = Result.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "jsonStr", value = "用户信息", required = false, dataType = "String", paramType = "body", defaultValue = "")
			})	
	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public Result add(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody){
		System.out.println("RequestBody=" + requestBody);
		try {
			JSONObject jsonObject = JSON.parseObject(requestBody);		
			Result rs = userService.add(jsonObject);
			return rs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Result.error(ResultCode.UNKNOW_ERR.getCode(),"添加用户异常");
		}
	}
	
	@ApiOperation(value = "删除囚徒信息", notes = "删除囚徒信息<br><hr/>", response = Result.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "囚徒ID", required = false, dataType = "Long", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "number", value = "囚徒编号", required = false, dataType = "String", paramType = "body", defaultValue = "")
		})	
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public Result delete(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody){
		System.out.println("RequestBody=" + requestBody);
		try {
			JSONObject jsonObject = JSON.parseObject(requestBody);		
			Result rs = userService.delete(jsonObject);
			return rs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Result.error(ResultCode.UNKNOW_ERR.getCode(),"删除用户异常");
		}
	}
	
	@ApiOperation(value = "更新囚徒信息", notes = "更新囚徒信息<br><hr/>", response = Result.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "jsonStr", value = "囚徒信息", required = false, dataType = "String", paramType = "body", defaultValue = ""),
				})	
	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public Result update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody){
		System.out.println("RequestBody=" + requestBody);
		try {
			JSONObject jsonObject = JSON.parseObject(requestBody);		
			Result rs = userService.update(jsonObject);
			return rs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Result.error(ResultCode.UNKNOW_ERR.getCode(),"更新囚徒异常");
		}
	}
}
