package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.CountsModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class CountsModelMarshalTest extends BaseTest {

    CountsModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getCountsModel();
    }


    @Test(groups = { "unit" })
    public void testCountsJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        CountsModel model2 = POJOMarshalUtil.fromJSON(json, CountsModel.class);

        assertNotNull(model2);
        assertEquals(model.getFinalParsedInputTextCount(), model2.getFinalParsedInputTextCount());
        assertEquals(model.getInitialParsedInputTextCount(), model2.getInitialParsedInputTextCount());
        assertEquals(model.getPreScoringCandidateCount(), model2.getPreScoringCandidateCount());
        assertEquals(model.getRawCandidateCount(), model2.getRawCandidateCount());
    }

    @Test(groups = { "unit" })
    public void testCountsXML() {
        String xml = POJOMarshalUtil.toXML(model);

        CountsModel model2 = POJOMarshalUtil.fromXML(xml, CountsModel.class);

        assertNotNull(model2);
        assertEquals(model.getFinalParsedInputTextCount(), model2.getFinalParsedInputTextCount());
        assertEquals(model.getInitialParsedInputTextCount(), model2.getInitialParsedInputTextCount());
        assertEquals(model.getPreScoringCandidateCount(), model2.getPreScoringCandidateCount());
        assertEquals(model.getRawCandidateCount(), model2.getRawCandidateCount());
    }

}
