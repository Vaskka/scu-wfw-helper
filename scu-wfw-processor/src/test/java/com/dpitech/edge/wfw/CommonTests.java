package com.dpitech.edge.wfw;

import com.dpitech.edge.wfw.biz.facade.HealthCodeProcessor;
import com.dpitech.edge.wfw.biz.facade.HealthReportProcessor;
import com.dpitech.edge.wfw.biz.facade.SchoolNetworkProcessor;
import com.dpitech.edge.wfw.biz.facade.SelfInfoProcessor;
import com.dpitech.edge.wfw.biz.impl.HealthCodeProcessorImpl;
import com.dpitech.edge.wfw.biz.impl.HealthReportProcessorImpl;
import com.dpitech.edge.wfw.biz.impl.SchoolNetworkProcessorImpl;
import com.dpitech.edge.wfw.biz.impl.SelfInfoProcessorImpl;
import com.dpitech.edge.wfw.ua.facade.Simulation;
import com.dpitech.edge.wfw.ua.impl.SimulationImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CommonTests {

    private Simulation simulation = new SimulationImpl();

    private HealthCodeProcessor healthCodeProcessor = new HealthCodeProcessorImpl();

    private HealthReportProcessorImpl healthReportProcessor = new HealthReportProcessorImpl();

    private SelfInfoProcessor selfInfoProcessor = new SelfInfoProcessorImpl();

    private SchoolNetworkProcessor schoolNetworkProcessor = new SchoolNetworkProcessorImpl();

    @Test
    public void commonTest() throws IOException {

        String cookie = simulation.getWfwCookieStringAuthenticatedUsername("2017141463062", () -> "czm19990216");
        System.out.println(healthReportProcessor.reportJiangAn(cookie));

    }
}
