/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午7:59:42 * 
*/ 
package com.zhilutec.db.entities;

import javax.persistence.Entity;

@Entity(name="pr_role_resource")
public class RoleResourceEntity extends BaseEntity {
	  private Long user_id;//`user_id` int(20) NOT NULL COMMENT '用户ID',
	  private Long resource_id; //`resource_id` int(20) NOT NULL COMMENT '资源ID',
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Long getResource_id() {
		return resource_id;
	}
	public void setResource_id(Long resource_id) {
		this.resource_id = resource_id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoleResourceEntity [user_id=" + user_id + ", resource_id=" + resource_id + "]";
	}
	  
}
