package com.zhilutec.db.entities;

import javax.persistence.Table;

@Table(name="pr_floors")
public class FloorEntity extends BaseEntity {

	private String name;// ` varchar(32) DEFAULT NULL COMMENT '楼层',
	private String code;// varchar(32) DEFAULT NULL COMMENT '楼层编号',
	private String buildingUniqueCode;// varchar(32) DEFAULT NULL COMMENT
										// '楼栋编号',
	private String floorUniqueCode; // ` varchar(32) DEFAULT NULL,
	private String remark; // varchar(255) DEFAULT NULL,

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

	public String getBuildingUniqueCode() {
		return buildingUniqueCode;
	}

	public void setBuildingUniqueCode(String buildingUniqueCode) {
		this.buildingUniqueCode = buildingUniqueCode;
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

	@Override
	public String toString() {
		return "FloorEntity [name=" + name + ", code=" + code + ", buildingUniqueCode=" + buildingUniqueCode
				+ ", floorUniqueCode=" + floorUniqueCode + ", remark=" + remark + "]";
	}

}
