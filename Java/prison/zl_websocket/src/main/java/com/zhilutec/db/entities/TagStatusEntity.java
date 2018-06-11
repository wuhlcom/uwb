package com.zhilutec.db.entities;

import javax.persistence.Table;

@Table(name="pr_tag_status")
public class TagStatusEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4071307775450126838L;
	private Long tagId;
	private Integer status; //'状态',
	private Long timestamp; 
	private Double posX;  //'x坐标',
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return "TagStatusEntity [tagId=" + tagId + ", status=" + status + ", timestamp=" + timestamp + ", posX=" + posX
				+ ", posY=" + posY + ", posZ=" + posZ + ", posCode=" + posCode + ", position=" + position + "]";
	}
	
	
}
