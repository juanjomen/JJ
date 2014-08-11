package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.TypeModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TypeModelMarshalTest extends BaseTest {

    TypeModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getTypeModel();
    }


    @Test(groups = { "unit" })
    public void testTypeModelJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        TypeModel model2 = POJOMarshalUtil.fromJSON(json, TypeModel.class);

        assertNotNull(model2);
        assertEquals(model.getCode(), model2.getCode());
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getLocalizedName().size(), model2.getLocalizedName().size());
        assertEquals(model.getSelfLink().getTitle(), model2.getSelfLink().getTitle());
    }

    @Test(groups = { "unit" })
    public void testTypeModelXML() {
        String xml = POJOMarshalUtil.toXML(model);

        TypeModel model2 = POJOMarshalUtil.fromXML(xml, TypeModel.class);

        assertNotNull(model2);
        assertEquals(model.getCode(), model2.getCode());
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getLocalizedName().size(), model2.getLocalizedName().size());
        assertEquals(model.getSelfLink().getTitle(), model2.getSelfLink().getTitle());
    }

}
