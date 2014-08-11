package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.PlaceSearchResultsModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class PlaceSearchResultsModelMarshalTest extends BaseTest {

    PlaceSearchResultsModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getPlaceSearchResultsModel();
    }


    @Test(groups = { "unit" })
    public void testPlaceSearchResultJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        PlaceSearchResultsModel model2 = POJOMarshalUtil.fromJSON(json, PlaceSearchResultsModel.class);

        assertNotNull(model2);
        assertEquals(model.getCount(), model2.getCount());
        assertEquals(model.getRefId(), model2.getRefId());
        assertEquals(model.getMetrics().getCounts().getRawCandidateCount(), model2.getMetrics().getCounts().getRawCandidateCount());
        assertEquals(model.getResults().size(), model2.getResults().size());
    }

    @Test(groups = { "unit" })
    public void testPlaceSearchResultXML() {
        String xml = POJOMarshalUtil.toXML(model);

        PlaceSearchResultsModel model2 = POJOMarshalUtil.fromXML(xml, PlaceSearchResultsModel.class);

        assertNotNull(model2);
        assertEquals(model.getCount(), model2.getCount());
        assertEquals(model.getRefId(), model2.getRefId());
        assertEquals(model.getMetrics().getCounts().getRawCandidateCount(), model2.getMetrics().getCounts().getRawCandidateCount());
        assertEquals(model.getResults().size(), model2.getResults().size());
    }

}
