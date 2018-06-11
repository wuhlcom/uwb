package com.zhilutec.db.results;

public class PointResult {

	private Double posX;
	private Double posY;
	private Double posZ;
	private String posCode;
	private String name;
	private String code;
	private Integer tagId;
	private Integer type;
	
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
	public Integer getTagId() {
		return tagId;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "PointsRs [posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + ", posCode=" + posCode + ", name="
				+ name + ", code=" + code + ", tagId=" + tagId + ", type=" + type + "]";
	}
	
}
