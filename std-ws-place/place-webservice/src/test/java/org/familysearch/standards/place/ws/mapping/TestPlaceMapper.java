package org.familysearch.standards.place.ws.mapping;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.data.InvalidPlaceDataException;
import org.familysearch.standards.place.data.PlaceDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.PlaceNameDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.model.PlaceModel;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.familysearch.standards.place.ws.model.VariantModel;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class TestPlaceMapper {

    PlaceMapper mapper;
    ReadablePlaceDataService service;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws PlaceDataException {
        service = mock(ReadablePlaceDataService.class);
        mapper = new PlaceMapper(service);

        when(service.getVersionFromRevision(anyInt())).thenReturn("v1.0");
    }


    @Test(groups = {"unit"})
    public void PlaceMapper() {
        assertNotNull(mapper);
    }

    @Test(groups = {"unit"})
    public void createDTOFromModel() throws PlaceDataException {
        List<VariantModel> variantNames = this.makeVariants();
        List<PlaceNameDTO> variantDTOs = new ArrayList<PlaceNameDTO>();
        Map<Integer,Integer> priorityMap = new HashMap<Integer,Integer>();

        int priority = 1;
        for (VariantModel variant : variantNames) {
            PlaceNameDTO nameDTO = new PlaceNameDTO(variant.getId(), variant.getName().getName(), variant.getName().getLocale(), variant.getType().getId());
            variantDTOs.add(nameDTO);
            priorityMap.put(variant.getType().getId(), priority++);
        }
        
        PlaceDTO placeDTO = new PlaceDTO(11, variantDTOs, 1900, 2000, 55, null);

        when(service.getTypeById(eq(TypeCategory.NAME), isA(Integer.class))).thenReturn(mock(TypeDTO.class));
        when(service.getTypeByCode(eq(TypeCategory.NAME), anyString())).thenReturn(mock(TypeDTO.class));

        PlaceModel model = mapper.createModelFromDTO(placeDTO, new StdLocale("en"), "http://localhost/dummy/");
        assertNotNull(model);
        assertEquals(model.getFromYear(), Integer.valueOf(1900));
        assertEquals(model.getToYear(), Integer.valueOf(2000));
    }

    @Test(groups = {"unit"})
    public void createDTOFromModelNoFromTo() throws PlaceDataException {
        List<VariantModel> variantNames = this.makeVariants();
        List<PlaceNameDTO> variantDTOs = new ArrayList<PlaceNameDTO>();
        Map<Integer,Integer> priorityMap = new HashMap<Integer,Integer>();

        int priority = 1;
        for (VariantModel variant : variantNames) {
            PlaceNameDTO nameDTO = new PlaceNameDTO(variant.getId(), variant.getName().getName(), variant.getName().getLocale(), variant.getType().getId());
            variantDTOs.add(nameDTO);
            priorityMap.put(variant.getType().getId(), priority++);
        }
        
        PlaceDTO placeDTO = new PlaceDTO(11, variantDTOs, Integer.MIN_VALUE, Integer.MAX_VALUE, 55, null);

        when(service.getTypeById(eq(TypeCategory.NAME), isA(Integer.class))).thenReturn(mock(TypeDTO.class));
        when(service.getTypeByCode(eq(TypeCategory.NAME), anyString())).thenReturn(mock(TypeDTO.class));

        PlaceModel model = mapper.createModelFromDTO(placeDTO, new StdLocale("en"), "http://localhost/dummy/");
        assertNotNull(model);
        assertNull(model.getFromYear());
        assertNull(model.getToYear());
    }

    @Test(groups = {"unit"})
    public void createModelFromPlace() throws PlaceDataException {
        PlaceModel model = new PlaceModel();
        model.setId(11);
        model.setFromYear(1900);
        model.setToYear(2000);
        model.setVariants(this.makeVariants());

        when(service.getTypeById(eq(TypeCategory.NAME), anyInt())).thenReturn(mock(TypeDTO.class));

        PlaceDTO placeDTO = mapper.createDTOFromModel(model);
        assertNotNull(placeDTO);
        assertEquals(placeDTO.getId(), model.getId().intValue());
        assertEquals(placeDTO.getStartYear(), model.getFromYear());
        assertEquals(placeDTO.getEndYear(), model.getToYear());
        assertEquals(placeDTO.getVariants().size(), 4);
    }

    @Test(groups = {"unit"})
    public void createModelFromPlaceNullID() throws PlaceDataException {
        PlaceModel model = new PlaceModel();
        model.setId(null);
        model.setFromYear(1900);
        model.setToYear(2000);
        model.setVariants(this.makeVariants());

        when(service.getTypeById(eq(TypeCategory.NAME), anyInt())).thenReturn(mock(TypeDTO.class));

        PlaceDTO placeDTO = mapper.createDTOFromModel(model);
        assertNotNull(placeDTO);
        assertEquals(placeDTO.getId(), -1);
        assertEquals(placeDTO.getStartYear(), model.getFromYear());
        assertEquals(placeDTO.getEndYear(), model.getToYear());
        assertEquals(placeDTO.getVariants().size(), 4);
    }

    @Test(groups = {"unit"})
    public void createNameDTOFromVariantModel() throws PlaceDataException {
        List<VariantModel> variantNames = this.makeVariants();

        // Set one variant's ID to null, another variant's NAME to null
        variantNames.get(variantNames.size()-2).setId(null);
        variantNames.get(variantNames.size()-1).setName(null);

        when(service.getTypeById(eq(TypeCategory.NAME), anyInt())).thenReturn(mock(TypeDTO.class));

        List<PlaceNameDTO> placeNames = mapper.createNameDTOFromVariantModel(variantNames);
        assertNotNull(placeNames);
        assertEquals(placeNames.size(), 4);
        assertEquals(placeNames.get(0).getName().get(), "Provo");
        assertEquals(placeNames.get(1).getName().get(), "Proovoo");
        assertEquals(placeNames.get(2).getName().get(), "Provoheim");
        assertEquals(placeNames.get(3).getName().get(), "");
    }

    @Test(groups = {"unit"})
    public void createNameDTOFromVariantModelInvalid() throws PlaceDataException {
        List<VariantModel> variantNames = new ArrayList<VariantModel>();

        TypeModel tModel = new TypeModel();
        tModel.setId(11);
        tModel.setCode("code11");
        tModel.setIsPublished(true);

        VariantModel vModel = new VariantModel();
        vModel.setId(null);
        vModel.setName(null);
        vModel.setType(tModel);
        variantNames.add(vModel);

        when(service.getTypeById(eq(TypeCategory.NAME), anyInt())).thenReturn(mock(TypeDTO.class));

        try {
            List<PlaceNameDTO> placeNames = mapper.createNameDTOFromVariantModel(variantNames);
            fail("Shouldn't have reached here ... " + placeNames);
        } catch (InvalidPlaceDataException ex) {
            assertTrue(true, "Expected exception ...");
        }
    }

    /**
     * Create a list of four variant names, just for fun
     * @return list of four variant names
     */
    private List<VariantModel> makeVariants() {
        List<VariantModel> variantNames = new ArrayList<VariantModel>();

        int id = 1;
        for (String name[] : new String[][] { { "en", "Provo" }, { "fr", "Proovoo" }, { "de", "Provoheim"}, { "dk", "ProvoDK" } }) {
            NameModel nModel = new NameModel();
            nModel.setLocale(name[0]);
            nModel.setName(name[1]);

            TypeModel tModel = new TypeModel();
            tModel.setId(id+10);
            tModel.setCode("code" + id);
            tModel.setIsPublished(true);

            VariantModel vModel = new VariantModel();
            vModel.setId(id++);
            vModel.setName(nModel);
            vModel.setType(tModel);
            variantNames.add(vModel);
        }

        return variantNames;
    }
}
