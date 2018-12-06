package com.zhilutec.uwb.service;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.entity.Fence;
import org.springframework.transaction.annotation.Transactional;

public interface IFenceService {

    void fenceCacheInit();

    String getfences(JSONObject jsonObject);

    String add(JSONObject jsonObject);

    //更新人员
    @Transactional
    String update(JSONObject jsonObject);

    String groupFencesByType(JSONObject jsonObject);

    String delete(JSONObject jsonObject);

    Fence getFenceByCode(String code);
}
