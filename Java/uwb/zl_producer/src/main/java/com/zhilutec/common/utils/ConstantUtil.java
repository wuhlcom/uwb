package com.zhilutec.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ConstantUtil {

    public static final String UPGRADE = "UPGRADE";
    public static final String UPGRADE_TYPE = "upgrade";
    public static final String UPGRADE_QUERY = "query";
    public static final String UPGRADE_RESP = "resp";

    public static final Integer UPGRADE_STAT_SUCC = 0;
    public static final Integer UPGRADE_STAT_RUNN = 1;
    public static final Integer UPGRADE_STAT_PART = 2;
    public static final Integer UPGRADE_STAT_FAIL = 3;
    public static final Integer UPGRADE_STAT_REPEAT = 4;
    public static final Integer UPGRADE_STAT_DEFAULT = 100;
    public static final Integer UPGRADE_MSG_TYPE = 6;
}
