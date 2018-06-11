package com.zhilutec.db.results;

import java.io.Serializable;

public class BaseResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private int tagId;
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
	@Override
	public String toString() {
		return "BaseResultEntity [code=" + code + ", name=" + name + ", tagId=" + tagId + "]";
	}
	
}
