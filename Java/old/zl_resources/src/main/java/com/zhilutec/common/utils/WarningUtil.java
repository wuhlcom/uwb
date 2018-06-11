package com.zhilutec.common.utils;

import java.util.HashMap;
import java.util.Map;

public class WarningUtil {
    //	严重报警	串仓	0101
//		电子围栏	0102
//		sos按钮	0103
//		腕带拆除	0104
//		心率告警	0105
//    普通报警	卫生间滞留	0201
//			信号失联	0202
//    提示报警	低电量报警	0301
    public final static String ONLINE = "在线";
    public final static String OFFLINE = "离线";

    public final static String URGENCY_CODE = "01";
    public final static String COMMON_CODE = "02";
    public final static String TIP_CODE = "03";

    private final static String URGENCY = "严重";
    private final static String COMMON = "普通";
    private final static String TIP = "提示";

    private final static String CROSS = "串仓";

    private final static String OUT = "围栏";
    private final static String OUT_DETAIL = "电子围栏";

    private final static String BUTTON = "按钮";
    private final static String BUTTON_DETAIL = "SOS按钮";

    private final static String BRACELET = "腕带";
    private final static String BRACELET_DETAIL = "腕带拆除";
    private final static String HEAT = "心率";
    private final static String HEAT_DETAIL = "心率异常";

    private final static String LAVATORY = "厕所";
    private final static String LAVATORY_DETAIL = "厕所滞留";

    private final static String NOSIGNAL = "信号";
    private final static String NOSIGNAL_DETAIL = "信号消失";

    private final static String LOWPOWER = "电量";
    private final static String LOWPOWER_DETAIL = "电量过低";


    public final static Map<String, String> warningsMap = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put("01", URGENCY);
            put("02", COMMON);
            put("03", TIP);

            put("0101", CROSS);
            put("0102", OUT);
            put("0103", BUTTON);
            put("0104", BRACELET);
            put("0105", HEAT);

            put("0201", LAVATORY);
            put("0202", NOSIGNAL);

            put("0301", LOWPOWER);

        }
    };

    public final static Map<String, String> warningsMapDetail = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {

            put("0101", CROSS);
            put("0102", OUT_DETAIL);
            put("0103", BUTTON_DETAIL);
            put("0104", BRACELET_DETAIL);
            put("0105", HEAT_DETAIL);

            put("0201", LAVATORY_DETAIL);
            put("0202", NOSIGNAL_DETAIL);

            put("0301", LOWPOWER_DETAIL);

        }
    };


}
