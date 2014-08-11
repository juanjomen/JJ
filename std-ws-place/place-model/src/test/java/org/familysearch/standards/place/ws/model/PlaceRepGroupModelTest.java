package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class PlaceRepGroupModelTest {

    PlaceRepGroupModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new PlaceRepGroupModel();
    }


    @Test(groups = { "unit" })
    public void testTypeGroupModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(PlaceRepGroupModel.class);
        pojoTest.test();
    }
}
