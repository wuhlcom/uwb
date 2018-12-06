package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.services.IPersonService;
import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class PersonServiceImpl implements IPersonService {

    @Resource
    IRedisService redisService;

    @Override
    public Person getCache(Long tagId) {
        Person person = null;
        String key = redisService.genRedisKey(ConstantUtil.PERSON_KEY_PRE, tagId);
        Map map = redisService.hashGetMap(key);

        if (map == null) {
            return person;
        }

        String mapStr = JSONObject.toJSONString(map);
        person = JSONObject.parseObject(mapStr, Person.class);

        return person;
    }


}
