package com.dpitech.edge.wfw.biz.facade;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.wfw.biz.exceptions.ApiException;
import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;

/**
 *
 * 健康报打卡api
 *
 * @author rusheng
 */
public interface HealthReportProcessor {

    /**
     * 江安定位上报
     * @param stuNum 学号
     * @return json object
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    JSONObject reportJiangAn(String stuNum) throws AuthException, IOException, ApiException;
}
