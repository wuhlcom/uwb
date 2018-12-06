package com.zhilutec.uwb.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.service.IDepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/department")
@EnableAutoConfiguration
public class DepartmentController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IDepartmentService departmentService;

    @RequestMapping(value = "/tree", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String tree() {
        try {
            return departmentService.getDepartmentsTreeRs();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询部门树异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return departmentService.delete(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除部门异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String add(
            @RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return departmentService.add(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "添加部门异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(
            @RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return departmentService.update(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "修改部门异常");
            return rs.toJSONString();
        }
    }

    @RequestMapping(value = "/departments", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String departments() {
        try {
            return departmentService.getDepartments();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "获取部门信息异常");
            return rs.toJSONString();
        }
    }

}
