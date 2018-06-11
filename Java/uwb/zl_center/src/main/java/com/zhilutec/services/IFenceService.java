package com.zhilutec.services;

import com.zhilutec.dbs.entities.Fence;

public interface IFenceService {
    Fence getFence(String fenceCode);

    Fence getFenceByCode(String code);
}
