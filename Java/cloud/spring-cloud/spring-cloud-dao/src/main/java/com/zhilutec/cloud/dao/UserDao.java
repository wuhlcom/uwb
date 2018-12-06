/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月18日 下午6:25:29 *
 */
package com.zhilutec.cloud.dao;

import com.zhilutec.cloud.entity.User;
import com.zhilutec.cloud.mapper.MyMapper;

import java.util.Map;

public interface UserDao extends MyMapper<User> {
    User getUserByName(Map<String, Object> map);
}
