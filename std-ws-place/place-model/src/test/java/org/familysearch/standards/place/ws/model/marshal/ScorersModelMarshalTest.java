package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.ScorersModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class ScorersModelMarshalTest extends BaseTest {

    ScorersModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getScorersModel();
    }


    @Test(groups = { "unit" })
    public void testScorersJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        ScorersModel model2 = POJOMarshalUtil.fromJSON(json, ScorersModel.class);

        assertNotNull(model2);
        assertEquals(model.getScorers().size(), model2.getScorers().size());
        for (int i=0;  i<model.getScorers().size();  i++) {
            assertEquals(model.getScorers().get(i).getName(), model2.getScorers().get(i).getName());
            assertEquals(model.getScorers().get(i).getScore(), model2.getScorers().get(i).getScore());
            assertEquals(model.getScorers().get(i).getTime(), model2.getScorers().get(i).getTime());
        }
        
    }

    @Test(groups = { "unit" })
    public void testScorersXML() {
        String xml = POJOMarshalUtil.toXML(model);

        ScorersModel model2 = POJOMarshalUtil.fromXML(xml, ScorersModel.class);

        assertNotNull(model2);
        assertEquals(model.getScorers().size(), model2.getScorers().size());
        for (int i=0;  i<model.getScorers().size();  i++) {
            assertEquals(model.getScorers().get(i).getName(), model2.getScorers().get(i).getName());
            assertEquals(model.getScorers().get(i).getScore(), model2.getScorers().get(i).getScore());
            assertEquals(model.getScorers().get(i).getTime(), model2.getScorers().get(i).getTime());
        }
    }

}
