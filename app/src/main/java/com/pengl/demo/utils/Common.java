package com.pengl.demo.utils;

import java.util.Random;

public class Common {

    /**
     * 获取随机数
     *
     * @param iRdLength 随机数的长度
     * @return 随机数
     */
    public static String getRandom(int iRdLength) {
        Random rd = new Random();
        int iRd = rd.nextInt();
        if (iRd < 0) {
            iRd *= -1;
        }
        String sRd = String.valueOf(iRd);
        int iLgth = sRd.length();
        if (iRdLength > iLgth) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < iRdLength - String.valueOf(iRd).length(); i++) {
                result.append("0");
            }
            result.append(iRd);
            return result.toString();
        } else {
            return sRd.substring(iLgth - iRdLength, iLgth);
        }
    }

    /**
     * 在指定的范围内取随机数
     *
     * @param min 小值
     * @param max 大值
     * @return 在min和max的范围内，取一个随机数
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

}
