package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "resources")
public interface Resources {
    // @RequestMapping(value = "/resources/realtime/info",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @RequestMapping(value = "/resources/realtime/info",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    String  info(@RequestBody JSONObject requestBody);
}
