package com.zhilutec.services;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**value注册的服务名*/
@FeignClient(value = "uwb-resources")
public interface Resources {
    // @RequestMapping(value = "/resources/realtime/info",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    // @RequestMapping(value = "/uwb/resources/department/departments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    // String getDepartments(@RequestBody JSONObject requestBody);

    @RequestMapping(value = "/uwb/resources/department/departments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String getDepartments();
}
