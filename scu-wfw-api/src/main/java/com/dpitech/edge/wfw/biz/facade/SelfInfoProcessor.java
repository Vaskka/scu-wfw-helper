package com.dpitech.edge.wfw.biz.facade;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;

/**
 *
 * 基本信息API
 *
 * @author rusheng
 */
public interface SelfInfoProcessor {

    /**
     * 个人信息的原生数据
     * @param authCookie 已经赋权的cookie
     * @return json object
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     */
    JSONObject getRawData(String authCookie) throws AuthException, IOException;

}
