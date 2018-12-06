package com.zhilutec.uwb.common.pojo;

// "cmd_type":"alarm"
// "cmd_dir":"query"
// "sw":1
public class AlarmInfo {

    private String cmd_type="alarm";
    private String cmd_dir="query";
    private Integer sw;

    public String getCmd_type() {
        return cmd_type;
    }

    public void setCmd_type(String cmd_type) {
        this.cmd_type = cmd_type;
    }

    public String getCmd_dir() {
        return cmd_dir;
    }

    public void setCmd_dir(String cmd_dir) {
        this.cmd_dir = cmd_dir;
    }

    public Integer getSw() {
        return sw;
    }

    public void setSw(Integer sw) {
        this.sw = sw;
    }
}
