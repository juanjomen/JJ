package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.AttributeModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class AttributeModelMarshalTest extends BaseTest {

    AttributeModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getAttributeModel();
    }


    @Test(groups = { "unit" })
    public void testAttributeJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        AttributeModel model2 = POJOMarshalUtil.fromJSON(json, AttributeModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getRepId(), model2.getRepId());
        assertEquals(model.getType().getId(), model2.getType().getId());
        assertEquals(model.getYear(), model2.getYear());
        assertEquals(model.getValue(), model2.getValue());
        assertEquals(model.getLinks().size(), model2.getLinks().size());
    }

    @Test(groups = { "unit" })
    public void testAttributeXML() {
        String xml = POJOMarshalUtil.toXML(model);

        AttributeModel model2 = POJOMarshalUtil.fromXML(xml, AttributeModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getRepId(), model2.getRepId());
        assertEquals(model.getType().getId(), model2.getType().getId());
        assertEquals(model.getYear(), model2.getYear());
        assertEquals(model.getValue(), model2.getValue());
        assertEquals(model.getLinks().size(), model2.getLinks().size());
    }

}
