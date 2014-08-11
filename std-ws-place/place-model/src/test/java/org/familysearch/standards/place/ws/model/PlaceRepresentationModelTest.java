package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class PlaceRepresentationModelTest {

    PlaceRepresentationModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new PlaceRepresentationModel();
    }


    @Test(groups = { "unit" })
    public void testPlaceRepresentationModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(PlaceRepresentationModel.class);
        pojoTest.test();
    }
}
