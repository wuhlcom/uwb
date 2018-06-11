/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午3:54:32 * 
*/
package com.zhilutec.db.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhilu1234
 *
 */
@Table(name = "pr_prisoners")
public class PrisonerEntity extends BaseEntity {

	private String name;// `name` varchar(32) NOT NULL COMMENT '姓名',
	private String number;// `number` varchar(20) NOT NULL COMMENT '编号',
	private String idcard;// `idcard` varchar(20) NOT NULL COMMENT '身份证号码',
	private Date birth;// `birth` date NOT NULL COMMENT '出生日期',
	private Integer level;// `level` tinyint(1) DEFAULT NULL COMMENT '类别',
	private Integer sex;// `sex` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别 0 男
						// 1女',

	@Column(name = "prisoner_description")
	private String prison_desc;// `prisoner_description` varchar(255) DEFAULT
								// NULL COMMENT '描述',
	private Long created_at;// `created_at` bigint(11) NOT NULL COMMENT '添加时间',
	private String wristband_code;// `wristband_code` varchar(20) NOT NULL
									// COMMENT '手环编号',
	private Long area_id;// `area_id` varchar(11) DEFAULT NULL,
	private String native_place;// `native_place` varchar(255) DEFAULT NULL
								// COMMENT '籍贯',
	private Long cellnum;// `cellnum` int(11) NOT NULL COMMENT '监仓号',
	private Integer status;// `status` tinyint(4) NOT NULL COMMENT '1',
	private Integer isdel;// `isdel` tinyint(1) DEFAULT NULL COMMENT '0 存在 1
							// 不存在',
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getPrison_desc() {
		return prison_desc;
	}
	public void setPrison_desc(String prison_desc) {
		this.prison_desc = prison_desc;
	}
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	public String getWristband_code() {
		return wristband_code;
	}
	public void setWristband_code(String wristband_code) {
		this.wristband_code = wristband_code;
	}
	public Long getArea_id() {
		return area_id;
	}
	public void setArea_id(Long area_id) {
		this.area_id = area_id;
	}
	public String getNative_place() {
		return native_place;
	}
	public void setNative_place(String native_place) {
		this.native_place = native_place;
	}
	public Long getCellnum() {
		return cellnum;
	}
	public void setCellnum(Long cellnum) {
		this.cellnum = cellnum;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PrisonerEntity [name=" + name + ", number=" + number + ", idcard=" + idcard + ", birth=" + birth
				+ ", level=" + level + ", sex=" + sex + ", prison_desc=" + prison_desc + ", created_at=" + created_at
				+ ", wristband_code=" + wristband_code + ", area_id=" + area_id + ", native_place=" + native_place
				+ ", cellnum=" + cellnum + ", status=" + status + ", isdel=" + isdel + "]";
	}

}
