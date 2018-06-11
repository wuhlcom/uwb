package com.zhilutec.db.entities;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class RedisStatusEnitity implements Serializable{
	 /**
		 *  out  cross status  online  posCode nosignal
		 *  1		1	1		0
			0		1	1		0
			1		0	1		0
			x		x	0		0
			0		0	1		1
		 * out 电子围栏  0 无报警 1有报警 
		 * cross 串仓     0 无报警 1有报警 
		 * nosignal    0 无报警 1有报警 
		 * status 在线离线    0 离线 1在线 
		 * level 报警级别
		 * postCode 最后一次的位置
		 * timestamp 时间
		 */
	private static final long serialVersionUID = -1825939294242623576L;
	private Integer out;
	private Integer cross;
	private Integer nosignal;
	private String level;
	private Integer status;
	private Long timestamp;
	private String posCode;
	private Integer type;
	private Integer op;
	public Integer getOut() {
		return out;
	}
	public void setOut(Integer out) {
		this.out = out;
	}
	public Integer getCross() {
		return cross;
	}
	public void setCross(Integer cross) {
		this.cross = cross;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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
	public String getPosCode() {
		return posCode;
	}
	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}	
	
	public Integer getNosignal() {
		return nosignal;
	}
	public void setNosignal(Integer nosignal) {
		this.nosignal = nosignal;
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
	@Override
	public String toString() {
		return "RedisStatusEnitity [out=" + out + ", cross=" + cross + ", nosignal=" + nosignal + ", level=" + level
				+ ", status=" + status + ", timestamp=" + timestamp + ", posCode=" + posCode + ", type=" + type
				+ ", op=" + op + "]";
	}
}