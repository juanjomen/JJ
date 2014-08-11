package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.CentroidModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class CentroldModelMarshalTest extends BaseTest {

    CentroidModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getCentroidModel();
    }


    @Test(groups = { "unit" })
    public void testCentroidJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        CentroidModel model2 = POJOMarshalUtil.fromJSON(json, CentroidModel.class);

        assertNotNull(model2);
        assertEquals(model.getLatitude(), model2.getLatitude(), 0.0001);
        assertEquals(model.getLongitude(), model2.getLongitude(), 0.0001);
    }

    @Test(groups = { "unit" })
    public void testCentroidXML() {
        String xml = POJOMarshalUtil.toXML(model);

        CentroidModel model2 = POJOMarshalUtil.fromXML(xml, CentroidModel.class);

        assertNotNull(model2);
        assertEquals(model.getLatitude(), model2.getLatitude(), 0.0001);
        assertEquals(model.getLongitude(), model2.getLongitude(), 0.0001);
    }

}
