package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface ILoginService {
    String login(HttpServletRequest request, JSONObject requestJson);

    String loginStatus(JSONObject requestJson);

    String loginStatus(String token);

    String logout(JSONObject jsonObject);
}
