package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class LocalizedNameDescModelTest {

    LocalizedNameDescModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new LocalizedNameDescModel();
    }


    @Test(groups = { "unit" })
    public void testLocalizedNameDescModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(LocalizedNameDescModel.class);
        pojoTest.test();
    }
}
