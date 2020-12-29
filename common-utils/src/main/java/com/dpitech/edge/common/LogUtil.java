package com.dpitech.edge.common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * @author rusheng
 */
@Slf4j
public final class LogUtil {

    /**
     * debug 日志
     * @param logger logger
     * @param format format str
     * @param args 参数
     */
    public static void debugf(Logger logger, String format, Object ...args) {
        logger.debug(format, args);
    }

    /**
     * info 日志
     * @param logger logger
     * @param format format str
     * @param args 参数
     */
    public static void infof(Logger logger, String format, Object ...args) {
        logger.info(format, args);
    }

    /**
     * error 日志
     * @param logger logger
     * @param format format str
     * @param args 参数
     */
    public static void errorf(Logger logger, String format, Object ...args) {
        logger.error(format, args);
    }

    /**
     * error 日志
     * @param logger logger
     * @param format format str
     * @param cause Throwable cause
     * @param args 参数
     */
    public static void errorf(Logger logger, String format, Throwable cause, Object... args) {
        logger.error(format, args, cause);
    }
}
