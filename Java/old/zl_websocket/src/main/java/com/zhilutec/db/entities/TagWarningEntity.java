package com.zhilutec.db.entities;

import javax.persistence.Table;

@Table(name = "pr_tag_warnings")
public class TagWarningEntity extends BaseEntity {

	private static final long serialVersionUID = -6360584152031413454L;
	private Long tagId; // 
	private String warningCode; // 
	private String level; // 
	private Long timestamp;// 
	private Integer op;
	private Double posX; // x坐标',
	private Double posY; //  'y坐标',
	private Double posZ; // 'z坐标',
	private String posCode; // '坐标位置',
	private String position;

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	
	

	public String getWarningCode() {
		return warningCode;
	}

	public void setWarningCode(String warningCode) {
		this.warningCode = warningCode;
	}


	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	

	public Integer getOp() {
		return op;
	}

	public void setOp(Integer op) {
		this.op = op;
	}
	

	public Double getPosX() {
		return posX;
	}

	public void setPosX(Double posX) {
		this.posX = posX;
	}

	public Double getPosY() {
		return posY;
	}

	public void setPosY(Double posY) {
		this.posY = posY;
	}

	public Double getPosZ() {
		return posZ;
	}

	public void setPosZ(Double posZ) {
		this.posZ = posZ;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "TagWarningEntity [tagId=" + tagId + ", warningCode=" + warningCode + ", level=" + level + ", timestamp="
				+ timestamp + ", op=" + op + ", posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + ", posCode="
				+ posCode + ", position=" + position + "]";
	}

	
}
