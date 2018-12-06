package com.zhilutec.common.validators;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;

public class FileValidator {

    public static Result updateVal(JSONObject jsonObject) {
        Long id = jsonObject.getLong("id");
        System.out.println("id：" + id);
        if (RegexUtil.isNull(id))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "Id不能为空");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result swVal(JSONObject jsonObject) {
        Long id = jsonObject.getLong("id");
        System.out.println("id：" + id);
        if (RegexUtil.isNull(id))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "Id不能为空");

        // Integer status = jsonObject.getInteger("status");
        // System.out.println("status：" + status);
        // if (RegexUtil.isNull(status))
        //     return Result.error(ResultCode.PARAMETER_ERR.getCode(), "状态不能为空");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Boolean isDepartmentName(String name) {
        if (RegexUtil.stringCheck(name))
            return true;
        return false;
    }
}
