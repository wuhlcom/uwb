package com.zhilutec.common.utils;

public class StatusFlagUtil {
    /**
     * 将信息类型使用flag标识来分类
     * 1 严重报警
     * 2 普通报警
     * 3 提示
     * 4 在线
     * 5 离线(信号消失)
     * 6 串仓，离开本监仓
     * 7 电子围栏(离线)
     */
    public static final Integer URGENCY = 1;
    public static final Integer COMMON = 2;
    public static final Integer TIP = 3;
    public static final Integer ONLINE = 4;
    public static final Integer OFFLINE =5;
    public static final Integer CROSS = 6;
    public static final Integer OUT = 7;

}
