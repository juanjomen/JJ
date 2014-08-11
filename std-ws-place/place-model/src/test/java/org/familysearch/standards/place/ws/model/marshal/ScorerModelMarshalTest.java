package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.ScorerModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class ScorerModelMarshalTest extends BaseTest {

    ScorerModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getScorerModel();
    }


    @Test(groups = { "unit" })
    public void testScorerJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        ScorerModel model2 = POJOMarshalUtil.fromJSON(json, ScorerModel.class);

        assertNotNull(model2);
        assertEquals(model.getName(), model2.getName());
        assertEquals(model.getScore(), model2.getScore());
        assertEquals(model.getTime(), model2.getTime());
    }

    @Test(groups = { "unit" })
    public void testScorerXML() {
        String xml = POJOMarshalUtil.toXML(model);

        ScorerModel model2 = POJOMarshalUtil.fromXML(xml, ScorerModel.class);

        assertNotNull(model2);
        assertEquals(model.getName(), model2.getName());
        assertEquals(model.getScore(), model2.getScore());
        assertEquals(model.getTime(), model2.getTime());
    }

}
