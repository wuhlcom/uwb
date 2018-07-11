package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.pojos.UpgradeInfo;
import io.netty.channel.Channel;

import java.util.Map;

public interface IUpgradeService  {

    Map<String, Channel> getActiveChannel();

    String getVersion() throws InterruptedException;

    String upgrade(JSONObject jsonObject) throws InterruptedException;

    void initUpgradeStatus();

    void updateUpgradeStatus(UpgradeInfo upgradeInfo);
}
