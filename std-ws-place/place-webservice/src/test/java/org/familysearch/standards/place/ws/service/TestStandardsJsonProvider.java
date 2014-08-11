package org.familysearch.standards.place.ws.service;

import static org.testng.Assert.*;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class TestStandardsJsonProvider {

    StandardsJsonProvider healthCheck;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        healthCheck = new StandardsJsonProvider();
    }

    @Test(groups = {"unit"} )
    public void testStandardsJsonProvider() {
        assertNotNull(healthCheck);
    }

}
