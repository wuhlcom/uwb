package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.Fence;

import java.util.List;

public interface FenceDao extends MyMapper<Fence>, BaseDao<Fence> {
    List<Fence> getFenceByPerson(String personCode);
    List<Fence> getFences(List<String> fenceCodeLs);
}

