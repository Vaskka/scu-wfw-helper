package com.dpitech.edge.wfw;

import com.dpitech.edge.wfw.biz.facade.HealthCodeService;
import com.dpitech.edge.wfw.biz.impl.HealthCodeServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CommonTests {

    private HealthCodeService healthCodeService = new HealthCodeServiceImpl();

    @Test
    public void commonTest() throws IOException {
        System.out.println(healthCodeService.getHealthCodeInfo("2017141463062", "czm19990216"));
    }
}
