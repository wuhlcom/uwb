package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.services.ICoordinateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/coordinate")
@EnableAutoConfiguration
public class CoordinateController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ICoordinateService coordinateService;


    @RequestMapping(value = "/coordinates", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String coordinates(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return coordinateService.getCoordiantesByTime(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询坐标信息出现异常");
            return rs.toJSONString();
        }
    }


}
