package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;
import com.zhilutec.uwb.entity.Coordinate;
import com.zhilutec.uwb.entity.Fence;
import com.zhilutec.uwb.service.IPolygonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PolygonServiceImpl implements IPolygonService {


    /**
     * 构建多边形围栏组
     */
    private Map<String, Polygon> buildFences(List<Fence> fences) {
        List<Polygon> polygons = new ArrayList<>();
        Map<String, Polygon> fencePolygons = new HashMap<>();
        for (Fence fence : fences) {
            Polygon polygon = this.buildFence(fence);
            fencePolygons.put(fence.getFenceCode(), polygon);
            polygons.add(polygon);
        }
        return fencePolygons;
    }

    /**
     * 构建单个多边形围栏
     */
    public Polygon buildFence(Fence fence) {
        String points = fence.getPoints();
        return getPolygon(points);
    }

    /**
     * 构建单个多边形围栏
     */
    public Polygon buildFence(String points) {
        return getPolygon(points);
    }

    private Polygon getPolygon(String points) {
        JSONArray jsonArray = JSON.parseArray(points);
        Polygon polygon;
        Polygon.Builder polygonBuilder = new Polygon.Builder();
        for (Object o : jsonArray) {
            JSONObject point = JSONObject.parseObject(o.toString());
            double x = point.getFloatValue("x");
            double y = point.getFloatValue("y");
            polygonBuilder.addVertex(new Point(x, y));
        }
        polygon = polygonBuilder.build();
        return polygon;
    }

    /**
     * 判断和多边形的关系
     */
    @Override
    public Boolean isInFence(Fence fence, Double x, Double y) {
        Polygon polygon = this.buildFence(fence);
        return polygon.contains(new Point(x, y));
    }

    /**
     * 判断和多边形的关系
     */
    @Override
    public Boolean isInFence(String points, Double x, Double y) {
        Polygon polygon = this.buildFence(points);
        return polygon.contains(new Point(x, y));
    }


    /**
     * 判断点和多边型组的关系
     */
    @Override
    public Map<String, Boolean> isInside(JSONObject point, List<Fence> fences) {
        List<Boolean> fenceRs = new ArrayList<>();

        Map<String, Boolean> pointFenceMap = new HashMap<>();

        Map<String, Polygon> polygonsMap = this.buildFences(fences);

        double x = point.getFloatValue("x");
        double y = point.getFloatValue("y");

        //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
        //entry.getKey() ;entry.getValue(); entry.setValue();
        //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
        for (Map.Entry<String, Polygon> entry : polygonsMap.entrySet()) {
            boolean isIn = entry.getValue().contains(new Point(x, y));
            pointFenceMap.put(entry.getKey(), isIn);
        }

        return pointFenceMap;
    }

    /**
     * 判断点和多边型组的关系
     */
    @Override
    public Map<String, Boolean> isInside(Coordinate coordinate, List<Fence> fences) {
        List<Boolean> fenceRs = new ArrayList<>();

        Map<String, Boolean> pointFenceMap = new HashMap<>();

        Map<String, Polygon> polygonsMap = this.buildFences(fences);

        double x = coordinate.getPosX();
        double y = coordinate.getPosY();

        //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
        //entry.getKey() ;entry.getValue(); entry.setValue();
        //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
        for (Map.Entry<String, Polygon> entry : polygonsMap.entrySet()) {
            boolean isIn = entry.getValue().contains(new Point(x, y));
            pointFenceMap.put(entry.getKey(), isIn);
        }

        return pointFenceMap;
    }

}
