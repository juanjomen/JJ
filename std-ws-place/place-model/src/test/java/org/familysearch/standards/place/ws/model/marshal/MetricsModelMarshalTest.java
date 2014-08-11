package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.MetricsModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class MetricsModelMarshalTest extends BaseTest {

    MetricsModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getMetricsModel();
    }


    @Test(groups = { "unit" })
    public void testMetricsJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        MetricsModel model2 = POJOMarshalUtil.fromJSON(json, MetricsModel.class);

        assertNotNull(model2);
        assertEquals(model.getCounts().getFinalParsedInputTextCount(), model2.getCounts().getFinalParsedInputTextCount());
        assertEquals(model.getScorers().getScorers().size(), model2.getScorers().getScorers().size());
        assertEquals(model.getTimings().getTotalTime(), model2.getTimings().getTotalTime());
    }

    @Test(groups = { "unit" })
    public void testMetricsXML() {
        String xml = POJOMarshalUtil.toXML(model);

        MetricsModel model2 = POJOMarshalUtil.fromXML(xml, MetricsModel.class);

        assertNotNull(model2);
        assertEquals(model.getCounts().getFinalParsedInputTextCount(), model2.getCounts().getFinalParsedInputTextCount());
        assertEquals(model.getScorers().getScorers().size(), model2.getScorers().getScorers().size());
        assertEquals(model.getTimings().getTotalTime(), model2.getTimings().getTotalTime());
    }

}
