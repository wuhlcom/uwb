/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午6:56:12 * 
*/ 
package com.zhilutec.db.entities;

import javax.persistence.Entity;

@Entity(name="pr_roles")
public class RoleEntity extends BaseEntity  {
	
	  private String role_name;//`role_name` varchar(32) NOT NULL COMMENT '角色名',
	  private Integer level;//`level` bit(1) DEFAULT NULL COMMENT '角色级别',
	  private String role_description;//`role_description` varchar(255) DEFAULT NULL COMMENT '角色描述',
	  private String role_code;
	  
	  
	public String getRole_name() {
		return role_name;
	}


	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}


	public Integer getLevel() {
		return level;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}


	public String getRole_description() {
		return role_description;
	}


	public void setRole_description(String role_description) {
		this.role_description = role_description;
	}
    
	 

	public String getRole_code() {
		return role_code;
	}


	public void setRole_code(String role_code) {
		this.role_code = role_code;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoleEntity [role_name=" + role_name + ", level=" + level + ", role_description=" + role_description
				+ ", role_code=" + role_code + "]";
	}
	  
	  
}
