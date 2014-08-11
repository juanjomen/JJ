package org.familysearch.standards.place.ws.mapping;

import static org.testng.AssertJUnit.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.Group;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.LocalizedNameDescModel;
import org.familysearch.standards.place.ws.model.PlaceRepGroupModel;
import org.familysearch.standards.place.ws.model.PlaceRepSummaryModel;
import org.familysearch.standards.place.ws.model.PlaceTypeGroupModel;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.testng.annotations.Test;


public class TestGroupMapper {

 
    @Test(groups = {"unit"})
    public void testCreateModelFromTypeGroup() {
        ReadablePlaceDataService			mockDataService;
        GroupMapper							mapper;
        PlaceTypeGroupModel						typeModel;
        Group						type;

        Map<String,String> namesMap = new HashMap<>();
        namesMap.put("en", "name");
        Map<String,String> descrMap = new HashMap<>();
        descrMap.put("en", "desc");

        type = mock( Group.class );
        when( type.getNames() ).thenReturn( namesMap );
        when( type.getDescriptions() ).thenReturn( descrMap );
        when( type.getId() ).thenReturn( 1 );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new GroupMapper( mockDataService );

        typeModel = mapper.createModelFromTypeGroup( type, "path/" );
        assertEquals( 1, typeModel.getId() );
        assertEquals( "name", typeModel.getLocalizedName().get(0).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get(0).getDescription() );
        assertEquals( "path/" + GroupMapper.GROUP_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromTypeGroupWithTypes() throws PlaceDataException {
        ReadablePlaceDataService   mockDataService;
        GroupMapper                 mapper;
        PlaceTypeGroupModel        typeModel;
        Group                      type;
        StdLocale                  locale = new StdLocale( "en" );
        Set<Integer>               types = new HashSet<Integer>();
        Set<Group>                 subGroups = new HashSet<Group>();
        Group                      subGroup;

        Map<String,String> sgNamesMap = new HashMap<>();
        sgNamesMap.put("en", "subgroup name");
        Map<String,String> sgDescrMap = new HashMap<>();
        sgDescrMap.put("en", "subgroup desc");

        subGroup = mock( Group.class );
        when( subGroup.getNames() ).thenReturn( sgNamesMap );
        when( subGroup.getDescriptions() ).thenReturn( sgDescrMap );
        when( subGroup.getId() ).thenReturn( 17 );

        types.add( 12 );
        subGroups.add( subGroup );

        Map<String,String> namesMap = new HashMap<>();
        namesMap.put("en", "name");
        Map<String,String> descrMap = new HashMap<>();
        descrMap.put("en", "desc");

        type = mock( Group.class );
        when( type.getNames() ).thenReturn( namesMap );
        when( type.getDescriptions() ).thenReturn( descrMap );
        when( type.getId() ).thenReturn( 1 );
        when( type.getMemberIds() ).thenReturn( types );
        when( type.getSubGroups() ).thenReturn( subGroups );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new GroupMapper( mockDataService );

        TypeDTO mockType = mock(TypeDTO.class);
        when(mockType.getId()).thenReturn(12);
        when(mockType.getAllNames()).thenReturn(namesMap);
        when(mockType.getAllDescriptions()).thenReturn(descrMap);
        when(mockType.getCode()).thenReturn("code");
        when(mockType.isPublished()).thenReturn(true);
        
        doReturn(mockType).when(mockDataService).getTypeById(TypeCategory.PLACE, 12);

        try {
            typeModel = mapper.createModelFromTypeGroupWithTypes( type, locale, "path/" );
            assertEquals( 1, typeModel.getId() );
            assertEquals( "name", typeModel.getLocalizedName().get(0).getName() );
            assertEquals( "desc", typeModel.getLocalizedName().get(0).getDescription() );
            assertEquals( "path/" + GroupMapper.GROUP_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
            assertEquals( "self", typeModel.getSelfLink().getRel() );
            assertEquals( new Integer( 12 ), typeModel.getTypes().get( 0 ).getId() );
            assertEquals( "name", typeModel.getTypes().get( 0 ).getLocalizedName().get( 0 ).getName() );
            assertEquals( "desc", typeModel.getTypes().get( 0 ).getLocalizedName().get( 0 ).getDescription() );
            assertEquals( "en", typeModel.getTypes().get( 0 ).getLocalizedName().get( 0 ).getLocale() );
            assertEquals( new Boolean( true ), typeModel.getTypes().get( 0 ).isPublished() );
            assertEquals( "code", typeModel.getTypes().get( 0 ).getCode() );
            assertEquals( 17, typeModel.getSubGroups().get( 0 ).getId() );
            assertEquals( "subgroup name", typeModel.getSubGroups().get( 0 ).getLocalizedName().get(0).getName() );
            assertEquals( "subgroup desc", typeModel.getSubGroups().get( 0 ).getLocalizedName().get(0).getDescription() );
        } catch (PlaceDataException e) {
            fail("Shouldn't have failed ... " + e.getMessage());
        }
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromPlaceRepGroup() {
        ReadablePlaceDataService  mockDataService;
        GroupMapper                mapper;
        PlaceRepGroupModel        placeRepModel;
        Group                     type;

        Map<String,String> namesMap = new HashMap<>();
        namesMap.put("en", "name");
        Map<String,String> descrMap = new HashMap<>();
        descrMap.put("en", "desc");

        type = mock(Group.class);
        when(type.getNames()).thenReturn(namesMap);
        when(type.getDescriptions()).thenReturn(descrMap);
        when(type.getId()).thenReturn(1);
        mockDataService = mock(ReadablePlaceDataService.class);
        mapper = new GroupMapper(mockDataService);

        placeRepModel = mapper.createModelFromPlaceRepGroup(type, "path/");
        assertEquals(1, placeRepModel.getId());
        assertEquals("name", placeRepModel.getLocalizedName().get(0).getName());
        assertEquals("desc", placeRepModel.getLocalizedName().get(0).getDescription());
        assertEquals("path/" + GroupMapper.GROUP_PLACE_REP_PATH + 1, placeRepModel.getSelfLink().getHref());
        assertEquals("self", placeRepModel.getSelfLink().getRel());
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromPlaceRepGroupWithTypes() throws PlaceDataException {
        ReadablePlaceDataService   mockDataService;
        GroupMapper                mapper;
        PlaceRepGroupModel         placeRepModel;
        Group                      type;
        StdLocale                  locale = new StdLocale("en");
        Set<Integer>               types = new HashSet<Integer>();
        Set<Group>                 subGroups = new HashSet<Group>();
        Group                      subGroup;

        Map<String,String> sgNamesMap = new HashMap<>();
        sgNamesMap.put("en", "subgroup name");
        Map<String,String> sgDescrMap = new HashMap<>();
        sgDescrMap.put("en", "subgroup desc");

        subGroup = mock(Group.class);
        when(subGroup.getNames()).thenReturn(sgNamesMap);
        when(subGroup.getDescriptions()).thenReturn(sgDescrMap);
        when(subGroup.getId()).thenReturn(17);

        types.add(12);
        subGroups.add(subGroup);

        Map<String,String> namesMap = new HashMap<>();
        namesMap.put("en", "name");
        Map<String,String> descrMap = new HashMap<>();
        descrMap.put("en", "desc");

        type = mock(Group.class);
        when(type.getNames()).thenReturn(namesMap);
        when(type.getDescriptions()).thenReturn(descrMap);
        when(type.getId()).thenReturn(1);
        when(type.getMemberIds()).thenReturn(types);
        when(type.getSubGroups()).thenReturn(subGroups);
        mockDataService = mock(ReadablePlaceDataService.class);
        mapper = new GroupMapper(mockDataService);

        TypeDTO mockType = mock(TypeDTO.class);
        when(mockType.getId()).thenReturn(12);
        when(mockType.getAllNames()).thenReturn(namesMap);
        when(mockType.getAllDescriptions()).thenReturn(descrMap);
        when(mockType.getCode()).thenReturn("code");
        when(mockType.isPublished()).thenReturn(true);
        
        doReturn(mockType).when(mockDataService).getTypeById(TypeCategory.PLACE, 12);

        try {
            placeRepModel = mapper.createModelFromPlaceRepGroupWithTypes(type, locale, "path/");
            assertEquals(1, placeRepModel.getId());
            assertEquals("name", placeRepModel.getLocalizedName().get(0).getName());
            assertEquals("desc", placeRepModel.getLocalizedName().get(0).getDescription());
            assertEquals("path/" + GroupMapper.GROUP_PLACE_REP_PATH + 1, placeRepModel.getSelfLink().getHref());
            assertEquals(new Integer( 12 ), placeRepModel.getRepSummaries().get(0).getId());
//            assertEquals("path/" + PlaceRepresentationMapper.REPS_PATH + 12, placeRepModel.getRepLinks().get(0).getHref());
            assertEquals("self", placeRepModel.getSelfLink().getRel());
            assertEquals(17, placeRepModel.getSubGroups().get(0).getId());
            assertEquals("subgroup name", placeRepModel.getSubGroups().get(0).getLocalizedName().get(0).getName());
            assertEquals("subgroup desc", placeRepModel.getSubGroups().get(0).getLocalizedName().get(0).getDescription());
        } catch (PlaceDataException e) {
            fail("Shouldn't have failed ... " + e.getMessage());
        }
    }

    @Test(groups = {"unit"})
    public void testCreateDTOFromTypeGroup() throws PlaceDataException {
        ReadablePlaceDataService   mockDataService = mock(ReadablePlaceDataService.class);
        GroupMapper                mapper = new GroupMapper(mockDataService);

        PlaceTypeGroupModel model = makePlaceTypeGroupModel(11, true, "en", "group A", "group A", "ja", "group A/JA", "group A/JA", "de", "group A/DE", "group A/DE");
        GroupDTO groupDTO = mapper.createDTOFromTypeGroup(model, true);
        assertNotNull(groupDTO);
        assertEquals(11, groupDTO.getId());
        assertEquals(3, groupDTO.getNames().size());
        assertEquals("group A", groupDTO.getName("en"));
        assertEquals("group A/JA", groupDTO.getName("ja"));
        assertEquals("group A/DE", groupDTO.getName("de"));
        assertEquals("group A", groupDTO.getDescription("en"));
        assertEquals("group A/JA", groupDTO.getDescription("ja"));
        assertEquals("group A/DE", groupDTO.getDescription("de"));
        assertEquals(3, groupDTO.getMemberIds().size());
        assertTrue(groupDTO.getMemberIds().contains(1));
        assertTrue(groupDTO.getMemberIds().contains(3));
        assertTrue(groupDTO.getMemberIds().contains(5));
        assertEquals(2, groupDTO.getChildGroups().size());
    }

    @Test(groups = {"unit"})
    public void testCreateDTOFromPlaceRepGroup() throws PlaceDataException {
        ReadablePlaceDataService   mockDataService = mock(ReadablePlaceDataService.class);
        GroupMapper                mapper = new GroupMapper(mockDataService);

        PlaceRepGroupModel model = makePlaceRepGroupModel(11, true, "en", "group A", "group A", "ja", "group A/JA", "group A/JA", "de", "group A/DE", "group A/DE");
        GroupDTO groupDTO = mapper.createDTOFromPlaceRepGroup(model, true);
        assertNotNull(groupDTO);
        assertEquals(11, groupDTO.getId());
        assertEquals(3, groupDTO.getNames().size());
        assertEquals("group A", groupDTO.getName("en"));
        assertEquals("group A/JA", groupDTO.getName("ja"));
        assertEquals("group A/DE", groupDTO.getName("de"));
        assertEquals("group A", groupDTO.getDescription("en"));
        assertEquals("group A/JA", groupDTO.getDescription("ja"));
        assertEquals("group A/DE", groupDTO.getDescription("de"));
        assertEquals(3, groupDTO.getMemberIds().size());
        assertTrue(groupDTO.getMemberIds().contains(1));
        assertTrue(groupDTO.getMemberIds().contains(3));
        assertTrue(groupDTO.getMemberIds().contains(5));
        assertEquals(2, groupDTO.getChildGroups().size());
    }

    /**
     * Create a 'PlaceTypeGroupModel' instance with mostly dummy values.
     * 
     * @param id identifier
     * @param makeChildren TRUE if we should add children, FALSE otherwise
     * @param localeNameDesc triplets of locale + name + description
     * @return new 'PlaceTypeGroupModel' instance
     */
    private PlaceTypeGroupModel makePlaceTypeGroupModel(int id, boolean makeChildren, String... localeNameDesc) {
        List<LocalizedNameDescModel> names = new ArrayList<>();
        for (int i=0;  i<localeNameDesc.length;  i+=3) {
            names.add(makeNameAndDesc(localeNameDesc[i], localeNameDesc[i+1], localeNameDesc[i+2]));
        }

        List<TypeModel> types = new ArrayList<>();
        for (int i=1;  i<=5;  i+=2) {
            TypeModel type = mock(TypeModel.class);
            when(type.getId()).thenReturn(i);
            types.add(type);
        }

        List<PlaceTypeGroupModel> subGroups = new ArrayList<>();
        if (makeChildren) {
            subGroups.add(makePlaceTypeGroupModel(id+1, false, "en", "sub-group-01", "sub-group-01"));
            subGroups.add(makePlaceTypeGroupModel(id+2, false, "en", "sub-group-02", "sub-group-02"));
        }

        PlaceTypeGroupModel model = new PlaceTypeGroupModel();
        model.setId(id);
        model.setIsPublished(true);
        model.setName(names);
        model.setTypes(types);
        model.setSubGroups(subGroups);

        return model;
    }

    /**
     * Create a 'PlaceRepGroupModel' instance with mostly dummy values.
     * 
     * @param id identifier
     * @param makeChildren TRUE if we should add children, FALSE otherwise
     * @param localeNameDesc triplets of locale + name + description
     * @return new 'PlaceRepGroupModel' instance
     */
    private PlaceRepGroupModel makePlaceRepGroupModel(int id, boolean makeChildren, String... localeNameDesc) {
        List<LocalizedNameDescModel> names = new ArrayList<>();
        for (int i=0;  i<localeNameDesc.length;  i+=3) {
            names.add(makeNameAndDesc(localeNameDesc[i], localeNameDesc[i+1], localeNameDesc[i+2]));
        }

        List<PlaceRepSummaryModel> summaries = new ArrayList<>();
        PlaceRepSummaryModel summary;
        summary = new PlaceRepSummaryModel();
        summary.setId( 1 );
        summaries.add( summary );
        summary = new PlaceRepSummaryModel();
        summary.setId( 3 );
        summaries.add( summary );
        summary = new PlaceRepSummaryModel();
        summary.setId( 5 );
        summaries.add( summary );

        List<PlaceRepGroupModel> subGroups = new ArrayList<>();
        if (makeChildren) {
            subGroups.add(makePlaceRepGroupModel(id+1, false, "en", "sub-group-01", "sub-group-01"));
            subGroups.add(makePlaceRepGroupModel(id+2, false, "en", "sub-group-02", "sub-group-02"));
        }

        PlaceRepGroupModel model = new PlaceRepGroupModel();
        model.setId(id);
        model.setIsPublished(true);
        model.setName(names);
        model.setRepSummaries( summaries );
        model.setSubGroups(subGroups);

        return model;
    }

    /**
     * Create a localized name given the locale, name and description
     * 
     * @param locale locale
     * @param name name
     * @param desc description
     * @return localized name/description
     */
    private LocalizedNameDescModel makeNameAndDesc(String locale, String name, String desc) {
        LocalizedNameDescModel nameDesc = new LocalizedNameDescModel();

        nameDesc.setLocale(locale);
        nameDesc.setName(name);
        nameDesc.setDescription(desc);

        return nameDesc;
    }
}
