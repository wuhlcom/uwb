package com.zhilutec.uwb.service;


import com.zhilutec.uwb.entity.Person;

public interface IPersonService {
    Person getCache(Long tagId);
}
