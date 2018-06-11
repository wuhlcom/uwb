package com.zhilutec.dbs.pojos;

import com.zhilutec.dbs.entities.Person;

import java.util.List;

public class DepartmentRs {
    private String departmentName; //` varchar(128) DEFAULT NULL COMMENT '部门名称',
    private String departmentCode;//` varchar(64) DEFAULT NULL COMMENT '部门编号',
    private List<Person> persons;

    public DepartmentRs() {
    }

    public DepartmentRs(String departmentName, String departmentCode, List<Person> persons) {
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.persons = persons;
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

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "DepartmentRs{" +
                "departmentName='" + departmentName + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", persons=" + persons +
                '}';
    }
}
