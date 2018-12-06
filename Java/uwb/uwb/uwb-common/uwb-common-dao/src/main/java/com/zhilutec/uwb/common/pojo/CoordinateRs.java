package com.zhilutec.uwb.common.pojo;


import com.zhilutec.uwb.entity.Coordinate;

import java.util.List;

public class CoordinateRs {
    private String personCode;
    private List<Coordinate> coordinates;

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "CoordinateRs{" +
                "personCode='" + personCode + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}

