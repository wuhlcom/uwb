package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;

import com.zhilutec.services.IUpgradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/upgrade")
public class UpgradeController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IUpgradeService upgradeService;

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public String version() {
        try {
            return upgradeService.getVersion();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询版本出现异常").toJSONString();
        }
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String upgrade(@RequestBody JSONObject jsonObject) {
        try {
            return upgradeService.upgrade(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("升级出现异常").toJSONString();
        }
    }
}
