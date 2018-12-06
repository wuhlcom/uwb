package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.entity.Department;
import com.zhilutec.uwb.service.IDepartmentService;
import com.zhilutec.uwb.service.IRedisService;
import com.zhilutec.uwb.util.ConstantUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements IDepartmentService {

    @Resource
    IRedisService redisService;

    @Override
    public Department getCache(String departmentCode) {
        Department department = null;
        String key = redisService.genRedisKey(ConstantUtil.DEPARTMENT_KEY_PRE, departmentCode);
        Map map = redisService.hashGetMap(key);
        if (map == null) {
            return department;
        }
        String mapStr = JSONObject.toJSONString(map);
        department = JSONObject.parseObject(mapStr, Department.class);
        return department;
    }
}
