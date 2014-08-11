package org.familysearch.standards.place.ws.mapping;

import static org.testng.Assert.*;

import org.familysearch.standards.place.data.SourceDTO;
import org.familysearch.standards.place.ws.model.SourceModel;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class SourceMapperTest {

    SourceDTO dto;
    SourceMapper mapper;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        dto = new SourceDTO(11, "title", "description", true);
        mapper = new SourceMapper();
    }


    @Test(groups = {"unit"})
    public void createdModelFromDto() {
        SourceModel model = mapper.createModelFromDTO(dto, "/places/");

        assertNotNull(model);
        assertEquals(model.getId(), Integer.valueOf(11));
        assertEquals(model.getTitle(), "title");
        assertEquals(model.getDescription(), "description");
        assertTrue(model.isPublished());
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModel() {
        SourceModel model = mapper.createModelFromDTO(dto, "/places/");

        SourceDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getId(), model.getId().intValue());
        assertEquals(dto.getTitle(), model.getTitle());
        assertEquals(dto.getDescription(), model.getDescription());
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModelNullId() {
        SourceModel model = mapper.createModelFromDTO(dto, "/places/");
        model.setId(null);

        SourceDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getId(), 0);
    }
}
