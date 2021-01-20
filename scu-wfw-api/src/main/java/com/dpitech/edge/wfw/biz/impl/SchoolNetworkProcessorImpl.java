package com.dpitech.edge.wfw.biz.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.common.CommonConst;
import com.dpitech.edge.common.HttpUtil;
import com.dpitech.edge.common.log.LogUtil;
import com.dpitech.edge.wfw.biz.exceptions.ApiException;
import com.dpitech.edge.wfw.biz.facade.SchoolNetworkProcessor;
import com.dpitech.edge.wfw.biz.model.SchoolNetworkDeviceFetchModel;
import com.dpitech.edge.wfw.ua.excepton.AuthException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rusheng
 */
@Slf4j
public class SchoolNetworkProcessorImpl implements SchoolNetworkProcessor {

    /**
     * 得到校园网在线的设备列表
     * @param authCookie 鉴权后的cookie
     * @return raw list of mac、ip
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    @Override
    public JSONObject getRawJsonData(String authCookie) throws AuthException, IOException, ApiException {

        String respBody;
        try {
            respBody = HttpUtil.postWithFormData(CommonConst.SCHOOL_NETWORK_DEVICE_URL, "",
                    authCookie, CommonConst.COMMON_REFER).body();
        } catch (InterruptedException e) {
            throw new ApiException(e.toString(), e, "get network client error.");
        }

        LogUtil.infof(log, "network device list api get response: {}", respBody);

        return JSONObject.parseObject(respBody);
    }

    /**
     * 得到校园网在线的设备列表
     * @param authCookie 鉴权后的cookie
     * @return SchoolNetworkDeviceModel mac、ip
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    @Override
    public List<SchoolNetworkDeviceFetchModel> getSchoolNetworkInfo(String authCookie) throws AuthException, IOException, ApiException {
        JSONObject resp = getRawJsonData(authCookie);
        if (resp.getInteger("e") == null || !resp.getInteger("e").equals(0)) {
            throw new ApiException("get network device error, msg=" + resp.getString("m"), new RuntimeException(), resp.toJSONString());
        }

        JSONArray list = resp.getJSONObject("d").getJSONArray("list");
        if (list == null) {
            return new ArrayList<>();
        }
        else {
            return list.stream()
                    .map(jsonItem -> SchoolNetworkDeviceFetchModel.getInstanceFromJson((JSONObject) jsonItem))
                    .collect(Collectors.toList());
        }
    }
}
