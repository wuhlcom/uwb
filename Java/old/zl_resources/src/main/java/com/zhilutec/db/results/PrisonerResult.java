package com.zhilutec.db.results;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhilutec.db.entities.PrisonerEntity;

public class PrisonerResult  extends PrisonerEntity {
	private String name;// '姓名',
	private String code;//   '编号',
	
	@Temporal(TemporalType.DATE)
	@JSONField(format="yyyy-MM-dd")
	private Date birth;//  '出生日期',
	private Integer level=0;// '类别',
	private Integer sex;// '性别 0 男 1女',
	private Long tagId;	// '手环编号',

	private String nativePlace;// '籍贯',
	private String areaCode;// '监仓号',

	private String areaName;
	private String condemnation; //罪名
	private String imprisonment; //刑期
	private String portrait;
	private Integer age=0;
	
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
	
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCondemnation() {
		return condemnation;
	}
	public void setCondemnation(String condemnation) {
		this.condemnation = condemnation;
	}
	public String getImprisonment() {
		return imprisonment;
	}
	public void setImprisonment(String imprisonment) {
		this.imprisonment = imprisonment;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "PrisonerAreaResult [name=" + name + ", code=" + code + ", birth=" + birth + ", level=" + level
				+ ", sex=" + sex + ", tagId=" + tagId + ", nativePlace=" + nativePlace + ", areaCode=" + areaCode
				+ ", areaName=" + areaName + ", condemnation=" + condemnation + ", imprisonment=" + imprisonment
				+ ", portrait=" + portrait + ", age=" + age + "]";
	}
	
	
}
