package com.zhilutec.db.results;

public class WarningStatusResult {
	private Long id;
	private String level;
	private String warningCode;
	private String msg;	
	private Integer tagId;
	private Long timestamp;
	private Long finishTime;
	private String position;
	private Integer state;
	private String name;
	private String code;
	private Integer type;
	private String remark;
	private String areaName;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getTagId() {
		return tagId;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getWarningCode() {
		return warningCode;
	}
	public void setWarningCode(String warningCode) {
		this.warningCode = warningCode;
	}
	public Long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Long finishTime) {
		this.finishTime = finishTime;
	}
		
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
	public String toString() {
		return "WarningStatusResult{" +
				"id=" + id +
				", level='" + level + '\'' +
				", warningCode='" + warningCode + '\'' +
				", msg='" + msg + '\'' +
				", tagId=" + tagId +
				", timestamp=" + timestamp +
				", finishTime=" + finishTime +
				", position='" + position + '\'' +
				", state=" + state +
				", name='" + name + '\'' +
				", code='" + code + '\'' +
				", type=" + type +
				", remark='" + remark + '\'' +
				'}';
	}


}
