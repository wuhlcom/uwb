package com.zhilutec.uwb.common.validators;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;
import com.zhilutec.uwb.util.RegexUtil;


public class PersonValidator {

    public static Result addVal(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        System.out.println("name：" + name);
        if (RegexUtil.isNull(name) || !isDepartmentName(name))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "人名格式输入错误");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result getVal(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        System.out.println("name：" + name);
        if (!isDepartmentName(name))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "人名格式输入错误");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result updateVal(JSONObject jsonObject) {
        Long id = jsonObject.getLong("id");
        System.out.println("id：" + id);
        if (RegexUtil.isNull(id))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "Id不能为空");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Boolean isDepartmentName(String name) {
        if (RegexUtil.stringCheck(name))
            return true;
        return false;
    }
}
