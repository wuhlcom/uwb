package com.zhilutec.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestUtil {

    protected final static Logger logger = LoggerFactory.getLogger(RestUtil.class);

    private static final String Ipaddr = "http://192.168.10.166:9905";

    /**
     * 获取图片信息的完整路径url
     */
    private static final String FdfsUrl = Ipaddr + "/fastdfs/fdfs/fileInfo";


    public static Map<String, Object> post(String url, JSONObject jsonObj) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
        Map<String, Object> result;
        result = restTemplate.postForObject(url, formEntity, Map.class);
        return result;
    }

    /**获取图片信息*/
    public static Map<String, Object> getFileInfo(JSONObject jsonObj) {
        Map<String, Object> rs=new HashMap<>();
        try {
            rs = post(FdfsUrl, jsonObj);
        } catch (Exception e) {
            logger.info("=====连接fastdfs服务失败======");
            e.printStackTrace();
            rs = Result.error("failed");
        }
        return rs;
    }
}
