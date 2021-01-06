package com.dpitech.edge.wfw.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.common.CommonConst;
import com.dpitech.edge.common.HttpUtil;
import com.dpitech.edge.common.log.LogUtil;
import com.dpitech.edge.wfw.biz.facade.HealthCodeProcessor;
import com.dpitech.edge.wfw.ua.excepton.AuthException;
import com.dpitech.edge.wfw.ua.facade.Simulation;
import com.dpitech.edge.wfw.ua.impl.SimulationImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * 健康码接口实现
 * @author rusheng
 */
@Slf4j
public class HealthCodeProcessorImpl implements HealthCodeProcessor {

    /**
     * 返回健康码接口获取原生json
     * @param authCookie 已经赋权的cookie-line
     * @return json object
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     */
    @Override
    public JSONObject getRawJsonData(String authCookie) throws AuthException, IOException {
        HttpResponse<String> httpResponse;
        try {
            LogUtil.debugf(log, "health code request ready to send, authedCookie is: {}", authCookie);
            httpResponse = HttpUtil.get(CommonConst.HEALTH_CODE_URL, authCookie, CommonConst.COMMON_REFER);
            LogUtil.debugf(log, "health code request already sent, resp is: {}", httpResponse);
        } catch (Throwable e) {
            throw new IOException(e.toString(), e);
        }

        return JSONObject.parseObject(httpResponse.body());
    }
}
