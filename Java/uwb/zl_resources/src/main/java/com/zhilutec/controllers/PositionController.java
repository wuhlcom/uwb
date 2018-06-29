package com.zhilutec.controllers;

import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.services.IPositionService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/position")
@EnableAutoConfiguration
public class PositionController {

    @Resource
    IPositionService positionService;

    @RequestMapping(value = "/positions", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String positions() {
        try {
            return positionService.getPositionsRs();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询职务出现异常");
            return rs.toJSONString();
        }
    }


}
