package com.dpitech.edge.wfw.biz.facade;

import com.dpitech.edge.wfw.biz.exceptions.ApiException;
import com.dpitech.edge.wfw.biz.model.SchoolNetworkDeviceFetchModel;
import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;
import java.util.List;

/**
 * @author rusheng
 */
public interface SchoolNetworkProcessor extends BaseFetchApiProcessor {

    /**
     * 得到校园网在线的设备列表
     * @param authCookie 鉴权后的cookie
     * @return SchoolNetworkDeviceModel mac、ip
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    List<SchoolNetworkDeviceFetchModel> getSchoolNetworkInfo(String authCookie) throws AuthException, IOException, ApiException;

}
