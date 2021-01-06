package com.dpitech.edge.common;

import lombok.Getter;

/**
 * @author rusheng
 */
public class CommonException extends RuntimeException {

    @Getter
    private final String context;

    public CommonException(String message, Throwable cause, String context) {
        super(message, cause);
        this.context = context;
    }
}
