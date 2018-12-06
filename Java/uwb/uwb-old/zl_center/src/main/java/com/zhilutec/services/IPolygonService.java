package com.zhilutec.services;


import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Coordinate;
import com.zhilutec.dbs.entities.Fence;

import java.util.List;
import java.util.Map;

public interface IPolygonService {

    Boolean isInFence(Fence fence, Double x, Double y);

    Boolean isInFence(String points, Double x, Double y);

    Map<String, Boolean> isInside(JSONObject point, List<Fence> fences);

    Map<String, Boolean> isInside(Coordinate coordinate, List<Fence> fences);
}
