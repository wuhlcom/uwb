package com.zhilutec.services.impl;

import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.daos.PersonDao;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.services.IPersonService;
import com.zhilutec.services.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl extends IRedisService<Person> implements IPersonService {

    @Autowired
    PersonDao personDao;

    @Override
    protected String getRedisKey() {
        return null;
    }

    @Override
    public void flushdDbData() {
        this.flushdb();
    }

    @Override
    public String getPersonName(Long tagId) {
        String key = ConstantUtil.PERSON_KEY_PRE + ":" + tagId.toString();
        return this.strGet(key);
    }

    @Override
    public Person getPerson(Long tagId) {
        String key = ConstantUtil.PERSON_KEY_PRE + ":" + tagId.toString();
        return this.get(key, tagId.toString());
    }


}
