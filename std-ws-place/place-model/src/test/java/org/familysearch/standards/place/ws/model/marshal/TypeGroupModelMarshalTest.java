package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.PlaceTypeGroupModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TypeGroupModelMarshalTest extends BaseTest {

    PlaceTypeGroupModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getTypeGroupModel();
    }


    @Test(groups = { "unit" })
    public void testTypeGroupModelJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        PlaceTypeGroupModel model2 = POJOMarshalUtil.fromJSON(json, PlaceTypeGroupModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getLocalizedName().get(0).getName(), model2.getLocalizedName().get(0).getName());
        assertEquals(model.getLocalizedName().get(0).getDescription(), model2.getLocalizedName().get(0).getDescription());
        assertEquals(model.getSelfLink().getHref(), model2.getSelfLink().getHref());
        assertEquals(model.getSubGroups().size(), model2.getSubGroups().size());
        assertEquals(model.getTypes().size(), model2.getTypes().size());
    }

    @Test(groups = { "unit" })
    public void testTypeGroupModelXML() {
        String xml = POJOMarshalUtil.toXML(model);

        PlaceTypeGroupModel model2 = POJOMarshalUtil.fromXML(xml, PlaceTypeGroupModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getLocalizedName().get(0).getName(), model2.getLocalizedName().get(0).getName());
        assertEquals(model.getLocalizedName().get(0).getDescription(), model2.getLocalizedName().get(0).getDescription());
        assertEquals(model.getSelfLink().getHref(), model2.getSelfLink().getHref());
        assertEquals(model.getSubGroups().size(), model2.getSubGroups().size());
        assertEquals(model.getTypes().size(), model2.getTypes().size());
    }

}
