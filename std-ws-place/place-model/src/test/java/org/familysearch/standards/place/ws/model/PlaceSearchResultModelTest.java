package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class PlaceSearchResultModelTest {

    PlaceSearchResultModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new PlaceSearchResultModel();
    }


    @Test(groups = { "unit" })
    public void testPlaceSearchResultModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(PlaceSearchResultModel.class);
        pojoTest.test();
    }
}
