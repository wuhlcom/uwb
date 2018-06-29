package com.zhilutec.services;

import com.zhilutec.dbs.entities.Fence;

public interface IFenceService {

    Fence getCache(String fenceCode);

}
