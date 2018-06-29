package com.zhilutec.common.validators;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketValidator {

    protected final static Logger logger = LoggerFactory.getLogger(WebsocketValidator.class);


     public static Result wsTypeVal(String jsonStr) {
        if (RegexUtil.isNull(jsonStr))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求参数为空");

        JSONObject paramJson = null;
        try {
            paramJson = JSON.parseObject(jsonStr);
        } catch (Exception e) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求参数格式错误");
        }

        Integer wsType = paramJson.getInteger("type");
        if (!isWsType(wsType)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求类型错误");
        }

        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static boolean isWsType(Integer type) {
        if (RegexUtil.isNotNull(type)) {
            if (type.intValue() == 1 || type.intValue() != 2 || type.intValue() != 3) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
