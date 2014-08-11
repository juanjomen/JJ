package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class AttributeModelTest {

    AttributeModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new AttributeModel();
    }


    @Test(groups = { "unit" })
    public void testAttributeModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(AttributeModel.class);
        pojoTest.test();
    }
}
