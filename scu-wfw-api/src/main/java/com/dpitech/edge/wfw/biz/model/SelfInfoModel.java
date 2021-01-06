package com.dpitech.edge.wfw.biz.model;

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
public class SelfInfoModel {

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
