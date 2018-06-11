package com.zhilutec.db.entities;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="pr_areas")
public class AreaEntity extends BaseEntity {

	private String code; // '编号',
	private String name; // '指定位置',
	
	@Column(name="area_unique_code")
	private String areaCode;// varchar(32) DEFAULT NULL,
	private String floorUniqueCode;// '楼层编号',
	private String remark; // 
	private Integer type; // '0 牢房 1 厕所',

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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getFloorUniqueCode() {
		return floorUniqueCode;
	}

	public void setFloorUniqueCode(String floorUniqueCode) {
		this.floorUniqueCode = floorUniqueCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AreaEntity [code=" + code + ", name=" + name + ", areaCode=" + areaCode + ", floorUniqueCode="
				+ floorUniqueCode + ", remark=" + remark + ", type=" + type + "]";
	}


}
