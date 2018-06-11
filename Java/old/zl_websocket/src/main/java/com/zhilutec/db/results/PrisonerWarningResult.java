package com.zhilutec.db.results;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class PrisonerWarningResult implements Serializable{

	private static final long serialVersionUID = 1L;

	private String warningType="";
	private String warningPos="";
	private String currentPos="";
	private Long warningTime=0L;
	public String getWarningType() {
		return warningType;
	}
	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}
	public String getWarningPos() {
		return warningPos;
	}
	public void setWarningPos(String warningPos) {
		this.warningPos = warningPos;
	}
	public String getCurrentPos() {
		return currentPos;
	}
	public void setCurrentPos(String currentPos) {
		this.currentPos = currentPos;
	}

	public Long getWarningTime() {
		return warningTime;
	}
	public void setWarningTime(Long warningTime) {
		this.warningTime = warningTime;
	}
	
	@Override
	public String toString() {					
		JSONObject dataResult = new JSONObject();
		dataResult.put("warningType", warningType);
		dataResult.put("warningPos", currentPos);
		dataResult.put("currentPos", currentPos);
		dataResult.put("warningTime", warningTime);		
		return dataResult.toJSONString();
	}
	
		
}
