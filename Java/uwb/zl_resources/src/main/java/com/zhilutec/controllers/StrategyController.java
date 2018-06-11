package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.configs.redis.Overtime;
import com.zhilutec.dbs.pojos.RedisPolicy;
import com.zhilutec.services.IRedisPolicyService;
import com.zhilutec.services.IStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping(value = "/strategy")
@EnableAutoConfiguration
public class StrategyController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IStrategyService strategyService;

    @Resource
    IRedisPolicyService redisPolicyService;

    // @Cacheable(value = "strategies", key = "#requestBody.getString(\"strategyName\")")
    // @Cacheable(value = "strategies")
    // @Overtime(60L)
    @RequestMapping(value = "/strategies", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String strategies(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return strategyService.strategies(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询策略出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return strategyService.delete(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除策略异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String add(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return strategyService.add(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加策略异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return strategyService.update(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "修改策略异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/switch", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String policySwitch(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return strategyService.policySwitch(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "开关策略异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/test", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String test(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            Long tagId = requestBody.getLong("tagId");
            List<RedisPolicy> rs= redisPolicyService.getRedisPolicies(tagId);
            System.out.println(rs.size());
            System.out.println(rs);
            for (RedisPolicy r : rs) {
                System.out.println(r.getStartTime());
                System.out.println(r.getFinishTime());
            }
            return Result.ok(rs.toString()).toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "开关策略异常");
            return rs.toJSONString();
        }
    }

}
