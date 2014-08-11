package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class MetricsModelTest {

    MetricsModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new MetricsModel();
    }


    @Test(groups = { "unit" })
    public void testMetricsModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(MetricsModel.class);
        pojoTest.test();
    }
}
