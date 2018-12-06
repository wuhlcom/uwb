package com.zhilutec.uwb.controllers;

import com.zhilutec.uwb.service.ILevelService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;

import javax.annotation.Resource;

@RestController
@RequestMapping("/level")
@EnableAutoConfiguration
public class LevelController {

    @Resource
    ILevelService levelService;

    @RequestMapping(value = "/levels", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String ranks() {
        try {
            return levelService.getLevelsRs();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询级别异常");
            return rs.toJSONString();
        }
    }


    @RequestMapping(value = "/info", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String level() {
        try {
            return levelService.getlevel();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "获取级别和职务出现异常");
            return rs.toJSONString();
        }

    }

}
