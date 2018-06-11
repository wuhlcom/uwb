package com.zhilutec.common.validators;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketValidator {

    protected final static Logger logger = LoggerFactory.getLogger(WebsocketValidator.class);

    /**首页请求参数检验*/
    public static Result wsAreasVal(String jsonStr) {
        return wsTypeVal(jsonStr);
    }

    /**监仓请求参数检验*/
    public static Result wsAreaVal(String jsonStr) {
        Result rs = wsTypeVal(jsonStr);
        Integer errcode = (Integer) rs.get("errcode");
        if (errcode.intValue() == ResultCode.SUCCESS.getCode()) {
            JSONObject paramJson = JSON.parseObject(jsonStr);
            boolean isAreaCode = PublicValidator.isAreaCode(paramJson.getString("areaCode"));
            if (!isAreaCode)
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "监仓编号格式错误");
        }
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    /**囚犯请求参数检验*/
    public static Result wsPrisonerVal(String jsonStr) {
        Result rs = wsTypeVal(jsonStr);
        String tagId = (String) rs.get("tagId");
        if (isTagid(tagId)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "TagId格式错误");
        }
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result wsWarningVal(String jsonStr){
        if (RegexUtil.isNull(jsonStr))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求参数为空");

        JSONObject paramJson = null;
        try {
            paramJson = JSON.parseObject(jsonStr);
        } catch (Exception e) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求参数格式错误");
        }

        Integer wsType = paramJson.getInteger("type");
        if (wsType.intValue()==3) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求类型错误");
        }

        Integer wsDataType = paramJson.getInteger("dataType");
        if (wsDataType.intValue()==4) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求数据类型错误");
        }
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

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

        Integer wsDataType = paramJson.getInteger("dataType");
        if (!isWsDataType(wsDataType)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求数据类型错误");
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

    public static boolean isWsDataType(Integer wsDataType) {
        if (wsDataType!=null) {
            if (wsDataType.intValue() == 0 || wsDataType.intValue() == 1 || wsDataType.intValue() == 2 || wsDataType.intValue() == 3 || wsDataType.intValue() == 4) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean isTagid(String tagId) {
        if (RegexUtil.isNotNull(tagId)) {
            if (RegexUtil.isInteger(tagId)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


}
