package com.zhilutec.dbs.params;

public class BaseParam {
    private int tagId;
    private int amount;

    public BaseParam() {
        super();
        // TODO Auto-generated constructor stub
    }

    public BaseParam(int tagId, int amount) {
        super();
        this.tagId = tagId;
        this.amount = amount;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BaseParam [tagId=" + tagId + ", amount=" + amount + "]";
    }

}
