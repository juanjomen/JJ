package org.familysearch.standards.place.ws.service;

import static org.testng.Assert.*;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class TestHealthCheck {

    HealthCheck healthCheck;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        healthCheck = new HealthCheck();
    }

    @Test(groups = {"unit"} )
    public void testHealthCheck() {
        assertNotNull(healthCheck);
    }

}
