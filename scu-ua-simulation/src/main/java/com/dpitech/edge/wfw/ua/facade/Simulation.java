package com.dpitech.edge.wfw.ua.facade;

import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;

/**
 * @author rusheng
 */
public interface Simulation {

    /**
     * <b>学号密码</b>得到已经授权的cookie，网络访问失败抛出IOException
     * @param studentNumber 学号
     * @param getPasswordCallback ua系统密码
     * @return cookie String
     * @throws AuthException 鉴权失败
     * @throws IOException 接口调用失败
     */
    String getWfwCookieStringAuthenticatedUsername(final String studentNumber, PasswordCallback getPasswordCallback) throws AuthException, IOException;

    /**
     * <b>关联手机</b>得到已经授权的cookie，网络访问失败抛出IOException
     *
     * @param studentNumber 学号
     * @param getPhoneCodeCallback 手机验证码接口回调
     * @return cookie String
     * @throws AuthException 鉴权失败
     * @throws IOException 接口调用失败
     */
    String getWfwCookieStringAuthenticatedPhone(final String studentNumber, PasswordCallback getPhoneCodeCallback) throws AuthException, IOException;

    /**
     * 验证学号密码正确性
     * @param student 学号
     * @param password 密码
     * @return boolean
     * @throws IOException upper api error.
     */
    boolean verifyStudentIdentity(final String student, final String password) throws IOException;

    /**
     * 获得学号对应的手机验证码
     * @param stuNumber 学号
     * @throws IOException 接口调用失败
     */
    void getPhoneCode(String stuNumber) throws IOException;

    /**
     * 手机验证码回调
     * @author rusheng
     */
    interface PasswordCallback {
        /**
         * get phone token
         * @return phone token
         */
        String getPassword();

    }
}
