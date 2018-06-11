/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午7:55:33 * 
*/ 
package com.zhilutec.db.entities;

import javax.persistence.Entity;

@Entity(name="pr_role_user")
public class RoleUserEntity extends BaseEntity {
	  private Long user_id;//`user_id` int(20) NOT NULL,
      private Long role_id;	  //`role_id` int(20) NOT NULL,
      
	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoleUser [user_id=" + user_id + ", role_id=" + role_id + "]";
	}
      
}
