package com.zhilutec.db.entities;

import javax.persistence.Table;

@Table(name="pr_domains")
public class DomainEntity extends BaseEntity{
	private String name;// varchar(64) DEFAULT NULL COMMENT '大区间名',
	private String domainUniqueCode;// varchar(32) DEFAULT NULL COMMENT '大区间编号',
	private String remark; // varchar(255) DEFAULT NULL,
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return "DomainEntity [name=" + name + ", domainUniqueCode=" + domainUniqueCode + ", remark=" + remark + "]";
	}
	  
}
