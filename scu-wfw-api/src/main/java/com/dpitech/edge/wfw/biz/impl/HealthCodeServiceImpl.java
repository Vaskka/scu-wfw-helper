package com.dpitech.edge.wfw.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.common.CommonConst;
import com.dpitech.edge.common.HttpUtil;
import com.dpitech.edge.common.LogUtil;
import com.dpitech.edge.wfw.biz.facade.HealthCodeService;
import com.dpitech.edge.wfw.ua.excepton.AuthException;
import com.dpitech.edge.wfw.ua.facade.Simulation;
import com.dpitech.edge.wfw.ua.impl.UsernamePasswordSimulationImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * 健康码接口实现
 * @author rusheng
 */
@Slf4j
public class HealthCodeServiceImpl implements HealthCodeService {

    /**
     * ua agent
     */
    private final Simulation simulation = new UsernamePasswordSimulationImpl();

    /**
     * 返回健康码接口获取原生json
     * @param stuNumber student number
     * @param psw ua password
     * @return json object
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     */
    @Override
    public JSONObject getHealthCodeInfo(String stuNumber, String psw) throws AuthException, IOException {
        String authCookie = simulation.getWfwCookieStringAuthenticated(stuNumber, psw);
        LogUtil.infof(log, "student: {} authenticated success.", stuNumber);

        HttpResponse<String> httpResponse;
        try {
            LogUtil.debugf(log, "two-step auth ready to send, student is: {}", stuNumber);
            httpResponse = HttpUtil.get(CommonConst.HEALTH_CODE_URL, authCookie, CommonConst.COMMON_REFER);
            LogUtil.debugf(log, "two-step auth already sent, resp is: {}", httpResponse);
        } catch (Throwable e) {
            throw new IOException(e.toString(), e);
        }

        return JSONObject.parseObject(httpResponse.body());
    }
}
