package com.zhilutec.uwb.common.pojo;

public class WarningLevelRs extends WarningRs {
    private Integer level;
    private Integer count;
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "WarningLevelRs{" +
                "level=" + level +
                ", count=" + count +
                '}';
    }
}
