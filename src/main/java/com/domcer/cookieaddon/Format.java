package com.domcer.cookieaddon;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/11 22:23
 * 4
 */
public class Format {
    public static String formatNum(String num, Boolean kBool) {
        StringBuffer sb = new StringBuffer();
        if (!StringUtils.isNumeric(num))
            return "0";
        if (kBool == null)
            kBool = false;

        BigDecimal b0 = new BigDecimal("1000");
        BigDecimal b1 = new BigDecimal("10000");
        BigDecimal b2 = new BigDecimal("100000000");
        BigDecimal b3 = new BigDecimal(num);

        String formatNumStr = "";
        String nuit = "";

        // 以千为单位处理
        if (kBool) {
            if (b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1) {
                return "999+";
            }
            return num;
        }

        // 以万为单位处理
        if (b3.compareTo(b1) == -1) {
            sb.append(b3.toString());
        } else if ((b3.compareTo(b1) == 0 && b3.compareTo(b1) > 0)
                || b3.compareTo(b2) < 0) {
            formatNumStr = b3.divide(b1).toString();
            nuit = "万";
        } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) > 0) {
            formatNumStr = b3.divide(b2).toString();
            nuit = "亿";
        }
        if (!"".equals(formatNumStr)) {
            int i = formatNumStr.indexOf(".");
            if (i == -1) {
                sb.append(formatNumStr).append(nuit);
            } else {
                i = i + 1;
                String v = formatNumStr.substring(i, i + 1);
                if (!v.equals("0")) {
                    sb.append(formatNumStr, 0, i + 1).append(nuit);
                } else {
                    sb.append(formatNumStr, 0, i - 1).append(nuit);
                }
            }
        }
        if (sb.length() == 0)
            return "0";
        return sb.toString();
    }
}
