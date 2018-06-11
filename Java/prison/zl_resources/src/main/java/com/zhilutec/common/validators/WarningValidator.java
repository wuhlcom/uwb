package com.zhilutec.common.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;

/*** 参数校验 ***/
public class WarningValidator {
	public final static Logger logger = LoggerFactory.getLogger(WarningValidator.class);

	public static Result warningQueryVal(String jsonStr) {
		if(RegexUtil.isNull(jsonStr))
			return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求参数为空");

		JSONObject paramJson = null;
		try {
			paramJson = JSON.parseObject(jsonStr);
		} catch (Exception e) {
			return Result.error(ResultCode.PARAMETER_ERR.getCode(), "请求参数格式错误");
		}

		boolean isAreaCode = PublicValidator.isAreaCode(paramJson.getString("areaCode"));
		if (!isAreaCode)
			return Result.error(ResultCode.PARAMETER_ERR.getCode(), "监仓编号格式错误");

		String order = paramJson.getString("order");
		if (!PublicValidator.isOrder(order))
			return Result.error(ResultCode.PARAMETER_ERR.getCode(), "排序类型错误");

		return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
	}
}
