/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午7:55:33 * 
*/ 
package com.zhilutec.dbs.entities;

import javax.persistence.Entity;

@Entity(name="uwb_user_role")
public class UserRole extends Base {
	  private Long userId;//`user_id` int(20) NOT NULL,
      private Long roleId;	  //`role_id` int(20) NOT NULL,

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
