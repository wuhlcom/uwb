package com.zhilutec.dbs.pojos;

import com.zhilutec.dbs.entities.Coordinate;

import java.util.List;
import java.util.Map;

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

