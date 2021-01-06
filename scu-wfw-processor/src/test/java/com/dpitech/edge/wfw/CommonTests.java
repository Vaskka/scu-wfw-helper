package com.dpitech.edge.wfw;

import com.dpitech.edge.wfw.biz.facade.HealthCodeProcessor;
import com.dpitech.edge.wfw.biz.impl.HealthCodeProcessorImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CommonTests {

    private HealthCodeProcessor healthCodeProcessor = new HealthCodeProcessorImpl();

    @Test
    public void commonTest() throws IOException {
        System.out.println(healthCodeProcessor.getHealthCodeInfo("2017141463062", "czm19990216"));
    }
}
