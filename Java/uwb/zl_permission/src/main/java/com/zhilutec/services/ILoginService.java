package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface ILoginService {
    String login(HttpServletRequest request, JSONObject requestJson);

    String isLogin(JSONObject requestJson);

    String logout(JSONObject jsonObject);
}
