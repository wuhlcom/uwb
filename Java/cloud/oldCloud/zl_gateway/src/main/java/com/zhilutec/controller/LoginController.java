package com.zhilutec.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.services.ILoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", maxAge = 3600, methods = {RequestMethod.POST})
@RestController
@RequestMapping(value = "/gateway/user")
@EnableAutoConfiguration
public class LoginController {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ILoginService loginService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String login(@RequestBody JSONObject requestJson) {
        logger.info("============用户登录====================");
        logger.info("requestJson:" + requestJson.toJSONString());
        try {
            return loginService.login(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("用户登录出现异常").toJSONString();
        }

    }


    @RequestMapping(value = "/loginStatus", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String loginStatus(@RequestBody JSONObject requestJson) {
        logger.info("============校验Token====================");
        logger.info("requestJson:" + requestJson.toJSONString());
        try {
            return loginService.loginStatus(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("请登录系统").toJSONString();
        }
    }


    @RequestMapping(value = "/logout", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String logout(@RequestBody JSONObject requestJson) {
        logger.info("============退出登录====================");
        logger.info("requestJson:" + requestJson.toJSONString());
        try {
            return loginService.logout(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("用户退出异常").toJSONString();
        }
    }

}
