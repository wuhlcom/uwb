package com.zhilutec.services;


import com.zhilutec.dbs.entities.Department;
import org.springframework.transaction.annotation.Transactional;

public interface IDepartmentService {
    Department getCache(String departmentCode);
}
