/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午6:25:29 * 
*/ 
package com.zhilutec.db.daos;

import java.util.List;
import java.util.Map;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.db.entities.UserEntity;

public interface UserDao extends MyMapper<UserEntity> {
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId); 
	
	/**
	 * 根据用户名，查询系统用户
	 */
//	UserEntity queryByAccount(String username);
	
	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);
	
    List<String> findPermissions(String account);
    List<String> findRoles(String account);
}
