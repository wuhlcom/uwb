package com.zhilutec.db.results;

public class StatusResult {

	private int amount;	
	private String code;
	private String name;
	private int tagId;	
	private Integer status;
	private long msg;
	private int type;
	private int level;
	private int absenceType;
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public long getMsg() {
		return msg;
	}
	public void setMsg(long msg) {
		this.msg = msg;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getAbsenceType() {
		return absenceType;
	}
	public void setAbsenceType(int absenceType) {
		this.absenceType = absenceType;
	}
	@Override
	public String toString() {
		return "StatusResult [amount=" + amount + ", code=" + code + ", name=" + name + ", tagId=" + tagId + ", status="
				+ status + ", msg=" + msg + ", type=" + type + ", level=" + level + ", absenceType=" + absenceType
				+ "]";
	}
		
	


	
}
