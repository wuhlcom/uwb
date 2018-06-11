package com.zhilutec.dbs.pojos;

public class PersonWristbandRS {

    private String personName;
    private String personCode;
    private Integer tagId;

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

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "PersonWristbandRS{" +
                "personName='" + personName + '\'' +
                ", personCode='" + personCode + '\'' +
                ", tagId=" + tagId +
                '}';
    }
}
