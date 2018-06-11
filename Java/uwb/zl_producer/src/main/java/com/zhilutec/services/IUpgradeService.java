package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.pojos.UpgradeInfo;

public interface IUpgradeService  {
    String getVersion() throws InterruptedException;

    String upgrade(JSONObject jsonObject) throws InterruptedException;

    void updateUpgradeStatus(String key, String field, UpgradeInfo upgradeInfo);

    void initUpgradeStatus();
}
