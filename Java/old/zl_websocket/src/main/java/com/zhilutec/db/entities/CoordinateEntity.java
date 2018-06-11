package com.zhilutec.db.entities;

import javax.persistence.Table;

@Table(name = "pr_coordinates")
public class CoordinateEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1922855296714047188L;
	private Long tagId; // int(16) NOT NULL COMMENT '手环编码',
	private Long timestamp; // int(11) NOT NULL COMMENT '坐标产生时间',
	private Double posX; // decimal(12,8) NOT NULL COMMENT 'x坐标',
	private Double posY; // decimal(12,8) NOT NULL COMMENT 'y坐标',
	private Double posZ; // decimal(12,8) NOT NULL COMMENT 'z坐标',
	private String posCode; // DEFAULT NULL COMMENT '坐标位置',
	private String warningCode;
	private Integer status; //
	private String level; //
	private Integer type; //
	private Integer op;


	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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
	
	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	

	public Integer getOp() {
		return op;
	}

	public void setOp(Integer op) {
		this.op = op;
	}
	

	public String getWarningCode() {
		return warningCode;
	}

	public void setWarningCode(String warningCode) {
		this.warningCode = warningCode;
	}



	@Override
	public String toString() {
		return "CoordinateEntity [tagId=" + tagId + ", timestamp=" + timestamp + ", posX=" + posX + ", posY=" + posY
				+ ", posZ=" + posZ + ", posCode=" + posCode + ", warningCode=" + warningCode + ", status=" + status
				+ ", level=" + level + ", type=" + type + ", op=" + op + "]";
	}

	
}
