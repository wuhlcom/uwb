package com.zhilutec.common.validators;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;


public class CoordinateValidator {
    public static Result coordinatesVal(JSONObject jsonObject) {
        String startTime = jsonObject.getString("startTime");
        System.out.println("startTime：" + startTime);

        if (RegexUtil.isNotNull(startTime)) {
            Long lonStart = Long.valueOf(startTime);
            if (lonStart <= 31420800L) {
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "起始时间范围错误");
            }
        }

        String endTime = jsonObject.getString("endTime");
        System.out.println("endTime：" + endTime);
        if (RegexUtil.isNotNull(endTime)) {
            Long lonEnd = Long.valueOf(endTime);
            if (lonEnd <= 31420800L) {
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "起始时间范围错误");
            }
        }

        if (RegexUtil.isNotNull(startTime) && RegexUtil.isNotNull(endTime)) {
            Long lonStart = Long.valueOf(startTime);
            Long lonEnd = Long.valueOf(endTime);
            if (lonStart > lonEnd) {
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "起始时间必须小于结束时间");
            }
        }

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }
}
