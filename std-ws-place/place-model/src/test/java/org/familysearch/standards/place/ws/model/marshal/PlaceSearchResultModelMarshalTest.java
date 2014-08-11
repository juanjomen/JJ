package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.PlaceSearchResultModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class PlaceSearchResultModelMarshalTest extends BaseTest {

    PlaceSearchResultModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getPlaceSearchResultModel();
    }


    @Test(groups = { "unit" })
    public void testPlaceSearchResultJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        PlaceSearchResultModel model2 = POJOMarshalUtil.fromJSON(json, PlaceSearchResultModel.class);

        assertNotNull(model2);
        assertEquals(model.getDistanceInKM(), model2.getDistanceInKM());
        assertEquals(model.getDistanceInMiles(), model2.getDistanceInMiles());
        assertEquals(model.getRawScore(), model2.getRawScore());
        assertEquals(model.getRelevanceScore(), model2.getRelevanceScore());
        assertEquals(model.getRep().getId(), model2.getRep().getId());
    }

    @Test(groups = { "unit" })
    public void testPlaceSearchResultXML() {
        String xml = POJOMarshalUtil.toXML(model);

        PlaceSearchResultModel model2 = POJOMarshalUtil.fromXML(xml, PlaceSearchResultModel.class);

        assertNotNull(model2);
        assertEquals(model.getDistanceInKM(), model2.getDistanceInKM());
        assertEquals(model.getDistanceInMiles(), model2.getDistanceInMiles());
        assertEquals(model.getRawScore(), model2.getRawScore());
        assertEquals(model.getRelevanceScore(), model2.getRelevanceScore());
        assertEquals(model.getRep().getId(), model2.getRep().getId());
    }

}
