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
@Table(name = "pr_users")
public class UserEntity extends BaseEntity {

	private String nickname;// `nickname` varchar(32) NOT NULL COMMENT '登录账户',
	private String password;// `password` varchar(128) NOT NULL COMMENT '密码',
	private String username;// `username` varchar(64) NOT NULL COMMENT '用户姓名',
	private String idcard;// `idcard` varchar(20) NOT NULL COMMENT '身份证号码',

    private String  code;// '编号',
	private Integer sex;// '性别',
	private Date birth;// `birth` date NOT NULL COMMENT '出生日期',
	private Long role_id;// `role_id` int(11) NOT NULL COMMENT '角色ID',

	private String remark;//  NULL COMMENT '描述',
	private String position;// `position` varchar(64) DEFAULT NULL COMMENT '职务',
	private Integer isdel;		// 存在 1 删除',
	private Long created_at; //用户添加日期',
	private Integer available; // 启用 1 禁用',
	
	private String telephone;
	
	private String address;
	
	private String email;
	
	private Long parent_user;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getIsdel() {
		return isdel;
	}

	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}

	public Long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getParent_user() {
		return parent_user;
	}

	public void setParent_user(Long parent_user) {
		this.parent_user = parent_user;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserEntity [nickname=" + nickname + ", password=" + password + ", username=" + username + ", idcard="
				+ idcard + ", code=" + code + ", sex=" + sex + ", birth=" + birth + ", role_id=" + role_id + ", remark="
				+ remark + ", position=" + position + ", isdel=" + isdel + ", created_at=" + created_at + ", available="
				+ available + ", telephone=" + telephone + ", address=" + address + ", email=" + email
				+ ", parent_user=" + parent_user + "]";
	}


}
