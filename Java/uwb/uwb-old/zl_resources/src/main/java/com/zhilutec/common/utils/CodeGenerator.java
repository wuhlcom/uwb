package com.zhilutec.common.utils;

import java.util.Random;

public class CodeGenerator {

    // 生成uuid
    public static String generateUuid(String prefix,int strLen) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMOPQRST";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        for (int i = 0; i < strLen - prefix.length(); i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
