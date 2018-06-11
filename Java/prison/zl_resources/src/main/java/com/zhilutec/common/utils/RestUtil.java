package com.zhilutec.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestUtil {

    protected final static Logger logger = LoggerFactory.getLogger(RestUtil.class);

    private static final  String Ipaddr = "http://192.168.10.232:8802";

    /**
     * 获取未查看报警数量的url
     */
    private static final String GetAmountUrl = Ipaddr + "/websocket/kafka/get";
    /**
     * 获取重置未查看报警数量的url
     */
    private static final String ResetAmountUrl = Ipaddr + "/websocket/kafka/reset";

    /**
     * 获取重置未查看报警数量的url
     */
    private static final String AreaInfoUrl = Ipaddr + "/websocket/kafka/areaInfo";

    /**
     * 初始化个人页面实时数据，以保证与监仓页面保持一致
     */
    private static final String PrisonerUrl = Ipaddr + "/websocket/kafka/prisoner";


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

    /**
     * 获取未读报警数量
     */
    public static Map<String, Object> getAmount(JSONObject jsonObj) {
        Map<String, Object> rs;
        try {
            rs = post(GetAmountUrl, jsonObj);
        } catch (Exception e) {
            logger.info("=====从Websocket获取报警数量时连接失败======");
            e.printStackTrace();
            Map rsMap = new HashMap();
            rsMap.put("amount", 0);
            rs = Result.ok(ResultCode.ROUTINE_ERR.getCode(), rsMap);
        }
        return rs;
    }

    /**
     * 获取重置未读报警数量
     */
    public static Map<String, Object> resetAmount(JSONObject jsonObj) {
        Map<String, Object> rs;
        try {
            rs = post(ResetAmountUrl, jsonObj);
        } catch (Exception e) {
            logger.info("=====从Websocket重置报警数量时连接失败======");
            e.printStackTrace();
            rs = Result.error("failed");
        }
        return rs;
    }

    /**
     * 监仓页面信息
     */
    public static Map<String, Object> getAreaInfo(JSONObject jsonObj) {
        Map<String, Object> rs;
        try {
            rs = post(AreaInfoUrl, jsonObj);
        } catch (Exception e) {
            logger.info("=====从Websocket获取监仓页面信息时连接失败======");
            e.printStackTrace();
            rs = Result.error("failed");
        }
        return rs;
    }

    /**
     * 囚犯信息
     */
    public static Map<String, Object> getPrisoner(JSONObject jsonObj) {
        Map<String, Object> rs;
        try {
            rs = post(PrisonerUrl, jsonObj);
        } catch (Exception e) {
            logger.info("=====从Websocket获取囚犯信息时连接失败======");
            e.printStackTrace();
            rs = Result.error("failed");
        }
        return rs;
    }

}
