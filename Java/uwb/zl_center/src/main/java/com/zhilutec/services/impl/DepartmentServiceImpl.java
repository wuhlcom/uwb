package com.zhilutec.services.impl;

import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.daos.DepartmentDao;
import com.zhilutec.dbs.entities.Department;
import com.zhilutec.services.IDepartmentService;
import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DepartmentServiceImpl extends IRedisService<Department> implements IDepartmentService {

    @Resource
    DepartmentDao departmentDao;

    @Override
    protected String getRedisKey() {
        return null;
    }

    @Override
    @Transactional
    public void departmentCacheInit() {
        List<Department> departments = departmentDao.getDepartments();
        for (Department department : departments) {
            this.addCache(department);
        }
    }

    @Override
    public void addCache(Department department) {
        String departmentCode = department.getDepartmentCode();
        String key = ConstantUtil.DEPARTMENT_KEY_PRE + ":" + departmentCode;
        this.put(key, departmentCode, department, ConstantUtil.REDIS_DEFAULT_TTL);
    }

    @Override
    public void deleteCache(Department department) {
        String departmentCode = department.getDepartmentCode();
        this.deleteCache(departmentCode);
    }

    @Override
    public void deleteCache(String departmentCode) {
        String key = ConstantUtil.DEPARTMENT_KEY_PRE + ":" + departmentCode;
        this.remove(key, departmentCode);
    }

    @Override
    public Department getCache(String departmentCode) {
        String key = ConstantUtil.DEPARTMENT_KEY_PRE + ":" + departmentCode;
        return this.get(key, departmentCode);
    }
}
