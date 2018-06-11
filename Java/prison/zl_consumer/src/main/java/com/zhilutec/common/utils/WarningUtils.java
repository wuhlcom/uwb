package com.zhilutec.common.utils;

import java.util.HashMap;
import java.util.Map;

public class WarningUtils {
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

    public final static String URGENCY = "严重";
    public final static String COMMON = "普通";
    public final static String TIP = "提示";

    public final static String CROSS_CODE = "0101";
    public final static String CROSS = "串仓";
    public final static String CANCEL_CROSS = "取消串仓报警";


    public final static String OUT_CODE = "0102";
    public final static String OUT = "围栏";
    public final static String OUT_DETAIL = "电子围栏";
    public final static String CANCEL_OUT = "取消电子围栏报警";

    public final static String BUTTON_CODE = "0103";
    public final static String BUTTON = "按钮";
    public final static String BUTTON_DETAIL = "SOS按钮";
    public final static String CANCEL_BUTTON = "取消SOS按钮报警";

    public final static String BRACELET_CODE = "0104";
    public final static String BRACELET = "腕带";
    public final static String BRACELET_DETAIL = "腕带拆除";
    public final static String CANCEL_BRACELET = "取消腕带拆除报警";

    public final static String HEART_CODE = "0105";
    public final static String HEART = "心率";
    public final static String HEART_DETAIL = "心率异常";
    public final static String CANCEL_HEART = "取消心率异常报警";

    public final static String LAVATORY_CODE = "0201";
    public final static String LAVATORY = "厕所";
    public final static String LAVATORY_DETAIL = "厕所滞留";
    public final static String CANCEL_LAVATORY = "取消厕所滞留报警";

    public final static String NOSIGNAL_CODE = "0202";
    public final static String NOSIGNAL = "信号";
    public final static String NOSIGNAL_DETAIL = "信号消失";
    public final static String CANCEL_NOSIGNAL = "取消信号消失报警";

    public final static String LOWPOWER_CODE = "0301";
    public final static String LOWPOWER = "电量";
    public final static String LOWPOWER_DETAIL = "电量过低";
    public final static String CANCEL_LOWPOWER = "取消电量过低报警";


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
            put("0105", HEART);

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
            put("0105", HEART_DETAIL);

            put("0201", LAVATORY_DETAIL);
            put("0202", NOSIGNAL_DETAIL);

            put("0301", LOWPOWER_DETAIL);

        }
    };

    /**
     * 将报警编码转成报警消息
     **/
    public static String warningMsg(String code, int flag) {
        String msg = null;
        if (code.equals(CROSS_CODE) && flag == 0) {
            msg = CANCEL_CROSS;
        } else if (code.equals(CROSS_CODE) && flag == 1) {
            msg = CROSS;
        }

        if (code.equals(OUT_CODE) && flag == 0) {
            msg = CANCEL_OUT;
        } else if (code.equals(OUT_CODE) && flag == 1) {
            msg = OUT_DETAIL;
        }

        if (code.equals(BUTTON_CODE) && flag == 0) {
            msg = CANCEL_BUTTON;
        } else if (code.equals(BUTTON_CODE) && flag == 1) {
            msg = BUTTON_DETAIL;
        }


        if (code.equals(BRACELET_CODE) && flag == 0) {
            msg = CANCEL_BRACELET;
        } else if (code.equals(BRACELET_CODE) && flag == 1) {
            msg = BRACELET_DETAIL;
        }

        if (code.equals(HEART_CODE) && flag == 0) {
            msg = CANCEL_HEART;
        } else if (code.equals(HEART_CODE) && flag == 1) {
            msg = HEART_DETAIL;
        }

        if (code.equals(LAVATORY_CODE) && flag == 0) {
            msg = CANCEL_LAVATORY;
        } else if (code.equals(LAVATORY_CODE) && flag == 1) {
            msg = LAVATORY_DETAIL;
        }

        if (code.equals(NOSIGNAL_CODE) && flag == 0) {
            msg = CANCEL_NOSIGNAL;
        } else if (code.equals(NOSIGNAL_CODE) && flag == 1) {
            msg = NOSIGNAL_DETAIL;
        }

        if (code.equals(LOWPOWER_CODE) && flag == 0) {
            msg = CANCEL_LOWPOWER;
        } else if (code.equals(LOWPOWER_CODE) && flag == 0) {
            msg = LOWPOWER_DETAIL;
        }
        return msg;
    }


}
