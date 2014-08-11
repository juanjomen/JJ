package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.CitationModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class CitationModelMarshalTest extends BaseTest {

    CitationModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getCitationModel();
    }


    @Test(groups = { "unit" })
    public void testCitationJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        CitationModel model2 = POJOMarshalUtil.fromJSON(json, CitationModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getRepId(), model2.getRepId());
        assertEquals(model.getType().getId(), model2.getType().getId());
        assertEquals(model.getSourceId(), model2.getSourceId());
        assertEquals(model.getDescription(), model2.getDescription());
        assertEquals(model.getSourceRef(), model2.getSourceRef());
        assertEquals(model.getCitDate(), model2.getCitDate());
        assertEquals(model.getLinks().size(), model2.getLinks().size());
    }

    @Test(groups = { "unit" })
    public void testCitationXML() {
        String xml = POJOMarshalUtil.toXML(model);
        CitationModel model2 = POJOMarshalUtil.fromXML(xml, CitationModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getRepId(), model2.getRepId());
        assertEquals(model.getType().getId(), model2.getType().getId());
        assertEquals(model.getSourceId(), model2.getSourceId());
        assertEquals(model.getDescription(), model2.getDescription());
        assertEquals(model.getSourceRef(), model2.getSourceRef());
        assertEquals(model.getCitDate(), model2.getCitDate());
        assertEquals(model.getLinks().size(), model2.getLinks().size());
    }

}
