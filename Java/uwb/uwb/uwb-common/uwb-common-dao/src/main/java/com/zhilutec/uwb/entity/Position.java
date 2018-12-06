package com.zhilutec.uwb.entity;

import javax.persistence.Table;

@Table(name="uwb_positions")
public class Position extends Base {
    private static final long serialVersionUID = 1L;
    private String positionName;//` varchar(32) DEFAULT NULL,
    private String positionCode;//` varchar(32) DEFAULT NULL,
    private String remark;
    private Integer isdel;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    @Override
    public String toString() {
        return "Position{" +
                "positionName='" + positionName + '\'' +
                ", positionCode='" + positionCode + '\'' +
                ", remark='" + remark + '\'' +
                ", isdel=" + isdel +
                '}';
    }
}
