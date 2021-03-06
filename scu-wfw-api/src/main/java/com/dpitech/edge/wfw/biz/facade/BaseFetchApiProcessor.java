package com.dpitech.edge.wfw.biz.facade;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.wfw.biz.exceptions.ApiException;
import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;

/**
 *
 * 只读接口api基类
 *
 * @author rusheng
 */
public interface BaseFetchApiProcessor {

    /**
     * 返回获取数据的接口拿到的原生json
     * @param authCookie 已经赋权的cookie-line
     * @return json object
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    JSONObject getRawJsonData(String authCookie) throws AuthException, IOException, ApiException;
}
