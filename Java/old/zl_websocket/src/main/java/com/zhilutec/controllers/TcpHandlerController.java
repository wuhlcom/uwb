package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.netty.tcpServer.TcpHandler;
import com.zhilutec.services.IAreaService;
import com.zhilutec.services.ITcpHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/tcp")
public class TcpHandlerController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ITcpHandlerService tcpHandlerService;

    @Resource
    IAreaService areaService;

    /**报警数量*/
    @RequestMapping("/get")
    public String getAmount(HttpServletRequest request, HttpServletResponse response,
                               @RequestBody String requestBody){
        logger.info("=====RequestBody:" + requestBody);
        try {
            return tcpHandlerService.getWaningAmount();
        } catch (Exception e) {
            e.printStackTrace();
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询未查看报警数量出现异常");
            return rs.toJSONString();
        }
    }

    /**重置报警数量*/
    @RequestMapping("/reset")
    public String resetAmount(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody JSONObject requestBody){
        logger.info("=====RequestBody:" + requestBody);
        try {
            return tcpHandlerService.resetWarning(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("重置未查看报警数量出现异常");
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "重置未查看报警数量出现异常");
            return rs.toJSONString();
        }
    }

    /**监仓页面信息*/
    @RequestMapping("/areaInfo")
    public String areaInfo(HttpServletRequest request, HttpServletResponse response,
                              @RequestBody JSONObject requestBody){
        logger.info("=====RequestBody:" + requestBody);
        try {
            return tcpHandlerService.getStatisticsInfo(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取监仓页面信息出现异常");
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "查询监仓页面信息出现异常");
            return rs.toJSONString();
        }
    }

    /**囚犯信息*/
    @RequestMapping("/prisoner")
    public String prisoner(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody JSONObject requestBody){
        logger.info("=====RequestBody:" + requestBody);
        try {
            return tcpHandlerService.getPrisonerMsg(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取囚犯信息出现异常");
            Result rs = Result.error(ResultCode.UNKNOW_ERR.getCode(), "获取囚犯信息出现异常");
            return rs.toJSONString();
        }
    }

}
