package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.services.IStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/strategy")
@EnableAutoConfiguration
public class StrategyController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IStrategyService strategyService;


    // @Cacheable(value = "strategies", key = "#requestJson.getString(\"strategyName\")")
    // @Cacheable(value = "strategies")
    // @Overtime(60L)
    @RequestMapping(value = "/strategies", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String strategies(@RequestBody JSONObject requestJson) {
        logger.info("=====RequestBody:" + requestJson);
        try {
            return strategyService.strategies(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询策略出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody JSONObject requestJson) {
        logger.info("=====RequestBody:" + requestJson);
        try {
            return strategyService.delete(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除策略异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String add(@RequestBody JSONObject requestJson) {
        logger.info("=====RequestBody:" + requestJson);
        try {
            return strategyService.add(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加策略异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject requestJson) {
        logger.info("=====RequestBody:" + requestJson);
        try {
            return strategyService.update(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "修改策略异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/switch", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String policySwitch(@RequestBody JSONObject requestJson) {
        logger.info("=====RequestBody:" + requestJson);
        try {
            return strategyService.policySwitch(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "开关策略异常");
            return rs.toJSONString();
        }
    }

}
