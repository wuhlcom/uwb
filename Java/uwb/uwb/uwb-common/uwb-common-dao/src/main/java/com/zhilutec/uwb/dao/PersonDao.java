package com.zhilutec.uwb.dao;


import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.Person;
import com.zhilutec.uwb.common.pojo.DepartmentRs;
import com.zhilutec.uwb.common.pojo.Person2DptRS;
import com.zhilutec.uwb.common.pojo.PersonDepartmentParam;
import com.zhilutec.uwb.common.pojo.PersonListRs;

import java.util.List;

public interface PersonDao extends MyMapper<Person> {
    //通过部门查找人员
    List<Person> getPersonsByDepartment(String code);

    //分组查询部门人员
    List<DepartmentRs> groupPersonsByDepartment();

    //分组查询部门人员
    List<DepartmentRs> groupPersonsByDepartment(String deparmentCode);

    //根据条件查询人员包含人员的部门信息
    List<PersonListRs> getPersonList(Person personParam);
    List<PersonListRs> getPersons();

    Integer batchUpdateDepartment(PersonDepartmentParam personDepartmentParam);

    List<Person2DptRS> getPersonsByDpt(String deparmentCode);

}

