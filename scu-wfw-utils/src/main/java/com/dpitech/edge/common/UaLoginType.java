package com.dpitech.edge.common;

import lombok.Getter;

/**
 * 当前版本只支持用户名密码登录
 * @author rusheng
 */
public enum UaLoginType {

    /**
     * 用户名密码登录模式
     */
    USERNAME_PSW("username_password"),

    /**
     * 手机验证码模式
     */
    PHONE_CODE("username_smstoken")
    ;

    @Getter
    private final String val;

    UaLoginType(String val) {
        this.val = val;
    }

}
