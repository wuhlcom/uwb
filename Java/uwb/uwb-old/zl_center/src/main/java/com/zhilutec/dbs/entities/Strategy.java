package com.zhilutec.dbs.entities;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;

@Table(name = "uwb_strategies")
public class Strategy extends Base {

    private static final long serialVersionUID = 1L;

    private String strategyName;//` varchar(128) DEFAULT NULL COMMENT '策略名',
    private String strategyCode;//` varchar(64) DEFAULT NULL COMMENT '策略编号',
    private String fenceCode;//` varchar(64) DEFAULT NULL COMMENT '围栏编号',
    private String remark;//` varchar(255) DEFAULT NULL COMMENT '策略备注',
    private Integer userType;//`type` int(2) DEFAULT NULL COMMENT '策略类型 0 个人策略 1 组策略',
    private Integer available;//` tinyint(2) DEFAULT NULL COMMENT ' 0 不 启用',


    @JsonDeserialize
    @JsonSerialize
    @Temporal(TemporalType.TIME)
    @JSONField(format = "HH:mm:ss")
    private Timestamp startTime;// '策略开始时间',

    @JsonDeserialize
    @JsonSerialize
    @Temporal(TemporalType.TIME)
    @JSONField(format = "HH:mm:ss")
    private Timestamp finishTime;// '策略结束时间',

    private Integer isdel;//'0 删除 1 保留',
    private Long createdAt;
    private Integer level;
    private String strategyUserId;
    private Integer timeType;
    private String timeValue;
    private Integer forbidden;

    @Transient
    private Long beginTime;
    @Transient
    private Long endTime;
    // @Transient
    // private String points;
    @Transient
    private String strategyUser;
    @Transient
    private String fenceName;
    @Transient
    private List timeValues;
    @Transient
    private List parentCodes;

    public List getParentCodes() {
        return parentCodes;
    }

    public void setParentCodes(List parentCodes) {
        this.parentCodes = parentCodes;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyCode() {
        return strategyCode;
    }

    public void setStrategyCode(String strategyCode) {
        this.strategyCode = strategyCode;
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getStrategyUserId() {
        return strategyUserId;
    }

    public void setStrategyUserId(String strategyUserId) {
        this.strategyUserId = strategyUserId;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public Integer getForbidden() {
        return forbidden;
    }

    public void setForbidden(Integer forbidden) {
        this.forbidden = forbidden;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }


    public String getStrategyUser() {
        return strategyUser;
    }

    public void setStrategyUser(String strategyUser) {
        this.strategyUser = strategyUser;
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public List getTimeValues() {
        return timeValues;
    }

    public void setTimeValues(List timeValues) {
        this.timeValues = timeValues;
    }

    @Override
    public String toString() {
        return "Strategy{" +
                "strategyName='" + strategyName + '\'' +
                ", strategyCode='" + strategyCode + '\'' +
                ", fenceCode='" + fenceCode + '\'' +
                ", remark='" + remark + '\'' +
                ", userType=" + userType +
                ", available=" + available +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", isdel=" + isdel +
                ", createdAt=" + createdAt +
                ", level=" + level +
                ", strategyUserId='" + strategyUserId + '\'' +
                ", timeType=" + timeType +
                ", timeValue='" + timeValue + '\'' +
                ", forbidden=" + forbidden +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", strategyUser='" + strategyUser + '\'' +
                ", fenceName='" + fenceName + '\'' +
                ", timeValues=" + timeValues +
                ", parentCodes=" + parentCodes +
                '}';
    }
}
