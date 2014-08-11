package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class JurisdictionModelTest {

    JurisdictionModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new JurisdictionModel();
    }


    @Test(groups = { "unit" })
    public void testJurisdictionModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(JurisdictionModel.class);
        pojoTest.test();
    }
}
