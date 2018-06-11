package com.zhilutec.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestUtil {

    protected final static Logger logger = LoggerFactory.getLogger(RestUtil.class);

    private static final String Ipaddr = "http://192.168.10.232:9905";


    /**
     * 获取图片信息的完整路径url
     */
    private static final String FdfsUrl = Ipaddr + "/fastdfs/fdfs/server";

    public static Map<String, Object> get(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
        if (responseEntity.getStatusCode().value() != 200) {
            logger.info("连接文件服务器失败");
            return Result.error("连接文件服务器失败");
        }
        JSONObject jsonObject = responseEntity.getBody();
        if (jsonObject==null) {
            return Result.error("未获取到服务器信息");
        }
        return jsonObject;
    }

    /**
     * 获取图片信息
     */
    public static Map<String, Object> getServerinfo() {
        Map<String, Object> rs = new HashMap<>();
        try {
            rs = get(FdfsUrl);
        } catch (Exception e) {
            logger.info("=====连接fastdfs服务失败======");
            e.printStackTrace();
            rs = Result.error("failed");
        }
        return rs;
    }
}
