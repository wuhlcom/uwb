package com.zhilutec.dbs.entities;
import com.zhilutec.dbs.pojos.Person2DptRS;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "uwb_departments")
public class Department extends Base {
    private String departmentName; //` varchar(128) DEFAULT NULL COMMENT '部门名称',
    private String departmentCode;//` varchar(64) DEFAULT NULL COMMENT '部门编号',
    private String remark;//` varchar(255) DEFAULT NULL COMMENT '说明',
    private String parentCode; //` varchar(64) DEFAULT NULL COMMENT '上一级部门',
    private String leaderCode;//` varchar(64) DEFAULT NULL COMMENT '领导编号',
    private String orderNum; //` int(64) DEFAULT NULL COMMENT '排序编号',
    private Integer isdel;
    private Long createdAt;

    @Transient
    private List<Department> children;

    @Transient
    private String tagId;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Transient
    private List<Person2DptRS> persons;

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getLeaderCode() {
        return leaderCode;
    }

    public void setLeaderCode(String leaderCode) {
        this.leaderCode = leaderCode;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
    }

    public List<Person2DptRS> getPersons() {
        return persons;
    }

    public void setPersons(List<Person2DptRS> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentName='" + departmentName + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", remark='" + remark + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", leaderCode='" + leaderCode + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", isdel=" + isdel +
                ", children=" + children +
                '}';
    }
}
