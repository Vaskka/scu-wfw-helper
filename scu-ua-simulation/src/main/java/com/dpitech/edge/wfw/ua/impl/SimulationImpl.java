package com.dpitech.edge.wfw.ua.impl;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.common.*;
import com.dpitech.edge.common.log.LogUtil;
import com.dpitech.edge.wfw.ua.excepton.AuthException;
import com.dpitech.edge.wfw.ua.facade.Simulation;
import lombok.extern.slf4j.Slf4j;
import org.seimicrawler.xpath.JXDocument;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * @author rusheng
 */
@Slf4j
public class SimulationImpl implements Simulation {

    /**
     * eai-sess
     */
    private static final String EAI_SESSION_COOKIE_KEY = "eai-sess";

    /**
     * uu key
     */
    private static final String UU_KEY_COOKIE_KEY = "UUkey";

    /**
     * ua域下cookie key
     */
    private static final String UA_DOMAIN_COOKIE_KEY = "_7da9a";

    /**
     * cookie manager
     * within-map: 'cookieKey' -> 'cookieVal'
     * 1. Set-Cookie -> save in within-map, new value can refresh the old.
     * 2. can get cookie from the within-map.
     */
    private static class CookieManager {
        /**
         * 存cookie的map
         */
        private final Map<String, String> cookieMap = new HashMap<>();

        /**
         * Set-Cookie -> Map
         * @param setCookieString Set-Cookie
         */
        public void saveCookieFromRawSetCookieString(String setCookieString) {
            String[] setCookieStringList = setCookieString.split(";");
            for (String rawCookieLine : setCookieStringList) {
                String[] eachCookieItem = rawCookieLine.split("=");
                if (eachCookieItem.length < 2) {
                    continue;
                }

                String cookieKey = eachCookieItem[0].startsWith(" ") ? eachCookieItem[0].substring(1) : eachCookieItem[0];
                String cookieVal = eachCookieItem[1];
                cookieMap.put(cookieKey, cookieVal);
            }
        }

        /**
         * 得到可以放进header里面的cookie
         * @param keys keys
         * @return String
         */
        public String getCookieValue(String ...keys) {
            var stringBuilder = new StringBuilder();
            var i = 0;
            for (String cookieKey : keys) {
                String cookieVal = cookieMap.get(cookieKey);
                if (cookieVal == null || cookieVal.length() == 0) {
                    i++;
                    continue;
                }

                stringBuilder.append(cookieKey);
                stringBuilder.append('=');
                stringBuilder.append(cookieVal);
                i++;

                if (i < keys.length) {
                    stringBuilder.append("; ");
                }
            }

            return stringBuilder.toString();
        }
    }

    /**
     * cookie manager
     */
    private final CookieManager cookieManager = new CookieManager();

    /**
     * <b>学号密码</b>得到已经授权的cookie，网络访问失败抛出IOException
     * @param studentNumber 学号
     * @param getPasswordCallback ua系统密码
     * @return cookie String
     * @throws AuthException 鉴权失败
     * @throws IOException 接口调用失败
     */
    @Override
    public String getWfwCookieStringAuthenticatedUsername(String studentNumber, PasswordCallback getPasswordCallback) throws AuthException, IOException {
        LogUtil.infof(log, "stu-num: {} ready to auth with username.", studentNumber);
        var realLoginPageUrl = getRealLoginUrl();

        // xpath process hide input and do post auth
        return doFinalLoginGetAuthenticatedCookie(studentNumber, getPasswordCallback.getPassword(), formHideInfo(realLoginPageUrl), UaLoginType.USERNAME_PSW);
    }

    /**
     * <b>关联手机</b>得到已经授权的cookie，网络访问失败抛出IOException
     *
     * 接口内不会尝试获取验证码
     *
     * @param studentNumber 学号
     * @param getPhoneCodeCallback 手机验证码接口回调
     * @return cookie String
     * @throws AuthException 鉴权失败
     * @throws IOException 接口调用失败
     */
    @Override
    public String getWfwCookieStringAuthenticatedPhone(String studentNumber, PasswordCallback getPhoneCodeCallback) throws AuthException, IOException {
        LogUtil.infof(log, "stu-num: {} ready to auth with phone.", studentNumber);
        var realLoginPageUrl = getRealLoginUrl();

        // xpath process hide input and do post auth
        return doFinalLoginGetAuthenticatedCookie(studentNumber, getPhoneCodeCallback.getPassword(), formHideInfo(realLoginPageUrl), UaLoginType.PHONE_CODE);
    }

