package com.zhilutec.db.entities;

import javax.persistence.Table;

@Table(name = "pr_configs")
public class PrisonerCfgEntity {
	
	private Integer startTime;// int(12) DEFAULT NULL COMMENT '开始时间',
	private Integer endTime; // int(12) DEFAULT NULL COMMENT '结束时间',
	private String prisonerCode;// ` varchar(32) DEFAULT NULL COMMENT '囚徒ID',
	private Integer absenceType;// ` int(20) DEFAULT NULL COMMENT '分配的原因',
	private Integer available;// ` tinyint(1) DEFAULT NULL COMMENT '0 不可用 1 可用',

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public String getPrisonerCode() {
		return prisonerCode;
	}

	public void setPrisonerCode(String prisonerCode) {
		this.prisonerCode = prisonerCode;
	}


	public Integer getAbsenceType() {
		return absenceType;
	}

	public void setAbsenceType(Integer absenceType) {
		this.absenceType = absenceType;
	}


	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}



	@Override
	public String toString() {
		return "PrisonerConfigEntity [startTime=" + startTime + ", endTime=" + endTime + ", prisonerCode="
				+ prisonerCode + ", absenceType=" + absenceType + ", available=" + available + "]";
	}




}
