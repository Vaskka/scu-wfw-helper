package com.dpitech.edge.wfw.biz.facade;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;

/**
 *
 * scu微服务，健康码接口
 *
 * @author rusheng
 */
public interface HealthCodeService {

    /**
     * 返回健康码接口获取原生json
     * @param stuNumber student number
     * @param psw ua password
     * @return json object
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     */
    JSONObject getHealthCodeInfo(String stuNumber, String psw) throws AuthException, IOException;

}
