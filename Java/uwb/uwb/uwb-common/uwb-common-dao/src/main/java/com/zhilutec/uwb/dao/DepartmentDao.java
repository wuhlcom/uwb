package com.zhilutec.uwb.dao;


import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.Department;

import java.util.List;

public interface DepartmentDao extends MyMapper<Department> {
    Integer countAll();

    List<Department> getDepartments();

    List<Department> getParentDepartments(String departmentCode);

    Integer updateRgt(Integer rgt);

    Integer updateLft(Integer rtg);

    List<Department> getDirectSubDpt(String departmentCode);
}

