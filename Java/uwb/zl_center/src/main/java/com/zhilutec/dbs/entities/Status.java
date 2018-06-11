package com.zhilutec.dbs.entities;

public class Status {

    private Long tagId;
    private Integer power;
    private Integer heart;
    private Integer type;
    private Integer sos;
    private Integer wristlet;
    private Integer move;
    private Long timestamp;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getHeart() {
        return heart;
    }

    public void setHeart(Integer heart) {
        this.heart = heart;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSos() {
        return sos;
    }

    public void setSos(Integer sos) {
        this.sos = sos;
    }

    public Integer getWristlet() {
        return wristlet;
    }

    public void setWristlet(Integer wristlet) {
        this.wristlet = wristlet;
    }

    public Integer getMove() {
        return move;
    }

    public void setMove(Integer move) {
        this.move = move;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Status{" +
                "tagId=" + tagId +
                ", power=" + power +
                ", heart=" + heart +
                ", type=" + type +
                ", sos=" + sos +
                ", wristlet=" + wristlet +
                ", move=" + move +
                ", timestamp=" + timestamp +
                '}';
    }
}
