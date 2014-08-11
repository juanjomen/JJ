package org.familysearch.standards.place.ws.mapping;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.familysearch.standards.place.data.AttributeDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.AttributeModel;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class AttributeMapperTest {

    AttributeDTO dto;
    AttributeMapper mapper;
    ReadablePlaceDataService service;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        dto = new AttributeDTO(11, 22, 33, 2010, "attr-value", 10);

        service = mock(ReadablePlaceDataService.class);
        mapper = new AttributeMapper(service);
    }


    @Test(groups = {"unit"})
    public void createdModelFromDto() throws PlaceDataException {
        when(service.getTypeById(TypeCategory.ATTRIBUTE, 33)).thenReturn(mock(TypeDTO.class));

        AttributeModel model = mapper.createModelFromDTO(dto, "/places/");

        assertNotNull(model);
        assertEquals(model.getId(), Integer.valueOf(11));
        assertEquals(model.getRepId(), Integer.valueOf(22));
        assertEquals(model.getYear(), Integer.valueOf(2010));
        assertEquals(model.getValue(), "attr-value");

        assertEquals(model.getLinks().size(), 3);
        assertEquals(model.getLinks().get(0).getRel(), "self");
        assertEquals(model.getLinks().get(0).getHref(), "/places/reps/22/attributes/11");
        assertEquals(model.getLinks().get(1).getRel(), "via");
        assertEquals(model.getLinks().get(1).getHref(), "/places/reps/22");
        assertEquals(model.getLinks().get(2).getRel(), "type");
        assertEquals(model.getLinks().get(2).getHref(), "/places/attribute-types/33");
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModel() throws PlaceDataException {
        TypeDTO mockTypeDTO = mock(TypeDTO.class);
        when(mockTypeDTO.getId()).thenReturn(11);

        doReturn(mockTypeDTO).when(service).getTypeById(eq(TypeCategory.ATTRIBUTE), anyInt());

        AttributeModel model = mapper.createModelFromDTO(dto, "/places/");

        AttributeDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getId(), model.getId().intValue());
        assertEquals(dto.getRepId(), model.getRepId().intValue());
        assertEquals(dto.getTypeId(), model.getType().getId().intValue());
        assertEquals(dto.getValue(), model.getValue());
        assertEquals(dto.getYear(), model.getYear().intValue());
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModelNullId() throws PlaceDataException {
        doReturn(mock(TypeDTO.class)).when(service).getTypeById(eq(TypeCategory.ATTRIBUTE), anyInt());

        AttributeModel model = mapper.createModelFromDTO(dto, "/places/");
        model.setId(null);

        AttributeDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getId(), 0);
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModelNullType() throws PlaceDataException {
        doReturn(mock(TypeDTO.class)).when(service).getTypeById(eq(TypeCategory.ATTRIBUTE), anyInt());

        AttributeModel model = mapper.createModelFromDTO(dto, "/places/");
        model.setType(null);

        AttributeDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getTypeId(), 0);
    }

    @Test(groups = {"unit"})
    public void createdDtoFromModelNullTypeId() throws PlaceDataException {
        TypeDTO mockTypeDTO = mock(TypeDTO.class);
        when(mockTypeDTO.getId()).thenReturn(11);
        doReturn(mockTypeDTO).when(service).getTypeById(eq(TypeCategory.ATTRIBUTE), anyInt());

        AttributeModel model = mapper.createModelFromDTO(dto, "/places/");
        TypeModel typeModel = new TypeModel();
        typeModel.setId(null);
        model.setType(typeModel);

        AttributeDTO dto = mapper.createDTOFromModel(model);
        assertNotNull(dto);
        assertEquals(dto.getTypeId(), 0);
    }
}
