package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.dbs.pojos.PersonWristbandRS;

public interface PersonDao extends MyMapper<Person>, BaseDao<Person> {
   PersonWristbandRS getPersonByTag(Long tag);
}

