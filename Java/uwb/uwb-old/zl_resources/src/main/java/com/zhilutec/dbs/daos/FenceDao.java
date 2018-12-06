package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.Fence;
import com.zhilutec.dbs.pojos.FenceRs;

import java.util.List;

public interface FenceDao extends MyMapper<Fence>, BaseDao<Fence> {

    List<FenceRs> getFencesByType(Integer type);
    List<Fence> getFences(Fence fence);
    List<Fence> getFences();
}

