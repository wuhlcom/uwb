package com.zhilutec.db.results;

public class WarningResult extends BaseResult{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	p.code,p.name,p.tag_id,c.pos_x,c.pos_y,c.pos_z,c.pos_code,c.timestamp  
	private String code;
	private String name;
	private int tagId;	
	private String level;
	private String msg;
	private long timestamp;
	
		
	public WarningResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WarningResult(String code, String name, int tagId, String level, String msg, long timestamp) {
		super();
		this.code = code;
		this.name = name;
		this.tagId = tagId;
		this.level = level;
		this.msg = msg;
		this.timestamp = timestamp;
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

		
	@Override
	public String toString() {
		return "WoringEntity [code=" + code + ", name=" + name + ", tagId=" + tagId + ", level=" + level + ", msg="
				+ msg + ", timestamp=" + timestamp + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
		
	
	

}
