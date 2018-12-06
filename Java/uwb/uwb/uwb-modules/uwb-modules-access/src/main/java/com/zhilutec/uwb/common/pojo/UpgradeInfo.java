package com.zhilutec.uwb.common.pojo;

import java.util.List;

public class UpgradeInfo {
    private String cmdType;
    private String cmdDir;
    private Integer upStatus;
    private Integer type;
    private List upResult;

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getCmdDir() {
        return cmdDir;
    }

    public void setCmdDir(String cmdDir) {
        this.cmdDir = cmdDir;
    }

    public Integer getUpStatus() {
        return upStatus;
    }

    public void setUpStatus(Integer upStatus) {
        this.upStatus = upStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List getUpResult() {
        return upResult;
    }

    public void setUpResult(List upResult) {
        this.upResult = upResult;
    }

    @Override
    public String toString() {
        return "UpgradeInfo{" +
                "cmdType='" + cmdType + '\'' +
                ", cmdDir='" + cmdDir + '\'' +
                ", upStatus=" + upStatus +
                ", type=" + type +
                ", upResult=" + upResult +
                '}';
    }
}
