package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.LocalizedNameDescModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class LocalizedNameDescModelMarshalTest extends BaseTest {

    LocalizedNameDescModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getLocalizedNameDescModel();
    }


    @Test(groups = { "unit" })
    public void testLocalizedNameDescJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        LocalizedNameDescModel model2 = POJOMarshalUtil.fromJSON(json, LocalizedNameDescModel.class);

        assertNotNull(model2);
        assertEquals(model.getDescription(), model2.getDescription());
        assertEquals(model.getLocale(), model2.getLocale());
        assertEquals(model.getName(), model2.getName());
    }

    @Test(groups = { "unit" })
    public void testLocalizedNameDescXML() {
        String xml = POJOMarshalUtil.toXML(model);

        LocalizedNameDescModel model2 = POJOMarshalUtil.fromXML(xml, LocalizedNameDescModel.class);

        assertNotNull(model2);
        assertEquals(model.getDescription(), model2.getDescription());
        assertEquals(model.getLocale(), model2.getLocale());
        assertEquals(model.getName(), model2.getName());
    }

}
