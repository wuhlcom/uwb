/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月18日 下午6:25:29 *
 */
package com.zhilutec.uwb.dao;


import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.User;

import java.util.Map;

public interface UserDao extends MyMapper<User> {
    User getUserByName(Map<String, Object> map);
}
