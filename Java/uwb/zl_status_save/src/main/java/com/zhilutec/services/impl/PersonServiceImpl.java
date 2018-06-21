package com.zhilutec.services.impl;

import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.services.IPersonService;
import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl extends IRedisService<Person> implements IPersonService {

    @Override
    protected String getRedisKey() {
        return null;
    }


    @Override
    public Person getPerson(Long tagId) {
        String key = ConstantUtil.PERSON_KEY_PRE + ":" + tagId.toString();
        return this.get(key, tagId.toString());
    }


}
