package com.zhilutec.common.result;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public Result() {
        put("errcode", ResultCode.SUCCESS.getCode());
    }

    public static Result error() {
        return error(ResultCode.UNKNOW_ERR.getCode(), "未知异常，请联系管理员");
    }

    public static Result error(String msg) {
        return error(ResultCode.UNKNOW_ERR.getCode(), msg);
    }

    public static Result error(int code, String msg) {
        Result r = new Result();
        r.put("errcode", code);
        r.put("msg", msg);
        return r;
    }

    public static Result error(ResultCode result) {
        Result r = new Result();
        r.put("errcode", result.getCode());
        r.put("msg", result.getErrmsg());
        return r;
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put("msg", msg);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok(int code, String msg) {
        Result r = new Result();
        r.put("errcode", code);
        r.put("msg", msg);
        return r;
    }

    public static Result ok(Object obj) {
        Result r = new Result();
        r.put("result", obj);
        return r;
    }

    public static Result ok(int code, Object obj) {
        Result r = new Result();
        r.put("errcode", code);
        r.put("result", obj);
        return r;
    }

    public static Result ok(ResultCode resultCode) {
        Result r = new Result();
        r.put("errcode", resultCode.getCode());
        r.put("msg", resultCode.getErrmsg());
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}