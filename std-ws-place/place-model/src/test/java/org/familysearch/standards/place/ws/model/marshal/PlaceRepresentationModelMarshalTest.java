package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class PlaceRepresentationModelMarshalTest extends BaseTest {

    PlaceRepresentationModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getPlaceRepresentationModel();
    }


    @Test(groups = { "unit" })
    public void testPlaceRepresentationJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        PlaceRepresentationModel model2 = POJOMarshalUtil.fromJSON(json, PlaceRepresentationModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getJurisdiction().getName(), model2.getJurisdiction().getName());
        assertEquals(model.getType().getId(), model2.getType().getId());
        assertEquals(model.getOwnerId(), model2.getOwnerId());
        assertEquals(model.getGroup().getId(), model2.getGroup().getId());
        assertEquals(model.getFromYear(), model2.getFromYear());
        assertEquals(model.getToYear(), model2.getToYear());
        assertEquals(model.getPreferredLocale(), model2.getPreferredLocale());
        assertEquals(model.isPublished(), model2.isPublished());
        assertEquals(model.isValidated(), model2.isValidated());
        assertEquals(model.getLocation().getCentroid().getLatitude(), model2.getLocation().getCentroid().getLatitude());
        assertEquals(model.getUUID(), model2.getUUID());
        assertEquals(model.getRevision(), model2.getRevision());
        assertEquals(model.getLinks().size(), model2.getLinks().size());
        assertEquals(model.getDisplayName().getName(), model2.getDisplayName().getName());
        assertEquals(model.getFullDisplayName().getName(), model2.getFullDisplayName().getName());
        assertEquals(model.getDisplayNames().size(), model2.getDisplayNames().size());
        assertEquals(model.getChildren().size(), model2.getChildren().size());
    }

    @Test(groups = { "unit" })
    public void testPlaceRepresentationXML() {
        String xml = POJOMarshalUtil.toXML(model);

        PlaceRepresentationModel model2 = POJOMarshalUtil.fromXML(xml, PlaceRepresentationModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getJurisdiction().getName(), model2.getJurisdiction().getName());
        assertEquals(model.getType().getId(), model2.getType().getId());
        assertEquals(model.getOwnerId(), model2.getOwnerId());
        assertEquals(model.getGroup().getId(), model2.getGroup().getId());
        assertEquals(model.getFromYear(), model2.getFromYear());
        assertEquals(model.getToYear(), model2.getToYear());
        assertEquals(model.getPreferredLocale(), model2.getPreferredLocale());
        assertEquals(model.isPublished(), model2.isPublished());
        assertEquals(model.isValidated(), model2.isValidated());
        assertEquals(model.getLocation().getCentroid().getLatitude(), model2.getLocation().getCentroid().getLatitude());
        assertEquals(model.getUUID(), model2.getUUID());
        assertEquals(model.getRevision(), model2.getRevision());
        assertEquals(model.getLinks().size(), model2.getLinks().size());
        assertEquals(model.getDisplayName().getName(), model2.getDisplayName().getName());
        assertEquals(model.getFullDisplayName().getName(), model2.getFullDisplayName().getName());
        assertEquals(model.getDisplayNames().size(), model2.getDisplayNames().size());
        assertEquals(model.getChildren().size(), model2.getChildren().size());
    }

}
