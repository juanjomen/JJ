package org.familysearch.standards.place.ws.mapping;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.PlaceRepresentation;
import org.familysearch.standards.place.data.GroupType;
import org.familysearch.standards.place.data.InvalidPlaceDataException;
import org.familysearch.standards.place.data.PlaceDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.PlaceRepresentationDTO;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.CentroidModel;
import org.familysearch.standards.place.ws.model.JurisdictionModel;
import org.familysearch.standards.place.ws.model.LocalizedNameDescModel;
import org.familysearch.standards.place.ws.model.LocationModel;
import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;
import org.familysearch.standards.place.ws.model.PlaceTypeGroupModel;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class TestPlaceRepresentationMapper {

    PlaceRepresentationMapper mapper;
    CommonMapper commonMapper;
    ReadablePlaceDataService service;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws PlaceDataException {
        service = mock(ReadablePlaceDataService.class);
        mapper = new PlaceRepresentationMapper(service);
        commonMapper = new CommonMapper(service);

//        when(service.getVersionFromRevision(anyInt())).thenReturn("v1.0");
    }

    @Test(groups = {"unit"})
    public void createModelFromPlaceRepresentation() throws PlaceDataException {
        // Grand-Parent information
        PlaceRepresentationDTO gparPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> gparNamesMap = new HashMap<String,String>();
        gparNamesMap.put("en", "Name-EN");
        gparNamesMap.put("fr", "Name-FR");

        when(gparPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 1 });
        when(gparPlaceRepDTO.getDisplayNames()).thenReturn(gparNamesMap);

        // Parent information
        PlaceRepresentationDTO parPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> parNamesMap = new HashMap<String,String>();
        parNamesMap.put("en", "Name-EN");
        parNamesMap.put("fr", "Name-FR");

        when(parPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 11, 1 });

        // Leaf information
        PlaceRepresentationDTO placeRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> namesMap = new HashMap<String,String>();
        namesMap.put("en", "Name-EN");
        namesMap.put("fr", "Name-FR");

        // Other mocks
        PlaceDTO ownerMock = mock(PlaceDTO.class);
        GroupDTO typeGroupMock = mock(GroupDTO.class);

        Map<String,String> naMap = new HashMap<String,String>();
        naMap.put("en", "Group-Name");
        Map<String,String> deMap = new HashMap<String,String>();
        deMap.put("en", "Group-Description");

        when(placeRepDTO.getJurisdictionChain()).thenReturn(new int[] { 111, 11, 1 });
        when(placeRepDTO.getDisplayNames()).thenReturn(namesMap);
        when(placeRepDTO.getUUID()).thenReturn("abcd-1234");
        when(placeRepDTO.getRevision()).thenReturn(55);
        when(placeRepDTO.getType()).thenReturn(17);
        when(placeRepDTO.getFromYear()).thenReturn(1900);
        when(placeRepDTO.getToYear()).thenReturn(2000);
        when(placeRepDTO.getOwnerId()).thenReturn(157);
        when(placeRepDTO.getPreferredLocale()).thenReturn("en");
        when(placeRepDTO.isPublished()).thenReturn(true);
        when(placeRepDTO.isValidated()).thenReturn(true);
        when(placeRepDTO.getLatitude()).thenReturn(55.5);
        when(placeRepDTO.getLongitude()).thenReturn(-111.1);
        when(placeRepDTO.getTypeGroup()).thenReturn(166);

        when(ownerMock.getId()).thenReturn(157);
        when(typeGroupMock.getId()).thenReturn(166);
        when(typeGroupMock.getNames()).thenReturn(naMap);
        when(typeGroupMock.getDescriptions()).thenReturn(deMap);

        when(service.getPlaceRepresentations(isA(int[].class), anyString()))
            .thenReturn(new PlaceRepresentationDTO[] { placeRepDTO, parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { gparPlaceRepDTO });
        when(service.getTypeById(eq(TypeCategory.PLACE), anyInt())).thenReturn(mock(TypeDTO.class));
        when(service.getPlaceById(157, null)).thenReturn(ownerMock);
        when(service.getGroupById(GroupType.PLACE_TYPE, 166)).thenReturn(typeGroupMock);

        PlaceRepresentationModel placeRepModel =
                mapper.createModelFromDTO(placeRepDTO, new StdLocale("en"), "http://localhost/blah/", false, true);

        assertNotNull(placeRepModel);
        assertEquals(placeRepModel.getId(), Integer.valueOf(111));
        assertEquals(placeRepModel.getDisplayName().getLocale(), "en");
        assertEquals(placeRepModel.getDisplayName().getName(), "Name-EN");
        assertEquals(placeRepModel.getDisplayNames().size(), 2);
        assertEquals(placeRepModel.getFromYear(), Integer.valueOf(1900));
        assertEquals(placeRepModel.getToYear(), Integer.valueOf(2000));
        assertEquals(placeRepModel.getFullDisplayName().getLocale(), "en");
        assertEquals(placeRepModel.getGroup().getId(), 166);
        assertEquals(placeRepModel.getGroup().getLocalizedName().get(0).getName(), "Group-Name");
        assertEquals(placeRepModel.getGroup().getLocalizedName().get(0).getDescription(), "Group-Description");
        assertEquals(placeRepModel.getJurisdiction().getId(), 11);
        assertEquals(placeRepModel.getLinks().size(), 4);
        assertEquals(placeRepModel.getLinks().get(0).getHref(), "http://localhost/blah/reps/111");
        assertEquals(placeRepModel.getLinks().get(1).getHref(), "http://localhost/blah/157");
        assertEquals(placeRepModel.getLinks().get(2).getHref(), "http://localhost/blah/reps/111/attributes/");
        assertEquals(placeRepModel.getLinks().get(3).getHref(), "http://localhost/blah/reps/111/citations/");
        assertEquals(placeRepModel.getLocation().getCentroid().getLatitude(), 55.5, 0.0001);
        assertEquals(placeRepModel.getLocation().getCentroid().getLongitude(), -111.1, 0.0001);
        assertEquals(placeRepModel.getPreferredLocale(), "en");
        assertEquals(placeRepModel.getRevision(), Integer.valueOf(55));
        assertEquals(placeRepModel.getType().getId(), Integer.valueOf(17));
        assertEquals(placeRepModel.getUUID(), "abcd-1234");
        assertTrue(placeRepModel.isPublished());
        assertTrue(placeRepModel.isValidated());
    }

    @Test(groups = {"unit"})
    public void createDTOFromModel() throws PlaceDataException {
        // Name stuff
        List<NameModel> dispNames = new ArrayList<NameModel>();
        for (String name[] : new String[][] { { "en", "Provo" }, { "fr", "Proovoo "} }) {
            NameModel nModel = new NameModel();
            nModel.setLocale(name[0]);
            nModel.setName(name[1]);
            dispNames.add(nModel);
        }

        List<LocalizedNameDescModel> nameAndDesc = new ArrayList<>();
        LocalizedNameDescModel ndModel = new LocalizedNameDescModel();
        ndModel.setLocale("en");
        ndModel.setName("Stinky Group");
        nameAndDesc.add(ndModel);

        // Group stuff
        PlaceTypeGroupModel group = new PlaceTypeGroupModel();
        group.setId(11);
        group.setName(nameAndDesc);

        // Centroid stuff
        CentroidModel centroid = new CentroidModel();
        centroid.setLatitude(111.1);
        centroid.setLongitude(-90.0);

        // Location stuff
        LocationModel location = new LocationModel();
        location.setCentroid(centroid);

        // Jurisdiction stuff
        JurisdictionModel jurisdiction = new JurisdictionModel();
        jurisdiction.setId(111);

        JurisdictionModel parent = new JurisdictionModel();
        parent.setId(11);

        JurisdictionModel grParent = new JurisdictionModel();
        grParent.setId(1);
        jurisdiction.setParent(parent);
        parent.setParent(grParent);

        // Type stuff
        TypeModel type = new TypeModel();
        type.setId(77);
        type.setCode("code");
        type.setIsPublished(true);

        when(service.getTypeById(eq(TypeCategory.PLACE), anyInt())).thenReturn(mock(TypeDTO.class));

        // Finally, we can create the place-rep stuff
        PlaceRepresentationModel model = new PlaceRepresentationModel();
        model.setDisplayNames(dispNames);
        model.setGroup(group);
        model.setLocation(location);
        model.setJurisdiction(jurisdiction);
        model.setOwnerId(222);
        model.setRevision(5);
        model.setType(type);

        PlaceRepresentationDTO placeRepDTO = mapper.createDTOFromModel(model, null);
        assertNotNull(placeRepDTO);
        assertEquals(placeRepDTO.getId(), 111);
        assertEquals(placeRepDTO.getDisplayNames().size(), 2);
        assertEquals(placeRepDTO.getTypeGroup(), Integer.valueOf(11));
    }

    @Test(groups = {"unit"})
    public void createDTOFromModelInvalidNames() throws PlaceDataException {
        // Name stuff
        List<NameModel> dispNames = new ArrayList<NameModel>();
        for (String name[] : new String[][] { { "en", "Provo" }, { "fr", "Proovoo "}, { "en", "Provo Two" } }) {
            NameModel nModel = new NameModel();
            nModel.setLocale(name[0]);
            nModel.setName(name[1]);
            dispNames.add(nModel);
        }

        // Finally, we can create the place-rep stuff
        PlaceRepresentationModel model = new PlaceRepresentationModel();
        model.setId(123);
        model.setDisplayNames(dispNames);

        try {
            mapper.createDTOFromModel(model, null);
            fail("Shouldn't have come here: ");
        } catch (InvalidPlaceDataException ex) { 
            assertTrue(true, "Expected exception");
        }
    }

    @Test(groups = {"unit"})
    public void createDTOFromModelMismatchId() throws PlaceDataException {
        PlaceRepresentationModel model = new PlaceRepresentationModel();
        model.setId(123);

        try {
            mapper.createDTOFromModel(model, 456);
            fail("This shouldn't have worked ...");
        } catch(InvalidPlaceDataException ex) {
            assertTrue(true, "Expected exception");
        }
    }

    @Test(groups = {"unit"})
    public void createDTOFromModelNulls() throws PlaceDataException {
        when(service.getTypeById(eq(TypeCategory.PLACE), anyInt())).thenReturn(mock(TypeDTO.class));

        // Finally, we can create the place-rep stuff
        PlaceRepresentationModel model = new PlaceRepresentationModel();
        model.setDisplayNames(null);
        model.setOwnerId(null);
        model.setRevision(null);

        PlaceRepresentationDTO placeRepDTO = mapper.createDTOFromModel(model, null);
        assertNotNull(placeRepDTO);
        assertEquals(placeRepDTO.getId(), -1);
        assertEquals(placeRepDTO.getDisplayNames().size(), 0);
        assertNull(placeRepDTO.getTypeGroup());
    }

    @Test(groups = {"unit"})
    public void createJurisdictionChainFromJurisdictionModel() {
        JurisdictionModel model = new JurisdictionModel();
        model.setId(1111);

        JurisdictionModel parent = new JurisdictionModel();
        parent.setId(111);

        JurisdictionModel grParent = new JurisdictionModel();
        grParent.setId(11);

        JurisdictionModel grGrParent = new JurisdictionModel();
        grGrParent.setId(1);

        model.setParent(parent);
        parent.setParent(grParent);
        grParent.setParent(grGrParent);

        int[] idChain = commonMapper.createJurisdictionChainFromJurisdictionModel(model);
        assertNotNull(idChain);
        assertEquals(idChain.length, 4);
        assertEquals(idChain[0], 1111);
        assertEquals(idChain[1], 111);
        assertEquals(idChain[2], 11);
        assertEquals(idChain[3], 1);
    }


    @Test(groups = {"unit"})
    public void createJurisdictionChainFromJurisdictionModelNull() {
        int[] idChain = commonMapper.createJurisdictionChainFromJurisdictionModel(null);

        assertNotNull(idChain);
        assertEquals(idChain.length, 0);
    }

    @Test(groups = {"unit"})
    public void createJurisdictionModelFromJurisdictionChain() throws PlaceDataException {
        // Grand-Parent information
        PlaceRepresentationDTO gparPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> gparNamesMap = new HashMap<String,String>();
        gparNamesMap.put("en", "Name-EN");
        gparNamesMap.put("fr", "Name-FR");

        when(gparPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 1 });
        when(gparPlaceRepDTO.getDisplayNames()).thenReturn(gparNamesMap);

        // Parent information
        PlaceRepresentationDTO parPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> parNamesMap = new HashMap<String,String>();
        parNamesMap.put("en", "Name-EN");
        parNamesMap.put("fr", "Name-FR");

        when(parPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 11, 1 });
        when(parPlaceRepDTO.getDisplayNames()).thenReturn(parNamesMap);

        // Leaf information
        PlaceRepresentationDTO placeRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> namesMap = new HashMap<String,String>();
        namesMap.put("en", "Name-EN");
        namesMap.put("fr", "Name-FR");

        when(placeRepDTO.getJurisdictionChain()).thenReturn(new int[] { 111, 11, 1 });
        when(placeRepDTO.getDisplayNames()).thenReturn(namesMap);

        PlaceRepresentation placeRep = new PlaceRepresentation(service, placeRepDTO);

        when(service.getPlaceRepresentations(isA(int[].class), anyString()))
            .thenReturn(new PlaceRepresentationDTO[] { placeRepDTO, parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { gparPlaceRepDTO });

        // Whew ... ready to test ...
        JurisdictionModel jModel = commonMapper.createJurisdictionModelFromJurisdictionChain(placeRep, new StdLocale("en"), "http://localhost/blah");

        assertNotNull(jModel);
        assertEquals(jModel.getId(), 111);
        assertEquals(jModel.getLocale(), "en");
        assertEquals(jModel.getName(), "Name-EN");
        assertEquals(jModel.getSelfLink().getRel(), "parent");
        assertNotNull(jModel.getParent());
        assertNotNull(jModel.getParent().getParent());
        assertNull(jModel.getParent().getParent().getParent());
    }
}
