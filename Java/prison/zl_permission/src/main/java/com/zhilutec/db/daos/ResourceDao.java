/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年10月18日 下午6:25:29 * 
*/ 
package com.zhilutec.db.daos;

import java.util.List;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.db.entities.ResourceEntity;

public interface ResourceDao extends MyMapper<ResourceEntity>,BaseDao<ResourceEntity> {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<ResourceEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<ResourceEntity> queryNotButtonList();
	
	/**
	 * 查询用户的权限列表
	 */
	List<ResourceEntity> queryUserList(Long userId);
	
	public List<ResourceEntity> listAllPermissions();
	
    List<String> findRoles(String account);
}
