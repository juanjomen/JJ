package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.HealthCheckModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class HealthCheckModelMarshalTest extends BaseTest {

    HealthCheckModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getHealthCheckModel();
    }


    @Test(groups = { "unit" })
    public void testHealthCheckJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        HealthCheckModel model2 = POJOMarshalUtil.fromJSON(json, HealthCheckModel.class);

        assertNotNull(model2);
        assertEquals(model.getAPIVersion(), model2.getAPIVersion());
        assertEquals(model.getCurrentRevision(), model2.getCurrentRevision());
        assertEquals(model.getWSVersion(), model2.getWSVersion());
        assertEquals(model.getLinks().size(), model2.getLinks().size());
    }

    @Test(groups = { "unit" })
    public void testHealthCheckXML() {
        String xml = POJOMarshalUtil.toXML(model);

        HealthCheckModel model2 = POJOMarshalUtil.fromXML(xml, HealthCheckModel.class);

        assertNotNull(model2);
        assertEquals(model.getAPIVersion(), model2.getAPIVersion());
        assertEquals(model.getCurrentRevision(), model2.getCurrentRevision());
        assertEquals(model.getWSVersion(), model2.getWSVersion());
        assertEquals(model.getLinks().size(), model2.getLinks().size());
    }

}
