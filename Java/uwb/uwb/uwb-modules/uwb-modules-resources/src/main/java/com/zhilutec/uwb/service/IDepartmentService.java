package com.zhilutec.uwb.service;

import com.alibaba.fastjson.JSONObject;

import com.zhilutec.uwb.entity.Department;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IDepartmentService {

    //添加部门
    //更新部门中添加的人员
    @Transactional
    String add(JSONObject jsonObject) throws InterruptedException;

    String update(JSONObject jsonObject) throws InterruptedException;

    List<String> getAllParents(List<String> parents, String code);

    List<String> getAllParentCodes(List<String> parents, String code);

    Department getParentTree(List<Department> departments, String parentCode);

    Department getDepartmentsTree();
    String getDepartmentsTreeRs();


    // 更新人员到上级部门
    String delete(JSONObject jsonObject);
    Department getDepartmentById(Long id);
    Department getDepartmentByCode(String code);

    String getDepartments();

    //查询当前部门和所有子部门
    List<Department> getParentSubDpt(String deparmentCode);

    //查询当前部门和所有子部门编号
    List<String> getParentSubDptCodes(String deparmentCode);

    @Transactional
    void departmentCacheInit();

    void addDptCache(Department department);

    // void deleteDptCache(Department department);

    void deleteDptCache(String departmentCode);

}
