package com.zhilutec.uwb.common.pojo;

public class VersionInfo {
    private String cmdType;
    private String cmdDir;
    private String ancVer;
    private String tagVer;
    private Long ancUpTime=0L;
    private Long tagUpTime=0L;
    private Integer type;

    private String serverIP;
    private Integer serverPort;

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

    public String getAncVer() {
        return ancVer;
    }

    public void setAncVer(String ancVer) {
        this.ancVer = ancVer;
    }

    public String getTagVer() {
        return tagVer;
    }

    public void setTagVer(String tagVer) {
        this.tagVer = tagVer;
    }

    public Long getAncUpTime() {
        return ancUpTime;
    }

    public void setAncUpTime(Long ancUpTime) {
        this.ancUpTime = ancUpTime;
    }

    public Long getTagUpTime() {
        return tagUpTime;
    }

    public void setTagUpTime(Long tagUpTime) {
        this.tagUpTime = tagUpTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "cmdType='" + cmdType + '\'' +
                ", cmdDir='" + cmdDir + '\'' +
                ", ancVer='" + ancVer + '\'' +
                ", tagVer='" + tagVer + '\'' +
                ", ancUpTime=" + ancUpTime +
                ", tagUpTime=" + tagUpTime +
                ", type=" + type +
                ", serverIP='" + serverIP + '\'' +
                ", serverPort=" + serverPort +
                '}';
    }
}
