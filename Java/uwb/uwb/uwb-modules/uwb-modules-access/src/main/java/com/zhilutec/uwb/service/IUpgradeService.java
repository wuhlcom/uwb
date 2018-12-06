package com.zhilutec.uwb.service;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.Channel;

import java.util.Map;
import com.zhilutec.uwb.common.pojo.UpgradeInfo;

public interface IUpgradeService  {

    Map<String, Channel> getActiveChannel();

    String getVersion() throws InterruptedException;

    String upgrade(JSONObject jsonObject) throws InterruptedException;

    void initUpgradeStatus();

    void updateUpgradeStatus(UpgradeInfo upgradeInfo);
}
