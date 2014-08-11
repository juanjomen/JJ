package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.JurisdictionModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class JurisdictionModelMarshalTest extends BaseTest {

    JurisdictionModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getJurisdictionModel();
    }


    @Test(groups = { "unit" })
    public void testJurisdictionJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        JurisdictionModel model2 = POJOMarshalUtil.fromJSON(json, JurisdictionModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getName(), model2.getName());
        assertEquals(model.getLocale(), model2.getLocale());
        assertEquals(model.getSelfLink().getHref(), model2.getSelfLink().getHref());
        assertEquals(model.getParent().getId(), model2.getParent().getId());
    }

    @Test(groups = { "unit" })
    public void testJurisdictionXML() {
        String xml = POJOMarshalUtil.toXML(model);

        JurisdictionModel model2 = POJOMarshalUtil.fromXML(xml, JurisdictionModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getName(), model2.getName());
        assertEquals(model.getLocale(), model2.getLocale());
        assertEquals(model.getSelfLink().getHref(), model2.getSelfLink().getHref());
        assertEquals(model.getParent().getId(), model2.getParent().getId());
    }

}
