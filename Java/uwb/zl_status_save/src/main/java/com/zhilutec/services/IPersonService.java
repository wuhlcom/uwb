package com.zhilutec.services;

import com.zhilutec.dbs.entities.Person;
public interface IPersonService {

      Person getCache(Long tagId);
}
