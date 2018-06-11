/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月18日 下午6:56:12 *
 */
package com.zhilutec.dbs.entities;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

@Entity(name = "uwb_roles")
public class Role extends Base {

    private String roleName;//`role_name` varchar(32) NOT NULL COMMENT '角色名',
    private Integer roleLevel;
    private String remark;
    private String roleCode;

    @Transient
    private List<User> users;
    @Transient
    private List<UwbPermission> permissions;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<UwbPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<UwbPermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleName='" + roleName + '\'' +
                ", roleLevel=" + roleLevel +
                ", remark='" + remark + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", users=" + users +
                ", permissions=" + permissions +
                '}';
    }
}
