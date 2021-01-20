package com.dpitech.edge.wfw.biz.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rusheng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelfInfoFetchModel implements FetchModel {

    /**
     * json对象构造实体
     * @param jsonItem json
     * @return model
     */
    public static SelfInfoFetchModel getInstanceFromJson(JSONObject jsonItem) {
        JSONObject data = jsonItem.getJSONObject("d").getJSONObject("base");
        JSONObject roleObj = data.getJSONObject("role");

        SelfInfoFetchModel.SelfInfoRole resRole = SelfInfoFetchModel.SelfInfoRole.builder()
                .identity(roleObj.getString("identity"))
                .identityId(roleObj.getString("identity_id"))
                .roleId(roleObj.getString("roleid"))
                .inOnDuty("1".equals(roleObj.getString("sfzx")))
                .stuNumber(roleObj.getString("number"))
                .build();

        return SelfInfoFetchModel.builder()
                .avatarUrl(data.getString("avatar"))
                .email(data.getString("email"))
                .sex(data.getString("sex"))
                .mobile(data.getString("mobile"))
                .realName(data.getString("realname"))
                .uid(data.getString("uid"))
                .role(resRole)
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelfInfoRole {
        private String roleId;

        private String stuNumber;

        private String identity;

        private String identityId;

        /**
         * 是否在校
         */
        private Boolean inOnDuty;

    }

    private String uid;

    private String realName;

    private String sex;

    private String email;

    private String mobile;

    /**
     * 头像url
     */
    private String avatarUrl;

    public SelfInfoRole role;
}
