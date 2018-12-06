package com.zhilutec.controller;

import com.zhilutec.services.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
//控制上处必须添加,否则Post请求无法识别
@RequestMapping(value = "/feign")
public class FeignController {

    @Autowired
    Resources resources;

    // @GetMapping(value = "/getDepartments",produces = "application/json;charset=UTF-8")
    // public @ResponseBody String sayHi(@RequestBody JSONObject requestBody) {
    //     return resources.getDepartments();
    // }

    @GetMapping(value = "/getDepartments",produces = "application/json;charset=UTF-8")
    public @ResponseBody String getDepartments() {
        return resources.getDepartments();
    }


}
