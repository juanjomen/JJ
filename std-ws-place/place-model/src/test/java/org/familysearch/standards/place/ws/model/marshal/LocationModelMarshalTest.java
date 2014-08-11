package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.LocationModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class LocationModelMarshalTest extends BaseTest {

    LocationModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getLocationModel();
    }


    @Test(groups = { "unit" })
    public void testLocationJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        LocationModel model2 = POJOMarshalUtil.fromJSON(json, LocationModel.class);

        assertNotNull(model2);
        assertEquals(model.getCentroid().getLatitude(), model2.getCentroid().getLatitude());
    }

    @Test(groups = { "unit" })
    public void testLocationXML() {
        String xml = POJOMarshalUtil.toXML(model);

        LocationModel model2 = POJOMarshalUtil.fromXML(xml, LocationModel.class);

        assertNotNull(model2);
        assertEquals(model.getCentroid().getLatitude(), model2.getCentroid().getLatitude());
    }

}
