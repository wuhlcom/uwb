package com.zhilutec.dbs.pojos;

public class Circle {
    private Point bullseye;
    private Double r;

    public Circle() {
    }

    public Circle(Point bullseye, Double radius) {
        this.bullseye = bullseye;
        this.r = r;
    }

    public Point getBullseye() {
        return bullseye;
    }

    public void setBullseye(Point bullseye) {
        this.bullseye = bullseye;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "bullseye=" + bullseye +
                ", r=" + r +
                '}';
    }
}
