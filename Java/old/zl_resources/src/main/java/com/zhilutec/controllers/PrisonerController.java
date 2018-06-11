/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月30日 下午4:14:39 * 
*/ 
package com.zhilutec.controllers;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IWarningStatusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/prisoner")
@EnableAutoConfiguration
@Api(value = "Prisoner")
public class PrisonerController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IPrisonerService prisonerService;
	@Resource
	private IWarningStatusService warningStatusService;

	@ApiOperation(value = "查询囚徒信息", notes = "查询囚徒信息<br><hr/>", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "number", value = "囚徒编号", required = false, dataType = "String", paramType = "body", defaultValue = "")
			})	
	@RequestMapping(value = "/info", method = { RequestMethod.POST },produces = "application/json;charset=UTF-8")
	public String info(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody) {			
		logger.info("=====RequestBody:" + requestBody);
		try {
			return prisonerService.getPrisonerInfo(requestBody);			
		} catch (Exception e) {		
			e.printStackTrace();
			return Result.error(ResultCode.UNKNOW_ERR.getCode(),"查询囚徒信息异常").toJSONString();
		}
	}
	       	
	@ApiOperation(value = "查询囚徒报警列表", notes = "查询囚徒报警列表<br><hr/>", response = Result.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "code", value = "囚徒编号", required = false, dataType = "String", paramType = "body", defaultValue = ""),
			@ApiImplicitParam(name = "page", value = "起始页", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),
			@ApiImplicitParam(name = "listRows", value = "数量", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),	
			@ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "body", defaultValue = ""),
			@ApiImplicitParam(name = "order", value = "排序类型", required = false, dataType = "String", paramType = "body", defaultValue = ""),
			@ApiImplicitParam(name = "startTime", value = "时间范围开始", required = false, dataType = "Long", paramType = "body", defaultValue = ""),
			@ApiImplicitParam(name = "endTime", value = "时间范围结束", required = false, dataType = "Long", paramType = "body", defaultValue = "")
			})	
	@RequestMapping(value = "/warnings", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String  warnings(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody){
		logger.info("=====RequestBody:" + requestBody);
		try {			
			return warningStatusService.getPrisonerWarningsList(requestBody);
		} catch (Exception e) {	
			e.printStackTrace();	
			return Result.error(ResultCode.UNKNOW_ERR.getCode(),"查询囚徒报警列表异常").toJSONString();
		}
	}
}
