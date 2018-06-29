package com.zhilutec.common.validators;

public class PublicValidator {

    public static boolean isAreaCode(String areaCode) {
        if (RegexUtil.isNotNull(areaCode)) {
            if (RegexUtil.isInteger(areaCode) && areaCode.length()==10) {
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public static boolean isOrder(String order) {
        if (RegexUtil.isNotNull(order)) {
            String lowerOrder = order.toLowerCase();
            if (!(lowerOrder.equals("asc") || lowerOrder.equals("desc"))) {
                return false;
            }
        }
        return true;
    }

    public static  boolean isTimestamp(Long timestamp){
        return  true;
    }
}
