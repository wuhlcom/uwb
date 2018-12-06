package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Department;

public interface IDepartmentService {
    Department getCache(String departmentCode);
}
