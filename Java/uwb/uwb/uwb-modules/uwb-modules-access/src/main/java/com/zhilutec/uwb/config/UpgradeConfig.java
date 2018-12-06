package com.zhilutec.uwb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**配置升级文件服务器地址*/
@Component
public class UpgradeConfig {

    @Value("${upgrade.ip}")
    private String upgradeIP;

    @Value("${upgrade.port}")
    private Integer upgradePort;

    public String getUpgradeIP() {
        return upgradeIP;
    }

    public void setUpgradeIP(String upgradeIP) {
        this.upgradeIP = upgradeIP;
    }

    public Integer getUpgradePort() {
        return upgradePort;
    }

    public void setUpgradePort(Integer upgradePort) {
        this.upgradePort = upgradePort;
    }


    @Override
    public String toString() {
        return "UpgradeConfig{" +
                "upgradeIP='" + upgradeIP + '\'' +
                ", upgradePort=" + upgradePort +
                '}';
    }
}
