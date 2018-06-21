package com.zhilutec.services;

import com.zhilutec.dbs.entities.Person;
public interface IPersonService {

    Person getPerson(Long tagId);
}
