package com.zhilutec.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.services.IRouteRefreshService;
import com.zhilutec.services.IRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/gateway/route")
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
            return routeService.addRedisRoute(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加路由异常");
            return rs.toJSONString();
        }
    }


    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject requestJson) {
        logger.info("=====requestJson:" + requestJson);
        try {
            return routeService.updateRedisRoute(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "更新路由异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/refresh", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String refresh() {
        logger.info("=======手动刷新路由表=======");
        try {
            routeRefreshService.refreshRoute();
            return Result.ok(ResultCode.SUCCESS).toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "刷新路由异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/query", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String get(@RequestBody JSONObject requestJson) {
        logger.info("=====requestJson:" + requestJson);
        try {
            return routeService.getPathRouteRs(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询路由异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody JSONObject requestJson) {
        logger.info("=====requestJson:" + requestJson);
        try {
            routeService.delRedisRoute(requestJson);
            return Result.ok(ResultCode.SUCCESS).toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除路由异常");
            return rs.toJSONString();
        }
    }

}
