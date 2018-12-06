package com.zhilutec.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;

public class UpgradeValidator {

    public static Result upgradeVal(JSONObject jsonObject) {
        String cmdDir = jsonObject.getString("cmdDir");
        System.out.println("cmdDir：" + cmdDir);
        if (RegexUtil.isNull(cmdDir))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求方不能为空");

        String cmdType = jsonObject.getString("cmdType");
        System.out.println("cmdType：" + cmdType);
        if (RegexUtil.isNull(cmdDir))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求类型不能为空");

        String downPath = jsonObject.getString("downPath");
        System.out.println("downPath：" + downPath);
        if (RegexUtil.isNull(downPath))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "文件路径不能为空");


        String name = jsonObject.getString("name");
        System.out.println("name：" + name);
        if (RegexUtil.isNull(name))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "版本名不能为空");

        // String serverIP = jsonObject.getString("serverIP");
        // System.out.println("serverIP：" + serverIP);
        // if (RegexUtil.isNull(serverIP))
        //     return Result.error(ResultCode.PARAMETER_ERR.getCode(), "文件服务器地址不能为空");
        //
        //
        // String serverPort = jsonObject.getString("serverPort");
        // System.out.println("serverPort：" + serverPort);
        // if (RegexUtil.isNull(name))
        //     return Result.error(ResultCode.PARAMETER_ERR.getCode(), "文件服务器端口不能为空");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }


    public static Boolean isDepartmentName(String name) {
        if (RegexUtil.stringCheck(name))
            return true;
        return false;
    }
}
