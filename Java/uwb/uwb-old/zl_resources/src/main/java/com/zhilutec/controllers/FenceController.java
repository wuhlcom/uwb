package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.services.IFenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/fence")
@EnableAutoConfiguration
public class FenceController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IFenceService fenceService;

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String addfence(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fenceService.add(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加区域出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/fences", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String fences(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fenceService.getfences(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "获取区域列表出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/groupFences", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String groupFences(@RequestBody JSONObject requestJson) {
        logger.info("=====RequestBody:" + requestJson);
        try {
            return fenceService.groupFencesByType(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "根据分组查询区域信息出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fenceService.update(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "更新区域信息出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fenceService.delete(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除区域出现异常");
            return rs.toJSONString();
        }
    }

}
