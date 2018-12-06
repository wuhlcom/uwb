package com.zhilutec.dbs.entities;

import javax.persistence.Table;

@Table(name = "uwb_fences")
public class Fence  extends Base {
    private static final long serialVersionUID = 1L;
    private String fenceName;
    private String fenceCode;
    private String remark;//` varchar(255) DEFAULT NULL COMMENT '说明',
    private String points;
    private Integer isdel;
    private Integer type;
    private  Long createdAt;

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public String getFenceCode() {
        return fenceCode;
    }

    public void setFenceCode(String fenceCode) {
        this.fenceCode = fenceCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Fence{" +
                "fenceName='" + fenceName + '\'' +
                ", fenceCode='" + fenceCode + '\'' +
                ", remark='" + remark + '\'' +
                ", points='" + points + '\'' +
                ", isdel=" + isdel +
                ", type=" + type +
                ", createdAt=" + createdAt +
                '}';
    }
}
