package com.dpitech.edge.wfw.ua.excepton;

import com.dpitech.edge.common.CommonException;


/**
 * @author rusheng
 */
public class AuthException extends CommonException {

    public AuthException(String message, Throwable cause, String context) {
        super(message, cause, context);
    }

}
