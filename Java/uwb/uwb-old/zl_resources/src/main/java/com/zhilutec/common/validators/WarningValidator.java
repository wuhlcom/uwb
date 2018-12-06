package com.zhilutec.common.validators;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.ZlTimeUtil;

import java.sql.Time;
import java.text.ParseException;
import java.util.List;

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
