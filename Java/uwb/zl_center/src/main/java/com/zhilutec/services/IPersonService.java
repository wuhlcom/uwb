package com.zhilutec.services;

import com.zhilutec.dbs.entities.Person;

public interface IPersonService {


    void flushdDbData();

    String getPersonName(Long tagId);

    Person getPerson(Long tagId);
}
