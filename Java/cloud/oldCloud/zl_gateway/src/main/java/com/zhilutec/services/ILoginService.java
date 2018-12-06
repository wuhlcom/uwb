package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;

public interface ILoginService {
    String login(JSONObject requestJson);

    String loginStatus(JSONObject requestJson);

    String loginStatus(String token);

    String logout(JSONObject jsonObject);
}
