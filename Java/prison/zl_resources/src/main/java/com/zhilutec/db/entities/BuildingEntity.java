package com.zhilutec.db.entities;

import javax.persistence.Table;

@Table(name="pr_buildings")
public class BuildingEntity extends BaseEntity {
	private String name; // varchar(32) DEFAULT NULL COMMENT '楼栋名',
	private String code;// varchar(32) DEFAULT NULL COMMENT '楼栋编码',
	private String buildingUniqueCode;// varchar(32) DEFAULT NULL COMMENT '唯一编号',
	private String domainUniqueCode;// varchar(32) DEFAULT NULL COMMENT '大区间编号',
	private String remark;// `remark` varchar(255) DEFAULT NULL,

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

	public String getDomainUniqueCode() {
		return domainUniqueCode;
	}

	public void setDomainUniqueCode(String domainUniqueCode) {
		this.domainUniqueCode = domainUniqueCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "BuildingEntity [name=" + name + ", code=" + code + ", buildingUniqueCode=" + buildingUniqueCode
				+ ", domainUniqueCode=" + domainUniqueCode + ", remark=" + remark + "]";
	}

}
