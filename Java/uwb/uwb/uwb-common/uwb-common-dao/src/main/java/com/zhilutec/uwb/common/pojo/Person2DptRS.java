package com.zhilutec.uwb.common.pojo;

public class Person2DptRS {

    private String departmentName; //将人员的personName属性改成departmentName
    private String departmentCode;  //将人员的personCode属性改成departmentCode
    private String tagId; //将tagId以String类型来获取,解决tagId为空返回值没有此字段的问题

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
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
                "departmentName='" + departmentName + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", tagId='" + tagId + '\'' +
                '}';
    }
}
