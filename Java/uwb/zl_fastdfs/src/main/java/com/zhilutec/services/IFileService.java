package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.FileEntity;
import org.springframework.transaction.annotation.Transactional;

public interface IFileService {


    Integer add(String jsonStr);

    String delete(JSONObject jsonObject);

    @Transactional
    String update(JSONObject jsonObject);


    String getFilesRs(JSONObject jsonObject);

    @Transactional
    String mapSwitch(JSONObject jsonObject);
}
