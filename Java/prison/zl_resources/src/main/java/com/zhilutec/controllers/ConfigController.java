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
import com.zhilutec.services.IPrisonerCfgService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/config")
@EnableAutoConfiguration
@Api(value = "Config")
public class ConfigController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IPrisonerCfgService prisonerConfigService;
	@ApiOperation(value = "查询监仓缺勤统计", notes = "查询监仓缺勤统计<br><hr/>", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "areaCode", value = "监仓编号", required = false, dataType = "String", paramType = "body", defaultValue = "")
			
			})	
	@RequestMapping(value = "/absence/query", method = { RequestMethod.POST },produces = "application/json;charset=UTF-8")
	public Object AbsenteeismQuery(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody) {			
		logger.info("=====RequestBody:" + requestBody);
		try {	
			return 	prisonerConfigService.absence(requestBody);
		} catch (Exception e) {	
			e.printStackTrace();
			return null;		}

	}
}
