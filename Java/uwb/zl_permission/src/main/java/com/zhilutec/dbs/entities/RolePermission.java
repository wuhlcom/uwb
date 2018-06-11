/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午7:51:39 * 
*/ 
package com.zhilutec.dbs.entities;

import javax.persistence.Entity;

@Entity(name="uwb_role_permission")
public class RolePermission extends Base {
	private String roleId;
	private String permissionId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "roleId='" + roleId + '\'' +
                ", permissionId='" + permissionId + '\'' +
                '}';
    }
}
