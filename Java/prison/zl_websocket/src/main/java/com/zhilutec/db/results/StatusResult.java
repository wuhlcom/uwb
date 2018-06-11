package com.zhilutec.db.results;

public class StatusResult extends BaseResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private int tagId;	
	private Integer status;
	private long timestamp;
		
	
	public StatusResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public StatusResult(String code, String name, int tagId, Integer status, long timestamp) {
		super();
		this.code = code;
		this.name = name;
		this.tagId = tagId;
		this.status = status;
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


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	@Override
	public String toString() {
		return "StatusEntity [code=" + code + ", name=" + name + ", tagId=" + tagId + ", status=" + status
				+ ", timestamp=" + timestamp + "]";
	}




}
