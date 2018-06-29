package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.Department;
import com.zhilutec.services.IDepartmentService;
import com.zhilutec.services.IRedisService;
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
        if(map==null){
            return department;
        }
        String mapStr=JSONObject.toJSONString(map);
        department = JSONObject.parseObject(mapStr, Department.class);
        return department;
    }
}