    /**
     * 验证学号密码正确性
     * @param student 学号
     * @param password 密码
     * @return boolean
     * @throws IOException upper api error.
     */
    @Override
    public boolean verifyStudentIdentity(String student, final String password) throws IOException {
        try {
            getWfwCookieStringAuthenticatedUsername(student, () -> password);
        }
        catch (AuthException e) {
            return false;
        }
        catch (Throwable e) {
            throw new IOException("upper api error.", e);
        }

        return true;
    }

    @Override
    public void getPhoneCode(String stuNumber) throws IOException {
        JSONObject body = new JSONObject();
        body.put("username", stuNumber);
        String respBody = null;
        try {
            respBody = HttpUtil.postWithJson(CommonConst.PHONE_CODE_URL, body, "", CommonConst.COMMON_REFER).body();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }

        LogUtil.infof(log, "send phone code get response: {}", respBody);
        JSONObject resp = JSONObject.parseObject(respBody);
        if (CommonUtil.isEmpty(resp.getString("status")) || !"success".equals(resp.getString("status"))) {
            LogUtil.errorf(log, "send sms code to {} is error, error response is: {}", stuNumber, respBody);
            throw new IOException("send sms code error, status is " + resp.getString("status"));
        }

    }

    /**
     * HttpResponse -> localtion and save cookie
     * @param response HttpResponse
     * @return location url
     */
    private String fromResponseGetLocationAndSaveCookie(HttpResponse<String> response) {
        cookieManager.saveCookieFromRawSetCookieString(response.headers().firstValue("Set-Cookie")
                .orElse(""));
        var locationUrl = response.headers().firstValue("location")
                .orElse(null);
        LogUtil.debugf(log, "from response headers get location: {}", locationUrl);
        return locationUrl;
    }

    /**
     * 得到真正的登录url
     * @return url
     * @throws IOException exception
     */
    private String getRealLoginUrl() throws IOException {
        // 第一次302，得到eai-sess、第二次鉴权的url
        HttpResponse<String> httpResponse;
        try {
            httpResponse = HttpUtil.get(CommonConst.HEALTH_CODE_URL, "", CommonConst.COMMON_REFER);
            LogUtil.debugf(log, "first 302, get response: {}", httpResponse.toString());
        } catch (Throwable e) {
            LogUtil.errorf(log, "first 302 error, url: {}, message:{}", e, CommonConst.HEALTH_CODE_URL, e.getMessage());
            throw new IOException(e.toString(), e);
        }

        // 第二次302，得到uuKey
        var locationUrl = fromResponseGetLocationAndSaveCookie(httpResponse);
        HttpResponse<String> newResp;
        String secondCookieVal = cookieManager.getCookieValue(EAI_SESSION_COOKIE_KEY, UU_KEY_COOKIE_KEY);
        try {
            newResp = HttpUtil.get(locationUrl, secondCookieVal, CommonConst.HEALTH_CODE_URL);
            LogUtil.debugf(log, "second 302, get response: {}", newResp.toString());
        }
        catch (Throwable e) {
            LogUtil.errorf(log, "second 302 error, url: {}, cookie:{}, message:{}", e, locationUrl, secondCookieVal, e.getMessage());
            throw new IOException(e.toString(), e);
        }

        // 最后一次get, 得到真实login地址
        var casAuthUrl = fromResponseGetLocationAndSaveCookie(newResp);
        HttpResponse<String> realLoginUrlResp;
        String realLoginCookieVal = cookieManager.getCookieValue(EAI_SESSION_COOKIE_KEY, UU_KEY_COOKIE_KEY);
        try {
            realLoginUrlResp = HttpUtil.get(casAuthUrl, realLoginCookieVal, CommonConst.HEALTH_CODE_URL);
            LogUtil.debugf(log, "final 302, get response: {}", realLoginUrlResp.toString());
        } catch (Throwable e) {
            LogUtil.errorf(log, "final 302 error, url: {}, cookie:{}, message:{}", e, casAuthUrl, realLoginCookieVal, e.getMessage());
            throw new IOException(e.toString(), e);
        }

        return fromResponseGetLocationAndSaveCookie(realLoginUrlResp);
    }

