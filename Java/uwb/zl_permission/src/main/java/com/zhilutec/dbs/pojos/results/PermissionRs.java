package com.zhilutec.dbs.pojos.results;

import com.zhilutec.dbs.entities.UwbPermission;

import java.util.List;

public class PermissionRs {
    // <id property="uid" column="uid"/>
    //  <result property="username" column="username" jdbcType="VARCHAR"/>
    //  <result property="roleId" column="role_id" jdbcType="BIGINT"/>
    //  <result property="permissionId" column="permission_id" jdbcType="BIGINT"/>
    //  <result property="menuCode" column="menu_code" jdbcType="VARCHAR"/>
    //  <result property="menuName" column="menu_name" jdbcType="VARCHAR"/>

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
