package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.entity.Person;
import com.zhilutec.uwb.service.IPersonService;
import com.zhilutec.uwb.service.IRedisService;
import com.zhilutec.uwb.util.ConstantUtil;
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
