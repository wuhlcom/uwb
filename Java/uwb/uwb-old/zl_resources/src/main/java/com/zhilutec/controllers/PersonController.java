package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.services.IPersonService;
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
@RequestMapping(value = "/person")
@EnableAutoConfiguration
public class PersonController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    IPersonService personService;

    @RequestMapping(value = "/persons", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String persons(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return personService.getPersonAndDepartmentRs(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询人员信息出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/topPersons", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String topPersons() {
        try {
            return personService.getPersonsInTopDepartment();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询顶级部门人员信息出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String add(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return personService.add(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加人员信息出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return personService.delete(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除人员信息出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return personService.update(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "修改人员信息出现异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/simple", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String simple() {
        try {
            return personService.getPersons();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "简单查询人员信息出现异常");
            return rs.toJSONString();
        }
    }


}
