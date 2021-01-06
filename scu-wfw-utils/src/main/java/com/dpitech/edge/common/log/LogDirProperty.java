package com.dpitech.edge.common.log;

import ch.qos.logback.core.PropertyDefinerBase;

import java.io.File;

/**
 * @author rusheng
 */
public class LogDirProperty extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        return System.getProperty("user.home") + File.separator + "logs";
    }
}
