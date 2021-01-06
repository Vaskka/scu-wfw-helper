package com.dpitech.edge.common;

import java.util.Map;
import java.util.Set;

/**
 * @author rusheng
 */
public class CommonUtil {

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
            stringBuilder.append(entry.getValue());

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

}
