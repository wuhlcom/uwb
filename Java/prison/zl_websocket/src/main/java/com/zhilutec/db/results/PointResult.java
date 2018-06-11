package com.zhilutec.db.results;

public class PointResult extends BaseResult{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private int tagId;
	private double posX;
	private double posY;
	private double posZ;
	private int posCode;
	private long timestamp;
		
	
	public PointResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PointResult(String code, String name, int tagId, int posX, int posY, int posZ, int posCode, long timestamp) {
		super();
		this.code = code;
		this.name = name;
		this.tagId = tagId;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.posCode = posCode;
		this.timestamp = timestamp;
	}

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



	public int getTagId() {
		return tagId;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	public int getPosCode() {
		return posCode;
	}

	public void setPosCode(int posCode) {
		this.posCode = posCode;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	@Override
	public String toString() {
		return "PointEntity [code=" + code + ", name=" + name + ", tagId=" + tagId + ", posX=" + posX + ", posY=" + posY
				+ ", posZ=" + posZ + ", posCode=" + posCode + ", timestamp=" + timestamp + "]";
	}



}
