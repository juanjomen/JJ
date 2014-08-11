package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.TimingsModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TimingsModelMarshalTest extends BaseTest {

    TimingsModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getTimingsModel();
    }


    @Test(groups = { "unit" })
    public void testTimingsJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        TimingsModel model2 = POJOMarshalUtil.fromJSON(json, TimingsModel.class);

        assertNotNull(model2);
        assertEquals(model.getAssemblyTime(), model2.getAssemblyTime());
        assertEquals(model.getIdentifyCandidatesLookupTime(), model2.getIdentifyCandidatesLookupTime());
        assertEquals(model.getIdentifyCandidatesMaxHitFilterTime(), model2.getIdentifyCandidatesMaxHitFilterTime());
        assertEquals(model.getIdentifyCandidatesTailMatchTime(), model2.getIdentifyCandidatesTailMatchTime());
        assertEquals(model.getIdentifyCandidatesTime(), model2.getIdentifyCandidatesTime());
        assertEquals(model.getParseTime(), model2.getParseTime());
        assertEquals(model.getScoringTime(), model2.getScoringTime());
        assertEquals(model.getTotalTime(), model2.getTotalTime());
    }

    @Test(groups = { "unit" })
    public void testTimingsXML() {
        String xml = POJOMarshalUtil.toXML(model);

        TimingsModel model2 = POJOMarshalUtil.fromXML(xml, TimingsModel.class);

        assertNotNull(model2);
        assertEquals(model.getAssemblyTime(), model2.getAssemblyTime());
        assertEquals(model.getIdentifyCandidatesLookupTime(), model2.getIdentifyCandidatesLookupTime());
        assertEquals(model.getIdentifyCandidatesMaxHitFilterTime(), model2.getIdentifyCandidatesMaxHitFilterTime());
        assertEquals(model.getIdentifyCandidatesTailMatchTime(), model2.getIdentifyCandidatesTailMatchTime());
        assertEquals(model.getIdentifyCandidatesTime(), model2.getIdentifyCandidatesTime());
        assertEquals(model.getParseTime(), model2.getParseTime());
        assertEquals(model.getScoringTime(), model2.getScoringTime());
        assertEquals(model.getTotalTime(), model2.getTotalTime());
    }

}
