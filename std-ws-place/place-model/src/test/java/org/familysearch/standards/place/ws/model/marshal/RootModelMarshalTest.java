package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.RootModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class RootModelMarshalTest extends BaseTest {

    RootModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getRootModel();
    }


    @Test(groups = { "unit" })
    public void testRootJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        RootModel model2 = POJOMarshalUtil.fromJSON(json, RootModel.class);

        assertNotNull(model2);
        assertEquals(model.getHealthCheck().getAPIVersion(), model2.getHealthCheck().getAPIVersion());
        assertEquals(model.getPlace().getId(), model2.getPlace().getId());
        assertEquals(model.getType().getCode(), model2.getType().getCode());
        assertEquals(model.getTypes().size(), model2.getTypes().size());
        assertEquals(model.getPlaceRepresentation().getId(), model2.getPlaceRepresentation().getId());
        assertEquals(model.getPlaceTypeGroup().getId(), model2.getPlaceTypeGroup().getId());
        assertEquals(model.getPlaceTypeGroups().size(), model2.getPlaceTypeGroups().size());
        assertEquals(model.getRequests().size(), model2.getRequests().size());
        assertEquals(model.getSearchResults().size(), model2.getSearchResults().size());
    }

    @Test(groups = { "unit" })
    public void testRootXML() {
        String xml = POJOMarshalUtil.toXML(model);

        RootModel model2 = POJOMarshalUtil.fromXML(xml, RootModel.class);

        assertNotNull(model2);
        assertEquals(model.getHealthCheck().getAPIVersion(), model2.getHealthCheck().getAPIVersion());
        assertEquals(model.getPlace().getId(), model2.getPlace().getId());
        assertEquals(model.getType().getCode(), model2.getType().getCode());
        assertEquals(model.getTypes().size(), model2.getTypes().size());
        assertEquals(model.getPlaceRepresentation().getId(), model2.getPlaceRepresentation().getId());
        assertEquals(model.getPlaceTypeGroup().getId(), model2.getPlaceTypeGroup().getId());
        assertEquals(model.getPlaceTypeGroups().size(), model2.getPlaceTypeGroups().size());
        assertEquals(model.getRequests().size(), model2.getRequests().size());
        assertEquals(model.getSearchResults().size(), model2.getSearchResults().size());
    }
}
