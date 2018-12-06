/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月18日 下午3:54:32 *
 */
package com.zhilutec.cloud.entity;

import javax.persistence.Table;

/**
 * @author zhilu1234
 */
@Table(name = "zl_gw_user")
public class User extends Base {
    private String username;// `username` varchar(64) NOT NULL COMMENT '用户名',
    private String password;// `password` varchar(128) NOT NULL COMMENT '密码',
    private Integer locked; // 启用 1 禁用 0',
    private Long createdAt; //用户添加日期',
    private Long updatedAt; //更新日期',

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", locked=" + locked +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
