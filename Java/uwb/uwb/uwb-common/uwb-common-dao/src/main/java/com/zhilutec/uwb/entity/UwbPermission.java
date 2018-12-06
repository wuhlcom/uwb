/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月18日 下午6:56:12 *
 */
package com.zhilutec.uwb.entity;

import javax.persistence.Entity;


@Entity(name = "uwb_permissions")
public class UwbPermission extends Base {
    private static final long serialVersionUID = 1L;
    private String menuCode;
    private String menuName;
    private String permissionCode; //权限的代码/通配符,对应代码中@RequiresPermissions 的value',
    private String permissionName;
    private String type;
    private String url;
    private Long parentId;
    private Integer requiredPermission;
    private Integer isdel;

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getRequiredPermission() {
        return requiredPermission;
    }

    public void setRequiredPermission(Integer requiredPermission) {
        this.requiredPermission = requiredPermission;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    @Override
    public String toString() {
        return "UwbPermission{" +
                "menuCode='" + menuCode + '\'' +
                ", menuName='" + menuName + '\'' +
                ", permissionCode='" + permissionCode + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", parentId=" + parentId +
                ", requiredPermission=" + requiredPermission +
                ", isdel=" + isdel +
                '}';
    }
}
