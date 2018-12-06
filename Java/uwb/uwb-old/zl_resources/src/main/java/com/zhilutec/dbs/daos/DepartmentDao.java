package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.Department;

import java.util.List;

public interface DepartmentDao extends MyMapper<Department>, BaseDao<Department> {
    Integer countAll();

    List<Department> getDepartments();

    List<Department> getParentDepartments(String departmentCode);

    Integer updateRgt(Integer rgt);

    Integer updateLft(Integer rtg);

    List<Department> getDirectSubDpt(String departmentCode);
}

