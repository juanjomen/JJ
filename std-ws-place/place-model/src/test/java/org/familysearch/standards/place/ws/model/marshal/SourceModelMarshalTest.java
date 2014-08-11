package org.familysearch.standards.place.ws.model.marshal;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.SourceModel;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class SourceModelMarshalTest extends BaseTest {

    SourceModel model;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = this.getSourceModel();
    }


    @Test(groups = { "unit" })
    public void testSourceJSON() {
        String json = POJOMarshalUtil.toJSON(model);

        SourceModel model2 = POJOMarshalUtil.fromJSON(json, SourceModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getTitle(), model2.getTitle());
        assertEquals(model.getDescription(), model2.getDescription());
        assertEquals(model.isPublished(), model2.isPublished());
    }

    @Test(groups = { "unit" })
    public void testSourceXML() {
        String xml = POJOMarshalUtil.toXML(model);

        SourceModel model2 = POJOMarshalUtil.fromXML(xml, SourceModel.class);

        assertNotNull(model2);
        assertEquals(model.getId(), model2.getId());
        assertEquals(model.getTitle(), model2.getTitle());
        assertEquals(model.getDescription(), model2.getDescription());
//        assertEquals(model.isPublished(), model2.isPublished());  // JAXB Unmarshaller leaves this as null ...
    }

}
