package com.zhilutec.controllers;


import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.services.IPermissionService;
import com.zhilutec.services.IUserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/menu")
public class PermissionContoller {

    @Resource
    IPermissionService permissionService;

    @RequestMapping(value = "/get", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String get(@RequestBody JSONObject requestJson) {
        System.out.println(requestJson.toString());
        try {
            return permissionService.getUserPermissions(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询权限菜单出现异常").toJSONString();
        }
    }

}
