package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.dbs.pojos.DepartmentRs;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IPersonService {


    String getPersonsInTopDepartment();

    List<DepartmentRs> getPersonsByGroup();

    @Transactional
    String getPersonAndDepartmentRs(JSONObject jsonObject);

    //添加人员
    @Transactional
    String add(JSONObject jsonObject) throws InterruptedException;

    //添加人员
    @Transactional
    String update(JSONObject jsonObject) throws InterruptedException;

    String delete(JSONObject jsonObject);


    List<Person> getPersonByDepartment(String departmentCode);

    Person getPersonByCode(String code);

    Person getPersonById(Long id);

    String getPersons();

    //获取指定部门组下的所有人员
    List<Person> getPersonsByDeparments(String departmentCode);

    @Transactional
    void personCacheInit();

    //hash将人员tag和名字添加到缓存中
    void addPersonCache(Long tagId, Person person);

    //hash删除缓存
    void delPersonCache(Long tagId);
}
