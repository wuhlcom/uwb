package com.zhilutec.uwb.dao;


import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.Fence;
import com.zhilutec.uwb.common.pojo.FenceRs;

import java.util.List;

public interface FenceDao extends MyMapper<Fence> {

    List<FenceRs> getFencesByType(Integer type);
    List<Fence> getFences(Fence fence);
    List<Fence> getFences();
}

