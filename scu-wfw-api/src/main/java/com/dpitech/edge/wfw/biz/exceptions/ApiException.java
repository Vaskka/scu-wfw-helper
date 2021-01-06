package com.dpitech.edge.wfw.biz.exceptions;

import com.dpitech.edge.common.CommonException;

/**
 * @author rusheng
 */
public class ApiException extends CommonException {
    public ApiException(String message, Throwable cause, String context) {
        super(message, cause, context);
    }
}
