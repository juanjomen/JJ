package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class NameModelMarshalTest extends BaseTest {

    NameModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getNameModel();
    }


    @Test(groups = { "unit" })
    public void testNameJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        NameModel model2 = POJOMarshalUtil.fromJSON(json, NameModel.class);

        assertNotNull(model2);
        assertEquals(model.getLocale(), model2.getLocale());
        assertEquals(model.getName(), model2.getName());
    }

    @Test(groups = { "unit" })
    public void testNameXML() {
        String xml = POJOMarshalUtil.toXML(model);

        NameModel model2 = POJOMarshalUtil.fromXML(xml, NameModel.class);

        assertNotNull(model2);
        assertEquals(model.getLocale(), model2.getLocale());
        assertEquals(model.getName(), model2.getName());
    }

}
