package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.VariantModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class VariantModelMarshalTest extends BaseTest {

    VariantModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getVariantModel();
    }


    @Test(groups = { "unit" })
    public void testVariantJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        VariantModel model2 = POJOMarshalUtil.fromJSON(json, VariantModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getName().getName(), model2.getName().getName());
        assertEquals(model.getType().getId(), model2.getType().getId());
    }

    @Test(groups = { "unit" })
    public void testVariantXML() {
        String xml = POJOMarshalUtil.toXML(model);

        VariantModel model2 = POJOMarshalUtil.fromXML(xml, VariantModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getName().getName(), model2.getName().getName());
        assertEquals(model.getType().getId(), model2.getType().getId());
    }

}
