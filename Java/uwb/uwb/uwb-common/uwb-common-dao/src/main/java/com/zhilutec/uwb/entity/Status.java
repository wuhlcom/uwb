package com.zhilutec.uwb.entity;

import javax.persistence.Table;

@Table(name="uwb_status")
public class Status {
    private static final long serialVersionUID = 1L;
    private String personName;
    private Long tagId;
    private Integer power;
    private Integer heart;
    private Integer type;
    private Integer sos;
    private Integer wristlet;
    private Integer move;
    private Long timestamp;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Status status = (Status) o;

        if (personName != null ? !personName.equals(status.personName) : status.personName != null) return false;
        if (tagId != null ? !tagId.equals(status.tagId) : status.tagId != null) return false;
        if (power != null ? !power.equals(status.power) : status.power != null) return false;
        if (heart != null ? !heart.equals(status.heart) : status.heart != null) return false;
        if (type != null ? !type.equals(status.type) : status.type != null) return false;
        if (sos != null ? !sos.equals(status.sos) : status.sos != null) return false;
        if (wristlet != null ? !wristlet.equals(status.wristlet) : status.wristlet != null) return false;
        if (move != null ? !move.equals(status.move) : status.move != null) return false;
        return timestamp != null ? timestamp.equals(status.timestamp) : status.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = personName != null ? personName.hashCode() : 0;
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        result = 31 * result + (power != null ? power.hashCode() : 0);
        result = 31 * result + (heart != null ? heart.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (sos != null ? sos.hashCode() : 0);
        result = 31 * result + (wristlet != null ? wristlet.hashCode() : 0);
        result = 31 * result + (move != null ? move.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Status{" +
                "personName='" + personName + '\'' +
                ", tagId=" + tagId +
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
