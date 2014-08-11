package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.PlaceModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class PlaceModelMarshalTest extends BaseTest {

    PlaceModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getPlaceModel();
    }


    @Test(groups = { "unit" })
    public void testPlaceJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        PlaceModel model2 = POJOMarshalUtil.fromJSON(json, PlaceModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getFromYear(), model2.getFromYear());
        assertEquals(model.getToYear(), model2.getToYear());
        assertEquals(model.getVariants().size(), model2.getVariants().size());
        assertEquals(model.getReps().size(), model2.getReps().size());
    }

    @Test(groups = { "unit" })
    public void testPlaceXML() {
        String xml = POJOMarshalUtil.toXML(model);

        PlaceModel model2 = POJOMarshalUtil.fromXML(xml, PlaceModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getFromYear(), model2.getFromYear());
        assertEquals(model.getToYear(), model2.getToYear());
        assertEquals(model.getVariants().size(), model2.getVariants().size());
        assertEquals(model.getReps().size(), model2.getReps().size());
    }

}
