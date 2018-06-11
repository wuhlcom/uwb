package com.zhilutec.controllers;

import java.util.List;

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
import com.zhilutec.db.results.PointResult;
import com.zhilutec.services.ICoordinateService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/coordinate")
@EnableAutoConfiguration
@Api(value = "Coordinate")
public class CoordinateController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ICoordinateService coordinateService;

	@ApiOperation(value = "查询监仓坐标信息", notes = "查询囚徒信息<br><hr/>", response = String.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "消息类型", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),
			@ApiImplicitParam(name = "tagIds", value = "tag id数组", required = false, dataType = "List", paramType = "body", defaultValue = "")
			})	
	@RequestMapping(value = "/queryPoints", method = { RequestMethod.POST },produces = "application/json;charset=UTF-8")
	public Object queryPoints(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestBody) {			
		logger.info("=====RequestBody:" + requestBody);
		List<PointResult> rs = null;
		try {	
			rs = coordinateService.queryPoints(requestBody);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	
}
