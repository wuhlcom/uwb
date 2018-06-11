package com.zhilutec.db.entities;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

@Table(name = "pr_tag_warning_status")
public class WarningStatusEntity extends BaseEntity{	
	private static final long serialVersionUID = 1922855296714047188L;
	private Long tagId; // '手环编码',
	private Long timestamp; //  '产生时间',
	@Transient
	private Long finishTime; //  恢复时间',
	private Double posX; // 'x坐标',
	private Double posY; // 'y坐标',
	private Double posZ; //  'z坐标',
	private String posCode; // '实际位置编号',
	private String position; // '实际位置',
	private String msg;  //报警消息
	private String warningCode;  //报警消息编号
	private Integer status; //在离线消息状态
	private String level; //报警级别
	private Integer type; //消息类型
	private Integer op;//报警开关
	private String areaName;//预期位置名称
	private String areaCode;//预期位置编号
	private String name;//tagId对应的人名
	private String code;//tagId对应的人员编号
	private Integer state;
	@Transient
	private Integer flag;
	
	public WarningStatusEntity() {
		super();		
	}

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

	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
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

	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	

	public Long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Long finishTime) {
		this.finishTime = finishTime;
	}

	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {					
		JSONObject dataResult = new JSONObject();
		dataResult.put("tagId", tagId);
		dataResult.put("timestamp", timestamp);
		dataResult.put("posX", posX);
		dataResult.put("posY", posY);
		dataResult.put("posZ", posZ);
		dataResult.put("posCode", posCode);
		dataResult.put("position", position);
		dataResult.put("warningCode", warningCode);
		dataResult.put("msg", msg);
		dataResult.put("status", status);
		dataResult.put("level", level);
		dataResult.put("type", type);
		dataResult.put("op", op);
		dataResult.put("areaName", areaName);
		dataResult.put("areaCode", areaCode);
		dataResult.put("name", name);
		dataResult.put("code", code);
		dataResult.put("finishTime", finishTime);
		dataResult.put("state", state);
		dataResult.put("flag", flag);
		return dataResult.toJSONString();
	}
}
