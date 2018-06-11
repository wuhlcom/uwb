/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月18日 下午6:25:29 *
 */
package com.zhilutec.dbs.daos;

import java.util.Map;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.User;

public interface UserDao extends MyMapper<User> {
    User getUserByName(Map<String, Object> map);
}
