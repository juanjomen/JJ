package org.familysearch.standards.place.ws.mapping;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import java.util.Date;

import org.familysearch.standards.place.data.CitationDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.CitationModel;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class CitationMapperTest {

    CitationDTO dto;
    CitationMapper mapper;
    ReadablePlaceDataService service;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        dto = new CitationDTO(11, 22, 33, 44, new Date(System.currentTimeMillis()), "a-description", "a-source-ref", 10);

        service = mock(ReadablePlaceDataService.class);
        mapper = new CitationMapper(service);
    }


    @Test(groups = {"unit"})
    public void createdModelFromDto() throws PlaceDataException {
        when(service.getTypeById(TypeCategory.CITATION, 44)).thenReturn(mock(TypeDTO.class));

        CitationModel model = mapper.createModelFromDTO(dto, "/places/");

        assertNotNull(model);
        assertEquals(model.getId(), Integer.valueOf(11));
        assertEquals(model.getSourceId(), Integer.valueOf(22));
        assertEquals(model.getRepId(), Integer.valueOf(33));
        assertNotNull(model.getCitDate());
        assertEquals(model.getDescription(), "a-description");
        assertEquals(model.getSourceRef(), "a-source-ref");

        assertEquals(model.getLinks().size(), 4);
        assertEquals(model.getLinks().get(0).getRel(), "self");
        assertEquals(model.getLinks().get(0).getHref(), "/places/reps/33/citations/11");
        assertEquals(model.getLinks().get(1).getRel(), "via");
        assertEquals(model.getLinks().get(1).getHref(), "/places/reps/33");
        assertEquals(model.getLinks().get(2).getRel(), "type");
        assertEquals(model.getLinks().get(2).getHref(), "/places/citation-types/44");
        assertEquals(model.getLinks().get(3).getRel(), "describes");
        assertEquals(model.getLinks().get(3).getHref(), "/places/sources/22");
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModel() throws PlaceDataException {
        TypeDTO mockTypeDTO = mock(TypeDTO.class);
        when(mockTypeDTO.getId()).thenReturn(11);

        doReturn(mockTypeDTO).when(service).getTypeById(eq(TypeCategory.CITATION), anyInt());

        CitationModel model = mapper.createModelFromDTO(dto, "/places/");

        CitationDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getId(), model.getId().intValue());
        assertEquals(dto.getRepId(), model.getRepId().intValue());
        assertEquals(dto.getSourceId(), model.getSourceId().intValue());
        assertEquals(dto.getTypeId(), model.getType().getId().intValue());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getSourceRef(), model.getSourceRef());
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModelNullId() throws PlaceDataException {
        doReturn(mock(TypeDTO.class)).when(service).getTypeById(eq(TypeCategory.CITATION), anyInt());

        CitationModel model = mapper.createModelFromDTO(dto, "/places/");
        model.setId(null);

        CitationDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getId(), 0);
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModelNullType() throws PlaceDataException {
        doReturn(mock(TypeDTO.class)).when(service).getTypeById(eq(TypeCategory.CITATION), anyInt());

        CitationModel model = mapper.createModelFromDTO(dto, "/places/");
        model.setType(null);

        CitationDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getTypeId(), 0);
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModelNullTypeId() throws PlaceDataException {
        TypeDTO mockTypeDTO = mock(TypeDTO.class);
        when(mockTypeDTO.getId()).thenReturn(11);
        doReturn(mockTypeDTO).when(service).getTypeById(eq(TypeCategory.CITATION), anyInt());

        CitationModel model = mapper.createModelFromDTO(dto, "/places/");
        TypeModel typeModel = new TypeModel();
        typeModel.setId(null);
        model.setType(typeModel);

        CitationDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getTypeId(), 0);
    }
}
