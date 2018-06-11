package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.Department;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.dbs.pojos.DepartmentRs;
import com.zhilutec.dbs.pojos.Person2DptRS;
import com.zhilutec.dbs.pojos.PersonDepartmentParam;
import com.zhilutec.dbs.pojos.PersonListRs;


import java.util.List;

public interface PersonDao extends MyMapper<Person>, BaseDao<Person> {
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

