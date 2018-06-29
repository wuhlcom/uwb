package com.zhilutec.dbs.entities;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "uwb_levels")
public class Level extends Base{

    private String levelName;//` varchar(16) DEFAULT NULL COMMENT '职称分类',
    private String levelCode;//` varchar(16) DEFAULT NULL,
    private String parentCode;//` varchar(16) DEFAULT NULL,
    private Integer isdel;
    private String remark;

    @Transient
    private List<Level> children;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Level> getChildren() {
        return children;
    }

    public void setChildren(List<Level> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Level{" +
                "levelName='" + levelName + '\'' +
                ", levelCode='" + levelCode + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", isdel=" + isdel +
                ", remark='" + remark + '\'' +
                ", children=" + children +
                '}';
    }
}
