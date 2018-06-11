package com.zhilutec.services;


import com.zhilutec.dbs.entities.Department;
import org.springframework.transaction.annotation.Transactional;

public interface IDepartmentService {


    @Transactional
    void departmentCacheInit();

    void addCache(Department department);

    void deleteCache(Department department);

    void deleteCache(String departmentCode);

    Department getCache(String departmentCode);
}
