package com.zhilutec.uwb.common.pojo;


public class WarningRs {
    private String personCode;
    private String personName;
    private Integer amount = 0;
    private Integer urgency = 0;
    private Integer common = 0;
    private Integer tip = 0;

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getUrgency() {
        return urgency;
    }

    public void setUrgency(Integer urgency) {
        this.urgency = urgency;
    }

    public Integer getCommon() {
        return common;
    }

    public void setCommon(Integer common) {
        this.common = common;
    }

    public Integer getTip() {
        return tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "WarningRs{" +
                "personCode='" + personCode + '\'' +
                ", personName='" + personName + '\'' +
                ", amount=" + amount +
                ", urgency=" + urgency +
                ", common=" + common +
                ", tip=" + tip +
                '}';
    }
}
