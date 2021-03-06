package com.dpitech.edge.wfw.biz.facade;


import com.dpitech.edge.wfw.biz.exceptions.ApiException;
import com.dpitech.edge.wfw.biz.model.SelfInfoFetchModel;
import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;

/**
 *
 * 基本信息API
 *
 * @author rusheng
 */
public interface SelfInfoProcessor extends BaseFetchApiProcessor {

    /**
     * 获得组装好的java对象，过滤了不常用的字段
     * @param authCookie 已经鉴权的cookie
     * @return SelfInfoModel
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    SelfInfoFetchModel getInfo(String authCookie) throws AuthException, IOException, ApiException;

}
