package com.zhilutec.uwb.common.pojo;

public class PersonRs {

    private String personName; //将人员的personName属性改成departmentName
    private String personCode;  //将人员的personCode属性改成departmentCode
    private String tagId; //将tagId以String类型来获取,解决tagId为空返回值没有此字段的问题

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "PersonRs{" +
                "personName='" + personName + '\'' +
                ", personCode='" + personCode + '\'' +
                ", tagId='" + tagId + '\'' +
                '}';
    }
}
