package com.dpitech.edge.wfw.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.dpitech.edge.common.CommonConst;
import com.dpitech.edge.common.HttpUtil;
import com.dpitech.edge.wfw.biz.exceptions.ApiException;
import com.dpitech.edge.wfw.biz.facade.SelfInfoProcessor;
import com.dpitech.edge.wfw.biz.model.SelfInfoModel;
import com.dpitech.edge.wfw.ua.excepton.AuthException;

import java.io.IOException;

/**
 *
 *  个人信息接口实现
 *
 * @author rusheng
 */
public class SelfInfoProcessorImpl implements SelfInfoProcessor {

    /**
     * 返回健康码接口获取原生json
     * @param authCookie 已经赋权的cookie-line
     * @return json object
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    @Override
    public JSONObject getRawJsonData(String authCookie) throws AuthException, IOException, ApiException {
        String respBody = null;
        try {
            respBody = HttpUtil.get(CommonConst.SELF_INFO_URL, authCookie, CommonConst.COMMON_REFER).body();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }

        JSONObject bodyJson = JSONObject.parseObject(respBody);
        if (bodyJson.getInteger("e") == null || !bodyJson.getInteger("e").equals(0)) {
            throw new ApiException("response get error, errCode: " +
                    bodyJson.getInteger("e") +
                    " errMsg:" + bodyJson.getString("m"), new RuntimeException(), respBody);
        }

        return bodyJson;
    }

    /**
     * 获得组装好的java对象，过滤了不常用的字段
     * @param authCookie 已经鉴权的cookie
     * @return SelfInfoModel
     * @throws AuthException 用户名密码错误
     * @throws IOException api call error.
     * @throws ApiException api exception.
     */
    @Override
    public SelfInfoModel getInfo(String authCookie) throws AuthException, IOException, ApiException {
        JSONObject rawData = getRawJsonData(authCookie);
        JSONObject data = rawData.getJSONObject("d").getJSONObject("base");
        JSONObject roleObj = data.getJSONObject("role");

        SelfInfoModel.SelfInfoRole resRole = SelfInfoModel.SelfInfoRole.builder()
                .identity(roleObj.getString("identity"))
                .identityId(roleObj.getString("identity_id"))
                .roleId(roleObj.getString("roleid"))
                .inOnDuty("1".equals(roleObj.getString("sfzx")))
                .stuNumber(roleObj.getString("number"))
                .build();

        return SelfInfoModel.builder()
                .avatarUrl(data.getString("avatar"))
                .email(data.getString("email"))
                .sex(data.getString("sex"))
                .mobile(data.getString("mobile"))
                .realName(data.getString("realname"))
                .uid(data.getString("uid"))
                .role(resRole)
                .build();
    }
}