    /**
     * 解析页面隐藏的input tag
     * @param realLoginPageUrl 真实的登录url
     * @return text in input tag
     * @throws IOException IOException
     */
    private String formHideInfo(String realLoginPageUrl) throws IOException {
        HttpResponse<String> resp;
        try {
            resp = HttpUtil.get(realLoginPageUrl, "", CommonConst.HEALTH_CODE_URL);
        }
        catch (Throwable e) {
            throw new IOException(e.toString(), e);
        }
        cookieManager.saveCookieFromRawSetCookieString(resp.headers().firstValue("Set-Cookie")
                .orElse(""));

        // process page get input tag with "display=none;"
        try {
            var jxd = JXDocument.create(resp.body());
            var xpathRes = jxd.selNOne("//input[@name=\"execution\"]/@value");
            String execText = xpathRes.asString();
            LogUtil.debugf(log, "parse login page get hide-text: {}", execText.length() > 100 ? execText.substring(0, 30) + "..." : execText);
            if (CommonUtil.isEmpty(execText)) {
                throw new IOException("xpath parse error.", new RuntimeException());
            }

            return execText;
        }
        catch (Throwable e) {
            throw new IOException(e.toString(), e);
        }
    }

    /**
     * 为cookie获权限
     * @param stuNumber stuNumber
     * @param psw password
     * @param hideText input tag with "display: none;"
     * @return cookie line authenticated
     */
    private String doFinalLoginGetAuthenticatedCookie(String stuNumber, String psw, String hideText, UaLoginType uaLoginType) throws IOException {
        HttpResponse<String> resp;
        try {
            Map<String, String> bodyForm = new HashMap<>(16);
            bodyForm.put("username", stuNumber);
            bodyForm.put("password", psw);
            bodyForm.put("type", uaLoginType.getVal());
            bodyForm.put("submit", "登录");
            bodyForm.put("_eventId", "submit");
            bodyForm.put("execution", hideText);

            // debug 日志过滤的form-data
            Set<String> filterSet = new HashSet<>();
            filterSet.add("password");
            filterSet.add("execution");
            LogUtil.debugf(log, "one-step auth ready to send, post form is: {}",
                    CommonUtil.fromMapGetFormDataStringRemoved(bodyForm, filterSet));

            // do post form-data
            resp = HttpUtil.postWithFormData(CommonConst.UA_FIRST_LOGIN_URL,
                    CommonUtil.fromMapGetFormDataString(bodyForm),
                    cookieManager.getCookieValue(UA_DOMAIN_COOKIE_KEY), "");
            LogUtil.debugf(log, "one-step auth already sent, resp is: {}", resp.toString());
        }
        catch (Throwable e) {
            throw new IOException(e.toString(), e);
        }

        // 最后二步提交，cookie获权
        String finalAuthUrl = resp.headers().firstValue("location")
                .orElseThrow(() -> new AuthException("auth error.", new RuntimeException(), resp.toString()));
        HttpResponse<String> finalAuthResp;
        String stepTwoCookieVal = cookieManager.getCookieValue(EAI_SESSION_COOKIE_KEY, UU_KEY_COOKIE_KEY);
        try {
            LogUtil.debugf(log, "two-step auth ready to send, cookie is: {}", stepTwoCookieVal);
            finalAuthResp = HttpUtil.get(finalAuthUrl, stepTwoCookieVal, "");
            LogUtil.debugf(log, "two-step auth already sent, resp is: {}", finalAuthResp.toString());
        }
        catch (Throwable e) {
            throw new IOException(e.toString(), e);
        }

        LogUtil.infof(log, "stu: {}, aleady authenticated.", stuNumber);
        return stepTwoCookieVal;
    }

}
