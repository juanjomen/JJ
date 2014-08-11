package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class PlaceSearchRequestModelTest {

    PlaceSearchRequestModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new PlaceSearchRequestModel();
    }


    @Test(groups = { "unit" })
    public void testPlaceSearchRequestModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(PlaceSearchRequestModel.class);
        pojoTest.test();
    }

    @Test(groups = { "unit" })
    public void testOptionalYears() {
        assertNull(model.getOptionalYears());

        model.setOptionalYears(null);
        assertNull(model.getOptionalYears());
    }

    @Test(groups = { "unit" })
    public void testRequiredYears() {
        assertNull(model.getRequiredYears());

        model.setRequiredYears(null);
        assertNull(model.getRequiredYears());
    }
}
