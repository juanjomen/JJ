package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class ScorersModelTest {

    ScorersModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new ScorersModel();
    }


    @Test(groups = { "unit" })
    public void testScorersModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(ScorersModel.class);
        pojoTest.addParameterValue(ScorerModel.class, new ScorerModel());
        pojoTest.test();
    }
}
