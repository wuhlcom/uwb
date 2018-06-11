package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.dbs.pojos.RedisPolicy;
import com.zhilutec.services.IRedisPolicyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/policy")
@EnableAutoConfiguration
public class PolicyController {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    IRedisPolicyService redisPolicyService;

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public Object add(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            String key="test";
            String test="test_1";
            RedisPolicy redisPolicy=new RedisPolicy();
            redisPolicy.setStrategyName("abc");
            redisPolicy.setStrategyUserId("123");
            redisPolicyService.putRedisPolicy(key,test,redisPolicy,-1);
            return redisPolicyService.getReidsPolicy(key,test);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加策略出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public Object get() {
        try {
            String key="test";
            String test="test_1";
            return redisPolicyService.getReidsPolicy(key,test);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询策略出现异常");
            return rs.toJSONString();
        }
    }
}
