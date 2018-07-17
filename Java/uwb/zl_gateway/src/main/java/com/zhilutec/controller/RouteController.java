package com.zhilutec.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.config.result.Result;
import com.zhilutec.config.result.ResultCode;
import com.zhilutec.services.IRouteRefreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.zhilutec.services.IRouteService;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/route")
@EnableAutoConfiguration
public class RouteController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IRouteService routeService;

    @Resource
    IRouteRefreshService routeRefreshService;

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String add(@RequestBody JSONObject requestJson) {
        logger.info("=====requestJson:" + requestJson);
        try {
            routeService.add(requestJson);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加路由异常");
            return rs.toJSONString();
        }
    }


    @RequestMapping(value = "/refresh", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String refresh() {
        logger.info("=====requestJson:");
        try {
            routeRefreshService.refreshRoute();
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加路由异常");
            return rs.toJSONString();
        }
    }
}
