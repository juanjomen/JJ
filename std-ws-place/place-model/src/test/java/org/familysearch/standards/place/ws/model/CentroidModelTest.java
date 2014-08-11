package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class CentroidModelTest {

    CentroidModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new CentroidModel();
    }


    @Test(groups = { "unit" })
    public void testCentroidModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(CentroidModel.class);
        pojoTest.test();
    }
}
