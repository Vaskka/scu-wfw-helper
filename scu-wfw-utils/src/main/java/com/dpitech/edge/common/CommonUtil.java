package com.dpitech.edge.common;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author rusheng
 */
public class CommonUtil {

    private static final Random RANDOM = new Random();

    /**
     * String is empty or null
     * @param target String
     * @return boolean
     */
    public static boolean isEmpty(String target) {
        return target == null || target.length() == 0;
    }

    /**
     * map -> form-data-str
     * @param formData form-data
     * @return String
     */
    public static String fromMapGetFormDataString(Map<String, String> formData) {
        var i = 0;
        var stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append('=');
            stringBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));

            i++;
            if (i < formData.size()) {
                stringBuilder.append('&');
            }
        }

        return stringBuilder.toString();
    }

    /**
     * map -> form-data-str
     * @param formData form-data
     * @return String
     */
    public static String fromMapGetFormDataStringRemoved(Map<String, String> formData, Set<String> removedKey) {
        var i = 0;
        var stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append('=');

            // 脱敏
            if (removedKey.contains(entry.getKey())) {
                stringBuilder.append("xxx");
            }
            else {
                stringBuilder.append(entry.getValue());
            }

            i++;
            if (i < formData.size()) {
                stringBuilder.append('&');
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 得到指定长度的随机数
     * @param length 长度
     * @return String
     */
    public static String getRandomNumWithLengthNonZeroBegin(int length) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            res.append(Math.abs(RANDOM.nextInt()) % 10);
        }

        if (res.length() == 0) {
            return "0";
        }

        if (res.charAt(0) == '0') {
            Integer nonZero = 1 + (Math.abs(RANDOM.nextInt()) % 9);
            res.replace(0, 1, String.valueOf(nonZero));
        }

        return res.toString();
    }

    /**
     * 得到指定长度的随机数
     * @param length 长度
     * @return String
     */
    public static String getRandomNumWithLength(int length) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            res.append(Math.abs(RANDOM.nextInt()) % 10);
        }

        if (res.length() == 0) {
            return "0";
        }

        return res.toString();
    }

}
