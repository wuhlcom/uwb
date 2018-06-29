package com.zhilutec.controllers;


import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user")
public class UserContoller {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IUserService userService;

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String addUser(@RequestBody JSONObject requestJson) {
        logger.info("======addUser=======");
        logger.info(requestJson.toString());
        try {
            return userService.add(requestJson).toJSONString();//将用户数据插入数据库
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加用户出现异常").toJSONString();
        }
    }

}
