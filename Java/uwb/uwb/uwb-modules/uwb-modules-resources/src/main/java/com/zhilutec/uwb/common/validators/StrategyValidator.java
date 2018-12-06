package com.zhilutec.uwb.common.validators;

import com.alibaba.fastjson.JSONObject;


import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;
import com.zhilutec.uwb.util.RegexUtil;
import com.zhilutec.uwb.util.ZlTimeUtil;

import java.sql.Time;
import java.text.ParseException;
import java.util.List;

public class StrategyValidator {
    public static Result addVal(JSONObject jsonObject) throws ParseException {
        String fenceCode = jsonObject.getString("fenceCode");
        System.out.println("fenceCode：" + fenceCode);
        if (RegexUtil.isNull(fenceCode))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "区域编号输入错误");

        String strategyName = jsonObject.getString("strategyName");
        System.out.println("strategyName：" + strategyName);
        if (RegexUtil.isNull(strategyName) || !isStrategyName(strategyName))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略名不能为空");

        String strategyUserId = jsonObject.getString("strategyUserId");
        System.out.println("strategyUserId：" + strategyUserId);
        if (RegexUtil.isNull(strategyUserId))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略用户ID不正确");

        String userType = jsonObject.getString("userType");
        System.out.println("userType：" + userType);
        if (RegexUtil.isNull(userType))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略用户类型不能为空");

        Integer intUserType = Integer.valueOf(userType);
        if (!(intUserType == 0 || intUserType == 1))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略用户类型不正确");

        String available = jsonObject.getString("available");
        System.out.println("available：" + available);

        if (RegexUtil.isNull(available))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略状态不能为空");

        Integer intAvailable = Integer.valueOf(available);
        if (!(intAvailable == 0 || intAvailable == 1))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略状态不正确");

        String startTime = jsonObject.getString("startTime");
        System.out.println("startTime：" + startTime);
        if (RegexUtil.isNull(startTime))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "起始时间不能为空");

        String finishTime = jsonObject.getString("finishTime");
        System.out.println("finishTime：" + finishTime);
        if (RegexUtil.isNull(finishTime))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "结束时间不能为空");

        Time sTime = ZlTimeUtil.str2Time(startTime);
        Time fTime = ZlTimeUtil.str2Time(finishTime);
        if (sTime.after(fTime))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "结束时间不能小于起始时间");

        String beginTime = jsonObject.getString("beginTime");
        System.out.println("beginTime：" + beginTime);
        if (RegexUtil.isNotNull(beginTime) && ((beginTime.length() != 10) || Integer.valueOf(beginTime) <= 1514735999))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "起始时间不正确");

        String endTime = jsonObject.getString("endTime");
        System.out.println("endTime：" + endTime);
        if (RegexUtil.isNotNull(endTime) && ((endTime.length() != 10) || Integer.valueOf(endTime) <= 1514735999))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "结束时间不正确");

        if (RegexUtil.isNotNull(beginTime) && RegexUtil.isNotNull(endTime) && Integer.valueOf(beginTime) <= Integer.valueOf(endTime))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "结束时间不能小于起始时间");

        String level = jsonObject.getString("level");
        System.out.println("level：" + level);
        if (RegexUtil.isNull(level) || !(Integer.valueOf(level) == 0 || Integer.valueOf(level) == 1 || Integer.valueOf(level) == 2))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "报警类型不正确");

        String timeValue = jsonObject.getString("timeValues");
        System.out.println("timeValues：" + timeValue);
        if (RegexUtil.isNull(timeValue))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "生效时间不正确");

        String remark = jsonObject.getString("remark");
        System.out.println("remark：" + remark);


        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }


    public static Result updateVal(JSONObject jsonObject) {
        String strategyCode = jsonObject.getString("strategyCode");
        System.out.println("strategyCode：" + strategyCode);
        if (RegexUtil.isNull(strategyCode))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略编号不能为空");

        String fenceCode = jsonObject.getString("fenceCode");
        System.out.println("fenceCode：" + fenceCode);
        if (RegexUtil.isNull(fenceCode))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请选择区域");

        String strategyName = jsonObject.getString("strategyName");
        System.out.println("strategyName：" + strategyName);
        if (RegexUtil.isNull(strategyName))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略名不能为空");

        String startTime = jsonObject.getString("startTime");
        System.out.println("startTime：" + startTime);
        if (RegexUtil.isNull(startTime))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请输入策略起始时间");

        String finishTime = jsonObject.getString("finishTime");
        System.out.println("finishTime：" + finishTime);
        if (RegexUtil.isNull(strategyCode))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请输入策略结束时间");

        String strategyUserId = jsonObject.getString("strategyUserId");
        System.out.println("strategyUserId：" + strategyUserId);
        if (RegexUtil.isNull(strategyUserId))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请选择策略用户");

        String level = jsonObject.getString("level");
        System.out.println("level：" + level);
        if (RegexUtil.isNull(level))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请选择策略级别");

        String available = jsonObject.getString("available");
        System.out.println("available：" + available);
        Integer intAvailable = Integer.valueOf(available);
        if (RegexUtil.isNull(available) && (intAvailable != 0 || intAvailable != 1))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略状态参数错误");

        String userType = jsonObject.getString("userType");
        System.out.println("userType：" + userType);
        if (RegexUtil.isNull(userType))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请选择用户类型");

        String timeValue = jsonObject.getString("timeValue");
        System.out.println("timeValue：" + timeValue);
        if (RegexUtil.isNull(userType))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请选择策略生效时间段");

        String forbidden = jsonObject.getString("forbidden");
        System.out.println("forbidden：" + forbidden);
        if (RegexUtil.isNull(forbidden))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请选择策略行为");

        String remark = jsonObject.getString("remark");
        System.out.println("remark：" + remark);

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result swVal(JSONObject jsonObject) {
        String strategyCode = jsonObject.getString("strategyCode");
        System.out.println("strategyCode：" + strategyCode);
        if (RegexUtil.isNull(strategyCode))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略编号不能为空");

        String available = jsonObject.getString("available");
        System.out.println("available：" + available);
        Integer intAvailable = Integer.valueOf(available);
        if (RegexUtil.isNull(available) && (intAvailable != 0 || intAvailable != 1))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "策略状态参数错误");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result delVal(JSONObject jsonObject) {
        List strategyCodes = jsonObject.getJSONArray("strategyCodes");
        System.out.println("strategyCodes：" + strategyCodes);
        if (RegexUtil.isNull(strategyCodes) || strategyCodes.isEmpty())
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "未提供策略编号");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Boolean isStrategyName(String strategyName) {
        if (RegexUtil.stringCheck(strategyName))
            return true;
        return false;
    }
}
