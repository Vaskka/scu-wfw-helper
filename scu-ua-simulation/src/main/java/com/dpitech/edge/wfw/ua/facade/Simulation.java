package com.dpitech.edge.wfw.ua.facade;

import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;

/**
 * @author rusheng
 */
public interface Simulation {

    /**
     * 得到已经授权的cookie，网络访问失败抛出IOException
     * @param studentNumber 学号
     * @param password ua系统密码
     * @return cookie String
     * @throws AuthException 鉴权失败
     * @throws IOException 接口调用失败
     */
    String getWfwCookieStringAuthenticated(String studentNumber, String password) throws AuthException, IOException;

    /**
     * 验证学号密码正确性
     * @param student 学号
     * @param password 密码
     * @return boolean
     * @throws IOException upper api error.
     */
    boolean verifyStudentIdentity(String student, String password) throws IOException;
}
