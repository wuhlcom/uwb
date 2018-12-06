package com.zhilutec.cloud.common.validators;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.cloud.util.ConstantUtil;
import com.zhilutec.cloud.util.RegexUtil;
import com.zhilutec.cloud.util.result.Result;
import com.zhilutec.cloud.util.result.ResultCode;


public class RouteValidator {

    public static Result add(JSONObject jsonObject) {
        // {"routeId":"uwb-producer2","route":"/uwb/producer/**","routeType":"Path","url":"http://192.168.10.196:11002","order":0,"available":1,"urlType":"HTTP"}
        String routeId = jsonObject.getString("routeId");
        System.out.println("routeId：" + routeId);
        if (RegexUtil.isNull(routeId)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "路由ID不能为空");
        }

        String route = jsonObject.getString("route");
        System.out.println("route：" + route);
        if (RegexUtil.isNull(route)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "接口地址不能为空");
        }

        String routeType = jsonObject.getString("routeType");
        System.out.println("routeType：" + routeType);
        if (RegexUtil.isNull(routeType)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "路由类型不能为空");
        }

        String url = jsonObject.getString("url");
        System.out.println("url：" + url);
        if (RegexUtil.isNull(url)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "路由地址不能为空");
        }

        String urlType = jsonObject.getString("urlType");
        System.out.println("urlType：" + urlType);
        if (RegexUtil.isNull(urlType)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "路由地址类型不能为空");
        } else {
            if (!(urlType.equals(ConstantUtil.GW_URI_LB) || urlType.equals(ConstantUtil.GW_URI_HTTP))) {
                return Result.error(ResultCode.PARAMETER_ERR.getCode(), "路由地址类型不正确");
            }
        }

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }

    public static Result update(JSONObject jsonObject) {
        String routeId = jsonObject.getString("routeId");
        System.out.println("routeId：" + routeId);
        if (RegexUtil.isNull(routeId)) {
            return Result.error(ResultCode.PARAMETER_ERR.getCode(), "路由ID不能为空");
        }

        System.out.println("验证通过");
        return Result.ok(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getErrmsg());
    }
}