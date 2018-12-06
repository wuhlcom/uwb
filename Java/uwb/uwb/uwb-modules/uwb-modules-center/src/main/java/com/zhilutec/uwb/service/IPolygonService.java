package com.zhilutec.uwb.service;


import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.entity.Coordinate;
import com.zhilutec.uwb.entity.Fence;

import java.util.List;
import java.util.Map;

public interface IPolygonService {

    Boolean isInFence(Fence fence, Double x, Double y);

    Boolean isInFence(String points, Double x, Double y);

    Map<String, Boolean> isInside(JSONObject point, List<Fence> fences);

    Map<String, Boolean> isInside(Coordinate coordinate, List<Fence> fences);
}
