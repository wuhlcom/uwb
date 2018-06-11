package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Fence;
import com.zhilutec.dbs.pojos.FenceRs;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
