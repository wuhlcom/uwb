/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月24日 下午6:49:05 *
 */
package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.dbs.daos.PermissionDao;
import com.zhilutec.dbs.entities.UwbPermission;
import com.zhilutec.dbs.pojos.PermissionRs;
import com.zhilutec.services.IPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionServiceImpl implements IPermissionService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PermissionDao permissionDao;

    /**
     * 根据用户名获取菜单权限
     */
    @Override
    public String getUserPermissions(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        PermissionRs permission = permissionDao.getPermissionByUser(jsonObject);
        if (permission == null) {
            return Result.error("未找到相应权限信息").toJSONString();
        } else {
            List<String> menus = new ArrayList<>();
            List<UwbPermission> permissions = permission.getPermissionList();
            for (UwbPermission perm : permissions) {
                menus.add(perm.getMenuCode());
            }

            map.put("uid", permission.getUid());
            map.put("roleId", permission.getRoleId());
            map.put("username", permission.getUsername());
            map.put("menus", menus);
            return Result.ok(ResultCode.SUCCESS.getCode(), map).toJSONString();
        }
    }

    /**
     * 根据用户名获取菜单权限
     */
    @Override
    public List<String> getUserMenus(JSONObject jsonObject) {
        PermissionRs permission = permissionDao.getPermissionByUser(jsonObject);
        List<String> menus = new ArrayList<>();
        if (permission != null) {
            List<UwbPermission> permissions = permission.getPermissionList();
            for (UwbPermission perm : permissions) {
                menus.add(perm.getMenuCode());
            }
        }
        return menus;
    }

}