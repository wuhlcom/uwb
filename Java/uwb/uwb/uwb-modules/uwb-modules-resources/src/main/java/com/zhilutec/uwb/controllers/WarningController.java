package com.zhilutec.uwb.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.service.IWarningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/warning")
@EnableAutoConfiguration
public class WarningController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IWarningService warningService;


    @RequestMapping(value = "/warnings", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String warnings(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return warningService.warnings(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询告警出现异常");
            return rs.toJSONString();
        }
    }


    @RequestMapping(value = "/statistics", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String statistics(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return warningService.getStatistics(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询告警分类统计异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return warningService.update(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "修改告警状态异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/amount", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String amount() {
        try {
            return warningService.getWarningAmount();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询告警数量异常");
            return rs.toJSONString();
        }
    }

}
