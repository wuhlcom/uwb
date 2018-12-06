package com.zhilutec.uwb.common.validators;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;
import com.zhilutec.uwb.util.RegexUtil;

import java.text.ParseException;

public class WarningValidator {
    public static Result statisticsVal(JSONObject jsonObject) throws ParseException {
        String limit = jsonObject.getString("limit");
        System.out.println("limit：" + limit);
        if (RegexUtil.isNull(limit)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "人员数量限制不能为空");
        }

        if (RegexUtil.isNull(limit)) {
            Integer intLimit = Integer.valueOf(limit);
            if (intLimit <= 0 || intLimit >= 20)
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "人员数量限制超出范围");
        }

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }


}
