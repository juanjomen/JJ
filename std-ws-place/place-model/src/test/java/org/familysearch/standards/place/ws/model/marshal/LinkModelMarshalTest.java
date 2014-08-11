package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class LinkModelMarshalTest extends BaseTest {

    LinkModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getLinkModel();
    }


    @Test(groups = { "unit" })
    public void testLinkJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        LinkModel model2 = POJOMarshalUtil.fromJSON(json, LinkModel.class);

        assertNotNull(model2);
        assertEquals(model.getHref(), model2.getHref());
        assertEquals(model.getHrefLang(), model2.getHrefLang());
        assertEquals(model.getLength(), model2.getLength());
        assertEquals(model.getRel(), model2.getRel());
        assertEquals(model.getTitle(), model2.getTitle());
        assertEquals(model.getType(), model2.getType());
    }

    @Test(groups = { "unit" })
    public void testLinkXML() {
        String xml = POJOMarshalUtil.toXML(model);

        LinkModel model2 = POJOMarshalUtil.fromXML(xml, LinkModel.class);

        assertNotNull(model2);
        assertEquals(model.getHref(), model2.getHref());
        assertEquals(model.getHrefLang(), model2.getHrefLang());
        assertEquals(model.getLength(), model2.getLength());
        assertEquals(model.getRel(), model2.getRel());
        assertEquals(model.getTitle(), model2.getTitle());
        assertEquals(model.getType(), model2.getType());
    }

}
