package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class HealthCheckModelTest {

    HealthCheckModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new HealthCheckModel();
    }


    @Test(groups = { "unit" })
    public void testHealthCheckModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        assertNotNull(model);

        POJOTestUtil pojoTest = new POJOTestUtil(HealthCheckModel.class);
        pojoTest.test();
    }
}
