package com.zhilutec.services.impl;

import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.daos.FenceDao;
import com.zhilutec.dbs.entities.Fence;
import com.zhilutec.services.IFenceService;
import com.zhilutec.services.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FenceServiceImpl extends IRedisService<Fence> implements IFenceService {

    @Autowired
    FenceDao fenceDao;

    @Override
    protected String getRedisKey() {
        return null;
    }

    @Override
    public Fence getFence(String fenceCode) {
        String key = ConstantUtil.FENCE_KEY_PRE + ":" + fenceCode;
        return this.get(key, fenceCode);
    }


    @Override
    public Fence getFenceByCode(String code) {
        Fence fence = new Fence();
        fence.setFenceCode(code);
        fence.setIsdel(1);
        return fenceDao.selectOne(fence);
    }


}
