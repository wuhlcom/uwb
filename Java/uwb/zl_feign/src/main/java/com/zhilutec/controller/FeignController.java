package com.zhilutec.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.services.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
//控制上处必须添加,否则Post请求无法识别
@RequestMapping(value = "/feign")
public class FeignController {

    @Autowired
    Resources resources;

    @PostMapping(value = "/info",produces = "application/json;charset=UTF-8")
    public @ResponseBody String sayHi(@RequestBody JSONObject requestBody) {
        return resources.info(requestBody);
    }

}
