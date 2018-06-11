package com.zhilutec.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhilutec.db.daos.TagWarningDao;
import com.zhilutec.db.entities.TagWarningEntity;
import com.zhilutec.services.ITagWarningService;

import java.util.List;

@Service
@Transactional
public class TagWarningServiceImpl implements ITagWarningService {

    @Autowired
    private TagWarningDao tagWarningDao;


    @Override
    public int saveWarning(String jsonStr) {
        TagWarningEntity record = JSON.parseObject(jsonStr, TagWarningEntity.class);
        int rs = tagWarningDao.insertSelective(record);
        return rs;
    }

    @Override
    public int saveWarning(TagWarningEntity warning) {
        int rs = tagWarningDao.insertSelective(warning);
        return rs;
    }

    @Override
    public void saveBatch(List<TagWarningEntity> records) {
        if (records != null && !records.isEmpty())
            tagWarningDao.insertList(records);
    }
}