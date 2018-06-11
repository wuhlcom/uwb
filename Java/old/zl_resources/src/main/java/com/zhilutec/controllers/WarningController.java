/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月30日 下午4:14:39 * 
*/ 
package com.zhilutec.controllers;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.services.IWarningStatusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;


@RestController
@RequestMapping(value = "/warning")
@EnableAutoConfiguration
@Api(value = "Warning")
public class WarningController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IWarningStatusService warningStatusService;
	
	
	@ApiOperation(value = "查询所有报警信息", notes = "查询所有报警信息<br><hr/>", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "domainUniqueCode", value = "大区间编号", required = false, dataType = "String", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "page", value = "起始页", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "listRows", value = "数量", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "buildingUniqueCode", value = "楼栋编号", required = false, dataType = "String", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "floorUniqueCode", value = "楼层编号", required = false, dataType = "String", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "areaCode", value = "区域编号", required = false, dataType = "String", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "order", value = "排序类型", required = false, dataType = "String", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "startTime", value = "时间范围开始", required = false, dataType = "Long", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "endTime", value = "时间范围结束", required = false, dataType = "Long", paramType = "body", defaultValue = "")
		})	
	@RequestMapping(value = "/areas", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String areas(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody) {			
		logger.info("=====RequestBody:" + requestBody);	
		try {
			return warningStatusService.getWariningList(requestBody);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询报警信息列表异常");
			return rs.toJSONString();
		}
	}
	
	@ApiOperation(value = "报警信息处理", notes = "报警信息处理<br><hr/>", response = String.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "个人编号", required = false, dataType = "String", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "state", value = "报警消息状态", required = false, dataType = "String", paramType = "body", defaultValue = ""),
		@ApiImplicitParam(name = "remark", value = "报警处理备注", required = false, dataType = "String", paramType = "body", defaultValue = "")
		})	
	@RequestMapping(value = "/manage", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String manage(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody) {			
		logger.info("=====RequestBody:" + requestBody);
		try {
			return warningStatusService.updateWarning(requestBody);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "报警信息处理出现异常");
			return rs.toJSONString();
		}
	}

	@ApiOperation(value = "重置报警计数", notes = "重置报警计数<br><hr/>", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "amount", value = "重置数量", required = false, dataType = "String", paramType = "body", defaultValue = "")
	})
	@RequestMapping(value = "/reset", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
	public String reset(HttpServletRequest request, HttpServletResponse response,
							@RequestBody JSONObject requestBody) {
		logger.info("=====RequestBody:" + requestBody);
		try {
			return warningStatusService.resetWsAmount(requestBody);
		} catch (Exception e) {
			e.printStackTrace();
			Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "重置未查看报警数量出现异常");
			return rs.toJSONString();
		}
	}
	
}
