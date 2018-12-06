package com.zhilutec.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.dbs.entities.CommonRoute;
import com.zhilutec.dbs.entities.PathRoute;
import com.zhilutec.services.IRouteRefreshService;
import com.zhilutec.services.IRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/gateway/api")
public class RouteApiController {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IRouteService routeService;

    @Resource
    IRouteRefreshService routeRefreshService;

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String add(@RequestBody JSONObject requestJson) {
        logger.info("=====requestJson:" + requestJson);
        try {
            routeService.addRedisRoute(requestJson);
            return Result.ok(ResultCode.SUCCESS).toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加路由异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/refresh", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String refresh() {
        try {
            routeRefreshService.refreshRoute();
            return Result.ok(ResultCode.SUCCESS).toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "刷新路由异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/get", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
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
