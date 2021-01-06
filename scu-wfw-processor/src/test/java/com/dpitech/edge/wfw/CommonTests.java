package com.dpitech.edge.wfw;

import com.dpitech.edge.wfw.biz.facade.HealthCodeProcessor;
import com.dpitech.edge.wfw.biz.impl.HealthCodeProcessorImpl;
import com.dpitech.edge.wfw.ua.facade.Simulation;
import com.dpitech.edge.wfw.ua.impl.SimulationImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Scanner;

public class CommonTests {

    private Simulation simulation = new SimulationImpl();

    private HealthCodeProcessor healthCodeProcessor = new HealthCodeProcessorImpl();

    @Test
    public void commonTest() throws IOException {
    }
}
