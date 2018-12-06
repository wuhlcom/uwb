package com.zhilutec.uwb.common.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.sql.Time;
import java.util.List;

public class RedisPolicy implements Serializable {
    private static final long serialVersionUID = 1L;
    private String strategyName;
    private String strategyCode;
    private String fenceCode;

    @Temporal(TemporalType.TIME)
    @JSONField(format = "HH:mm:ss")
    private Time startTime;// '策略开始时间',

    @Temporal(TemporalType.TIME)
    @JSONField(format = "HH:mm:ss")
    private Time finishTime;// '策略结束时间',

    private Integer level;
    private String strategyUserId;
    private Integer timeType;
    private Integer forbidden;
    private List timeValues;
    private Integer userType;
    private Integer available;


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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Time finishTime) {
        this.finishTime = finishTime;
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

    public Integer getForbidden() {
        return forbidden;
    }

    public void setForbidden(Integer forbidden) {
        this.forbidden = forbidden;
    }

    public List getTimeValues() {
        return timeValues;
    }

    public void setTimeValues(List timeValues) {
        this.timeValues = timeValues;
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

    @Override
    public String toString() {
        return "RedisPolicy{" +
                "strategyName='" + strategyName + '\'' +
                ", strategyCode='" + strategyCode + '\'' +
                ", fenceCode='" + fenceCode + '\'' +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", level=" + level +
                ", strategyUserId='" + strategyUserId + '\'' +
                ", timeType=" + timeType +
                ", forbidden=" + forbidden +
                ", timeValues=" + timeValues +
                ", userType=" + userType +
                ", available=" + available +
                '}';
    }
}
