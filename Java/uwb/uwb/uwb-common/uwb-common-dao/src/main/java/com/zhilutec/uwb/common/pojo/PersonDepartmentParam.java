package com.zhilutec.uwb.common.pojo;

import java.util.List;

public class PersonDepartmentParam {
    private String departmentCode;
    private List<String> persons;

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public List<String> getPersons() {
        return persons;
    }

    public void setPersons(List<String> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "PersonDepartmentParam{" +
                "departmentCode='" + departmentCode + '\'' +
                ", persons=" + persons +
                '}';
    }
}
