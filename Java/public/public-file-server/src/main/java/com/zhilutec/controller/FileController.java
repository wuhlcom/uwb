package com.zhilutec.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
public class FileController {
    private Map<String, String> agrs = new HashMap<String, String>();

    private ServletInputStream sis = null; //

    private byte[] b = new byte[4096]; //

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/file/upload", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String upload(MultipartHttpServletRequest httpServletRequest) {
        try {
            System.out.println(httpServletRequest);
            System.out.println(httpServletRequest.getMultiFileMap());
            System.out.println(httpServletRequest.getParameter("file_name"));
            System.out.println(httpServletRequest.getParameterMap());
            System.out.println(httpServletRequest.getParameterMap().get("file_name"));
            System.out.println(httpServletRequest.getRequestURI());
            System.out.println(httpServletRequest.getRequestURL());
            System.out.println(httpServletRequest.getMethod());
            System.out.println(httpServletRequest.getAttributeNames().hasMoreElements());
            return "ok";
        } catch (Exception e) {
            logger.error("upload file failed", e);
            return "{\"result\":\"failed\"}";
        }
    }
}
