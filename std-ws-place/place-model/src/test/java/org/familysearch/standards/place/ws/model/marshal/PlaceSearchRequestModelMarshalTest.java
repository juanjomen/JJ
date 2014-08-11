package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.PlaceSearchRequestModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class PlaceSearchRequestModelMarshalTest extends BaseTest {

    PlaceSearchRequestModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getPlaceSearchRequestModel();
    }


    @Test(groups = { "unit" })
    public void testPlaceSearchRequestJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        PlaceSearchRequestModel model2 = POJOMarshalUtil.fromJSON(json, PlaceSearchRequestModel.class);

        assertNotNull(model2);
        assertEquals(model.getAcceptLanguage(), model2.getAcceptLanguage());
        assertEquals(model.getDistance(), model2.getDistance());
        assertEquals(model.getFilter(), model2.getFilter());
        assertEquals(model.getFilterParents(), model2.getFilterParents());
        assertEquals(model.getFilterTypeGroups(), model2.getFilterTypeGroups());
        assertEquals(model.getFilterTypes(), model2.getFilterTypes());
        assertEquals(model.getFuzzy(), model2.getFuzzy());
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getLanguage(), model2.getLanguage());
        assertEquals(model.getLatitude(), model2.getLatitude());
        assertEquals(model.getLimit(), model2.getLimit());
        assertEquals(model.getLongitude(), model2.getLongitude());
        assertEquals(model.getOptionalParents(), model2.getOptionalParents());
        assertEquals(model.getOptionalTypes(), model2.getOptionalTypes());
        assertEquals(model.getOptionalYears(), model2.getOptionalYears());
        assertEquals(model.getPartial(), model2.getPartial());
        assertEquals(model.getPriorityTypes(), model2.getPriorityTypes());
        assertEquals(model.getProfile(), model2.getProfile());
        assertEquals(model.getPublishedType(), model2.getPublishedType());
        assertEquals(model.getReqTypeGroups(), model2.getReqTypeGroups());
        assertEquals(model.getRequiredDirParents(), model2.getRequiredDirParents());
        assertEquals(model.getRequiredParents(), model2.getRequiredParents());
        assertEquals(model.getRequiredTypes(), model2.getRequiredTypes());
        assertEquals(model.getRequiredYears(), model2.getRequiredYears());
        assertEquals(model.getText(), model2.getText());
        assertEquals(model.getThreshold(), model2.getThreshold());
        assertEquals(model.getValidatedType(), model2.getValidatedType());
    }

    @Test(groups = { "unit" })
    public void testPlaceSearchRequestXML() {
        String xml = POJOMarshalUtil.toXML(model);

        PlaceSearchRequestModel model2 = POJOMarshalUtil.fromXML(xml, PlaceSearchRequestModel.class);

        assertNotNull(model2);
        assertEquals(model.getAcceptLanguage(), model2.getAcceptLanguage());
        assertEquals(model.getDistance(), model2.getDistance());
        assertEquals(model.getFilter(), model2.getFilter());
        assertEquals(model.getFilterParents(), model2.getFilterParents());
        assertEquals(model.getFilterTypeGroups(), model2.getFilterTypeGroups());
        assertEquals(model.getFilterTypes(), model2.getFilterTypes());
        assertEquals(model.getFuzzy(), model2.getFuzzy());
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getLanguage(), model2.getLanguage());
        assertEquals(model.getLatitude(), model2.getLatitude());
        assertEquals(model.getLimit(), model2.getLimit());
        assertEquals(model.getLongitude(), model2.getLongitude());
        assertEquals(model.getOptionalParents(), model2.getOptionalParents());
        assertEquals(model.getOptionalTypes(), model2.getOptionalTypes());
        assertEquals(model.getOptionalYears(), model2.getOptionalYears());
        assertEquals(model.getPartial(), model2.getPartial());
        assertEquals(model.getPriorityTypes(), model2.getPriorityTypes());
        assertEquals(model.getProfile(), model2.getProfile());
        assertEquals(model.getPublishedType(), model2.getPublishedType());
        assertEquals(model.getReqTypeGroups(), model2.getReqTypeGroups());
        assertEquals(model.getRequiredDirParents(), model2.getRequiredDirParents());
        assertEquals(model.getRequiredParents(), model2.getRequiredParents());
        assertEquals(model.getRequiredTypes(), model2.getRequiredTypes());
        assertEquals(model.getRequiredYears(), model2.getRequiredYears());
        assertEquals(model.getText(), model2.getText());
        assertEquals(model.getThreshold(), model2.getThreshold());
        assertEquals(model.getValidatedType(), model2.getValidatedType());
    }
}
