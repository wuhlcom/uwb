package com.zhilutec.controllers;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.services.IFastDFSFileService;
import com.zhilutec.services.IFileService;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


// @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/fdfs")
@EnableAutoConfiguration
public class FastDFSController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IFastDFSFileService fastDFSFileService;

    @Resource
    IFileService fileService;

    @RequestMapping(value = "/upload", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String fileUpload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        try {
            return fastDFSFileService.uploadFdfsFileRs(multipartRequest);
        } catch (Exception e) {
            logger.error("upload file failed", e);
            return Result.error("文件上传出现异常").toJSONString();
        }
    }


    @RequestMapping(value = "/files", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String files(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fileService.getFilesRs(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取文件信息失败").toJSONString();
        }
    }

    @RequestMapping(value = "/download", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String download(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fastDFSFileService.downloadFileByNameRs(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件下载失败").toJSONString();
        }
    }


    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fileService.update(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件更新失败").toJSONString();
        }
    }

    @RequestMapping(value = "/switch", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String mapSwitch(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fileService.mapSwitch(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改文件状态失败").toJSONString();
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String delete(@RequestBody JSONObject requestBody) {
        logger.info("=====RequestBody:" + requestBody);
        try {
            return fileService.delete(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件更新失败").toJSONString();
        }
    }

    @RequestMapping(value = "/server", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String server() {
        try {
            String server = fastDFSFileService.getServer();
            if (server == null) {
                return Result.error("找不到文件服务器地址").toJSONString();
            } else {
                return server;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件更新失败").toJSONString();
        }
    }


}
