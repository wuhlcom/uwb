package com.zhilutec.dbs.pojos;

import com.zhilutec.dbs.entities.UwbPermission;

import java.util.List;

public class PermissionRs {

    private Long uid;
    private String username;
    private Long roleId;

    List<UwbPermission> permissionList;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<UwbPermission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<UwbPermission> permissionList) {
        this.permissionList = permissionList;
    }

    @Override
    public String toString() {
        return "PermissionRs{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", roleId=" + roleId +
                ", permissionList=" + permissionList +
                '}';
    }
}
