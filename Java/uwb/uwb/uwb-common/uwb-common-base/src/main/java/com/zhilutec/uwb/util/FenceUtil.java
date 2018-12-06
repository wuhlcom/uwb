package com.zhilutec.uwb.util;

import java.util.HashMap;
import java.util.Map;

public class FenceUtil {

    public static final String POLYGON = "polygon"; //多边形
    public static final String CIRCLE = "circle";//圆
    public static final String OVAL = "oval";//椭圆
    public static final String RECTANGLE = "rectangle";//矩形

    public final static Map<Integer, String> FENCE_TYPE = new HashMap<Integer, String>() {

        private static final long serialVersionUID = 1L;

        {
            put(0, FenceUtil.POLYGON);
            put(1, FenceUtil.CIRCLE);
            put(2, FenceUtil.OVAL);
            put(3, FenceUtil.RECTANGLE);
        }
    };

}
