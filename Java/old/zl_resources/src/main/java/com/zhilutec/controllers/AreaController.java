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
import com.zhilutec.services.IAreaService;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IWarningStatusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

@RestController
@RequestMapping(value = "/area")
@EnableAutoConfiguration
@Api(value = "Area")
public class AreaController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IAreaService areaService;

    @Resource
    private IPrisonerService prisonerService;

    @Resource
    private IWarningStatusService warningStatusService;

    @ApiOperation(value = "查询单个监仓信息", notes = "查询监仓信息<br><hr/>", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "监仓编号", required = false, dataType = "String", paramType = "body", defaultValue = "")})
    @RequestMapping(value = "/query", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String query(HttpServletRequest request, HttpServletResponse response,
                        @RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return areaService.getAreaInfo(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询监仓信息出现异常");
            return rs.toJSONString();
        }
    }

    @ApiOperation(value = "查询多个监仓信息", notes = "查询多个监仓信息<br><hr/>", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "监仓编号", required = false, dataType = "String", paramType = "body", defaultValue = "")})
    @RequestMapping(value = "/areas/query", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String areasQuery(@RequestBody String requestBody) {
        try {
            logger.info("=====RequestBody:" + requestBody);
            return areaService.queryAreasResult();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询区域信息出现异常");
            return rs.toJSONString();
        }
    }

    @ApiOperation(value = "查询监仓缺勤信息列表", notes = "查询监仓缺勤信息列表<br><hr/>", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "消息监仓编号", required = false, dataType = "String", paramType = "body", defaultValue = ""),
            @ApiImplicitParam(name = "page", value = "起始页", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),
            @ApiImplicitParam(name = "listRows", value = "数量", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),
            @ApiImplicitParam(name = "startTime", value = "时间范围开始", required = false, dataType = "Long", paramType = "body", defaultValue = ""),
            @ApiImplicitParam(name = "endTime", value = "时间范围结束", required = false, dataType = "Long", paramType = "body", defaultValue = "")
    })
    @RequestMapping(value = "/absence", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String absence(HttpServletRequest request, HttpServletResponse response,
                          @RequestBody String requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return warningStatusService.getAreaAbsence(requestBody);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询监仓缺勤列表出现异常");
            return rs.toJSONString();
        }
    }

    /**
     * 这里的报警包括离线消息
     */
    @ApiOperation(value = "查询监仓报警信息列表", notes = "查询监仓报警信息列表<br><hr/>", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "消息监仓编号", required = false, dataType = "String", paramType = "body", defaultValue = ""),
            @ApiImplicitParam(name = "page", value = "起始页", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),
            @ApiImplicitParam(name = "listRows", value = "数量", required = false, dataType = "Integer", paramType = "body", defaultValue = ""),
            @ApiImplicitParam(name = "startTime", value = "时间范围开始", required = false, dataType = "Long", paramType = "body", defaultValue = ""),
            @ApiImplicitParam(name = "endTime", value = "时间范围结束", required = false, dataType = "Long", paramType = "body", defaultValue = "")
    })
    @RequestMapping(value = "/warning", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String warning(HttpServletRequest request, HttpServletResponse response,
                          @RequestBody String requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return warningStatusService.getAreaWarnings(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询监仓报警统计出现异常");
            return rs.toJSONString();
        }
    }


    @ApiOperation(value = "电子点名", notes = "电子点名<br><hr/>", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "监仓编号", required = false, dataType = "String", paramType = "body", defaultValue = "")})
    @RequestMapping(value = "/summary", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String summary(@RequestBody String requestBody) {
        try {
            logger.info("=====RequestBody:" + requestBody);
            return prisonerService.getAttendanceStatistics(requestBody);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "电子点名出现异常");
            return rs.toJSONString();
        }
    }

    @ApiOperation(value = "根据楼栋查询区域信息", notes = "根据楼栋查询区域信息<br><hr/>", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "{}", value = "{}", required = false, dataType = "String", paramType = "body", defaultValue = "")})
    @RequestMapping(value = "/queryCode", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String queryCode(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody String requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return areaService.queryResult(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "根据楼栋查询区域信息出现异常");
            return rs.toJSONString();
        }
    }

    @ApiOperation(value = "查询区域囚犯信息", notes = "查询区域囚犯信息<br><hr/>", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "监仓编号", required = false, dataType = "String", paramType = "body", defaultValue = "")})
    @RequestMapping(value = "/prisoners", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String prisoners(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody String requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return prisonerService.queryByAreaResult(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询监仓囚犯信息出现异常");
            return rs.toJSONString();
        }
    }

}
