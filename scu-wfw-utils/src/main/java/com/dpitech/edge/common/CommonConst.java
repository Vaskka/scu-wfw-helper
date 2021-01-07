package com.dpitech.edge.common;

/**
 * @author rusheng
 */
public class CommonConst {

    /**
     * 超时时间
     */
    public static final Integer HTTP_TIMEOUT_SECOND = 20;

    /**
     * user agent
     */
    public static final String COMMON_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";

    /**
     * Accept
     */
    public static final String COMMON_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";

    /**
     * Origin
     */
    public static final String COMMON_ORIGIN = "https://ua.scu.edu.cn";

    public static final String COMMON_SEC_FETCH_SITE = "same-origin";
    public static final String COMMON_SEC_FETCH_MODE = "navigate";
    public static final String COMMON_SEC_FETCH_DEST = "document";
    public static final String COMMON_SEC_FETCH_USER = "?1";

    /**
     * content type form-data
     */
    public static final String COMMON_CONTENT_TYPE_FORM_DATA = "application/x-www-form-urlencoded";

    /**
     * content type json
     */
    public static final String COMMON_CONTENT_TYPE_JSON = "application/json";

    /**
     * 不特别声明的refer
     */
    public static final String COMMON_REFER = "https://ua.scu.edu.cn/login";

    /**
     * 手机验证吗url
     */
    public static final String PHONE_CODE_URL = "https://ua.scu.edu.cn/token";

    /**
     * 个人信息url
     */
    public static final String SELF_INFO_URL = "https://wfw.scu.edu.cn/uc/wap/user/get-info";

    /**
     * 健康码url
     */
    public static final String HEALTH_CODE_URL = "https://wfw.scu.edu.cn/healthcode/wap/default/index";

    /**
     * 成绩url
     */
    public static final String SCORE_URL = "https://wfw.scu.edu.cn/score/wap/default/get-data";

    /**
     * 第一步登录url
     */
    public static final String UA_FIRST_LOGIN_URL = "https://ua.scu.edu.cn/login";

    /**
     * 健康报打卡页面url
     */
    public static final String NCOV_PAGE_URL = "https://wfw.scu.edu.cn/ncov/wap/default/index";

    /**
     * 健康报打卡上报url
     */
    public static final String NCOV_REPORT_URL = "https://wfw.scu.edu.cn/ncov/wap/default/save";

}
