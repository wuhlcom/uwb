package com.zhilutec.uwb.common.validators;

import com.alibaba.fastjson.JSONObject;

import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;
import com.zhilutec.uwb.util.ConstantUtil;
import com.zhilutec.uwb.util.RegexUtil;

public class DepartmentValidator {

    public static Result addVal(JSONObject jsonObject) {
        String departmentName = jsonObject.getString("departmentName");
        System.out.println("departmentName：" + departmentName);
        if (RegexUtil.isNull(departmentName) || !isDepartmentName(departmentName))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "部门名称格式输入错误");

        String parentCode = jsonObject.getString("parentCode");
        System.out.println("parentCode：" + parentCode);
        if (RegexUtil.isNull(parentCode))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "上级部门不能为空");

        // List persons = jsonObject.getJSONArray("persons");
        // System.out.println("persons：" + persons.toString());

        String remark = jsonObject.getString("remark");
        System.out.println("remark：" + remark);


        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result updateVal(JSONObject jsonObject) {
        String departmentCode = jsonObject.getString("departmentCode");
        System.out.println("departmentCode：" + departmentCode);
        if (RegexUtil.isNull(departmentCode) )
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "部门编号不能为空");

        String departmentName = jsonObject.getString("departmentName");
        System.out.println("departmentName：" + departmentName);
        if (RegexUtil.isNull(departmentName) || !isDepartmentName(departmentName))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "部门名称格式输入错误");

        String parentCode = jsonObject.getString("parentCode");
        System.out.println("parentCode：" + parentCode);
        if (RegexUtil.isNull(parentCode)&&!departmentCode.equals(ConstantUtil.TOP_DEPARTMENT_CODE))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "上级部门不能为空");

        // List persons = jsonObject.getJSONArray("persons");
        // System.out.println("persons：" + persons.toString());

        String remark = jsonObject.getString("remark");
        System.out.println("remark：" + remark);


        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }


    public static Result delVal(JSONObject jsonObject) {
        String departmentCode = jsonObject.getString("departmentCode");
        System.out.println("departmentCode：" + departmentCode);
        if (RegexUtil.isNull(departmentCode))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "未提供部门编号");

        String parentCode = jsonObject.getString("parentCode");
        System.out.println("parentCode：" + parentCode);
        if (RegexUtil.isNull(parentCode))
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "未提供上级部门编号");

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Boolean isDepartmentName(String name) {
        if (RegexUtil.stringCheck(name))
            return true;
        return false;
    }
}
