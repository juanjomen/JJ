package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class CountsModelTest {

    CountsModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new CountsModel();
    }


    @Test(groups = { "unit" })
    public void testCountsModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(CountsModel.class);
        pojoTest.test();
    }
}
