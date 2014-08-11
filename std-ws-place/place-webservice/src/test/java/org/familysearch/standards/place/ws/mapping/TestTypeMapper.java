package org.familysearch.standards.place.ws.mapping;

import static org.testng.AssertJUnit.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.familysearch.standards.core.LocalizedData;
import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.AttributeType;
import org.familysearch.standards.place.CitationType;
import org.familysearch.standards.place.PlaceNameType;
import org.familysearch.standards.place.PlaceType;
import org.familysearch.standards.place.data.GroupType;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.testng.annotations.Test;

public class TestTypeMapper {

    @Test(groups = {"unit"})
    public void testCreateModelFromPlaceType() {
        ReadablePlaceDataService			mockDataService;
        TypeMapper							mapper;
        TypeModel							typeModel;
        PlaceType							type;
        StdLocale							locale = new StdLocale( "en" );

        type = mock( PlaceType.class );
        when( type.getName( locale ) ).thenReturn( new LocalizedData<String>( "name", locale ) );
        when( type.getDescription( locale ) ).thenReturn( new LocalizedData<String>( "desc", locale ) );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromType( type, locale, "path/", TypeCategory.PLACE );
        assertEquals( new Integer( 1 ), typeModel.getId() );
        assertEquals( "code", typeModel.getCode() );
        assertEquals( new Boolean( true ), typeModel.isPublished() );
        assertEquals( "name", typeModel.getLocalizedName().get( 0 ).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get( 0 ).getDescription() );
        assertEquals( "en", typeModel.getLocalizedName().get( 0 ).getLocale() );
        assertEquals( "path/" + TypeMapper.PLACE_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromPlaceType2() {
        ReadablePlaceDataService			mockDataService;
        TypeMapper							mapper;
        TypeModel							typeModel;
        TypeDTO								type;
        Map<String,String>					allNames = new HashMap<String,String>();
        Map<String,String>                  allDescr = new HashMap<String,String>();

        allNames.put( "en", "name" );
        allDescr.put( "en", "desc" );

        type = mock( TypeDTO.class );
        when( type.getAllNames() ).thenReturn( allNames );
        when (type.getAllDescriptions() ).thenReturn( allDescr );
        when( type.getDescription( "en" ) ).thenReturn( "desc" );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromTypeDTO( type, null, "path/", TypeCategory.PLACE );
        assertEquals( new Integer( 1 ), typeModel.getId() );
        assertEquals( "code", typeModel.getCode() );
        assertEquals( new Boolean( true ), typeModel.isPublished() );
        assertEquals( "name", typeModel.getLocalizedName().get( 0 ).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get( 0 ).getDescription() );
        assertEquals( "en", typeModel.getLocalizedName().get( 0 ).getLocale() );
        assertEquals( "path/" + TypeMapper.PLACE_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromPlaceType3() {
        ReadablePlaceDataService   mockDataService;
        TypeMapper                 mapper;
        TypeModel                  typeModel;
        TypeDTO                    type;
        Map<String,String>         allNames = new HashMap<String,String>();
        Map<String,String>         allDescr = new HashMap<String,String>();
        Set<GroupDTO>     ptGroups = new HashSet<GroupDTO>();

        allNames.put( "en", "name" );
        allDescr.put( "en", "desc" );

        ptGroups.add(new GroupDTO(11, GroupType.PLACE_TYPE, new HashMap<String,String>(), new HashMap<String,String>(), true, null));
        ptGroups.add(new GroupDTO(22, GroupType.PLACE_TYPE, new HashMap<String,String>(), new HashMap<String,String>(), true, null));

        type = mock( TypeDTO.class );
        when( type.getAllNames() ).thenReturn( allNames );
        when (type.getAllDescriptions() ).thenReturn( allDescr );
        when( type.getDescription( "en" ) ).thenReturn( "desc" );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromPlaceTypeDTO( type, ptGroups, "path/" );
        assertEquals(typeModel.getGroups().size(), 2);
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromPlaceNameType() {
        ReadablePlaceDataService			mockDataService;
        TypeMapper							mapper;
        TypeModel							typeModel;
        TypeDTO								type;
        Map<String,String>					allNames = new HashMap<String,String>();
        Map<String,String>                  allDescr = new HashMap<String,String>();

        allNames.put( "en", "name" );
        allDescr.put( "en", "desc" );

        type = mock( TypeDTO.class );
        when( type.getAllNames() ).thenReturn( allNames );
        when (type.getAllDescriptions() ).thenReturn( allDescr );
        when( type.getDescription( "en" ) ).thenReturn( "desc" );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromTypeDTO( type, null, "path/", TypeCategory.NAME );
        assertEquals( new Integer( 1 ), typeModel.getId() );
        assertEquals( "code", typeModel.getCode() );
        assertEquals( new Boolean( true ), typeModel.isPublished() );
        assertEquals( "name", typeModel.getLocalizedName().get( 0 ).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get( 0 ).getDescription() );
        assertEquals( "en", typeModel.getLocalizedName().get( 0 ).getLocale() );
        assertEquals( "path/" + TypeMapper.NAME_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromPlaceNameType2() {
        ReadablePlaceDataService			mockDataService;
        TypeMapper							mapper;
        TypeModel							typeModel;
        PlaceNameType						type;
        StdLocale							locale = new StdLocale( "en" );

        type = mock( PlaceNameType.class );
        when( type.getName( locale ) ).thenReturn( new LocalizedData<String>( "name", locale ) );
        when( type.getDescription( locale ) ).thenReturn( new LocalizedData<String>( "desc", locale ) );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromType( type, locale, "path/", TypeCategory.NAME );
        assertEquals( new Integer( 1 ), typeModel.getId() );
        assertEquals( "code", typeModel.getCode() );
        assertEquals( new Boolean( true ), typeModel.isPublished() );
        assertEquals( "name", typeModel.getLocalizedName().get( 0 ).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get( 0 ).getDescription() );
        assertEquals( "en", typeModel.getLocalizedName().get( 0 ).getLocale() );
        assertEquals( "path/" + TypeMapper.NAME_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromAttributeType() {
        ReadablePlaceDataService            mockDataService;
        TypeMapper                          mapper;
        TypeModel                           typeModel;
        TypeDTO                             type;
        Map<String,String>                  allNames = new HashMap<String,String>();
        Map<String,String>                  allDescr = new HashMap<String,String>();

        allNames.put( "en", "name" );
        allDescr.put( "en", "desc" );

        type = mock( TypeDTO.class );
        when( type.getAllNames() ).thenReturn( allNames );
        when (type.getAllDescriptions() ).thenReturn( allDescr );
        when( type.getDescription( "en" ) ).thenReturn( "desc" );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromTypeDTO( type, null, "path/", TypeCategory.ATTRIBUTE );
        assertEquals( new Integer( 1 ), typeModel.getId() );
        assertEquals( "code", typeModel.getCode() );
        assertEquals( new Boolean( true ), typeModel.isPublished() );
        assertEquals( "name", typeModel.getLocalizedName().get( 0 ).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get( 0 ).getDescription() );
        assertEquals( "en", typeModel.getLocalizedName().get( 0 ).getLocale() );
        assertEquals( "path/" + TypeMapper.ATTRIBUTE_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromAttributeType2() {
        ReadablePlaceDataService            mockDataService;
        TypeMapper                          mapper;
        TypeModel                           typeModel;
        AttributeType                       type;
        StdLocale                           locale = new StdLocale( "en" );

        type = mock( AttributeType.class );
        when( type.getName( locale ) ).thenReturn( new LocalizedData<String>( "name", locale ) );
        when( type.getDescription( locale ) ).thenReturn( new LocalizedData<String>( "desc", locale ) );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromType( type, locale, "path/", TypeCategory.ATTRIBUTE );
        assertEquals( new Integer( 1 ), typeModel.getId() );
        assertEquals( "code", typeModel.getCode() );
        assertEquals( new Boolean( true ), typeModel.isPublished() );
        assertEquals( "name", typeModel.getLocalizedName().get( 0 ).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get( 0 ).getDescription() );
        assertEquals( "en", typeModel.getLocalizedName().get( 0 ).getLocale() );
        assertEquals( "path/" + TypeMapper.ATTRIBUTE_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromCitationType() {
        ReadablePlaceDataService            mockDataService;
        TypeMapper                          mapper;
        TypeModel                           typeModel;
        TypeDTO                             type;
        Map<String,String>                  allNames = new HashMap<String,String>();
        Map<String,String>                  allDescr = new HashMap<String,String>();

        allNames.put( "en", "name" );
        allDescr.put( "en", "desc" );

        type = mock( TypeDTO.class );
        when( type.getAllNames() ).thenReturn( allNames );
        when (type.getAllDescriptions() ).thenReturn( allDescr );
        when( type.getDescription( "en" ) ).thenReturn( "desc" );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromTypeDTO( type, null, "path/", TypeCategory.CITATION );
        assertEquals( new Integer( 1 ), typeModel.getId() );
        assertEquals( "code", typeModel.getCode() );
        assertEquals( new Boolean( true ), typeModel.isPublished() );
        assertEquals( "name", typeModel.getLocalizedName().get( 0 ).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get( 0 ).getDescription() );
        assertEquals( "en", typeModel.getLocalizedName().get( 0 ).getLocale() );
        assertEquals( "path/" + TypeMapper.CITATION_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testCreateModelFromCitationType2() {
        ReadablePlaceDataService            mockDataService;
        TypeMapper                          mapper;
        TypeModel                           typeModel;
        CitationType                        type;
        StdLocale                           locale = new StdLocale( "en" );

        type = mock( CitationType.class );
        when( type.getName( locale ) ).thenReturn( new LocalizedData<String>( "name", locale ) );
        when( type.getDescription( locale ) ).thenReturn( new LocalizedData<String>( "desc", locale ) );
        when( type.getId() ).thenReturn( 1 );
        when( type.getCode() ).thenReturn( "code" );
        when( type.isPublished() ).thenReturn( true );
        mockDataService = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( mockDataService );

        typeModel = mapper.createModelFromType( type, locale, "path/", TypeCategory.CITATION );
        assertEquals( new Integer( 1 ), typeModel.getId() );
        assertEquals( "code", typeModel.getCode() );
        assertEquals( new Boolean( true ), typeModel.isPublished() );
        assertEquals( "name", typeModel.getLocalizedName().get( 0 ).getName() );
        assertEquals( "desc", typeModel.getLocalizedName().get( 0 ).getDescription() );
        assertEquals( "en", typeModel.getLocalizedName().get( 0 ).getLocale() );
        assertEquals( "path/" + TypeMapper.CITATION_TYPE_PATH + 1, typeModel.getSelfLink().getHref() );
        assertEquals( "self", typeModel.getSelfLink().getRel() );
    }

    @Test(groups = {"unit"})
    public void testGetPlaceTypeDTOFromModel() {
        ReadablePlaceDataService		dataServiceMock;
        TypeModel						typeModel = new TypeModel();
        TypeMapper						mapper;
        TypeDTO							mockTypeDto;
        TypeDTO							typeDto;

        try {
            mockTypeDto = mock( TypeDTO.class );
            when( mockTypeDto.getId() ).thenReturn( 1 );
            when( mockTypeDto.getCode() ).thenReturn( "TYPE" );
            dataServiceMock = mock( ReadablePlaceDataService.class );
            when( dataServiceMock.getTypeById( TypeCategory.PLACE, 1 ) ).thenReturn( mockTypeDto );
            when( dataServiceMock.getTypeByCode( TypeCategory.PLACE, "TYPE" ) ).thenReturn( mockTypeDto );
            mapper = new TypeMapper( dataServiceMock );

            typeModel.setId( 1 );

            //Verify it can be retrieved by id.
            typeDto = mapper.getTypeDTOFromModel( typeModel, TypeCategory.PLACE );
            assertNotNull( typeDto );
            assertEquals( 1, typeDto.getId() );
            assertEquals( "TYPE", typeDto.getCode() );

            typeModel.setCode( "TYPE" );
            typeModel.setId( null );

            //Verify it can be retrieved by code.
            typeDto = mapper.getTypeDTOFromModel( typeModel, TypeCategory.PLACE );
            assertNotNull( typeDto );
            assertEquals( 1, typeDto.getId() );
            assertEquals( "TYPE", typeDto.getCode() );
        }
        catch ( PlaceDataException e ) {
            assertFalse( "Exception thrown when it wasn't expected: " + e.toString(), true );
        }
    }

    @Test(groups = {"unit"})
    public void testGetPlaceTypeDTOFromModelWithNullValues() {
        ReadablePlaceDataService		dataServiceMock;
        TypeMapper						mapper;

        dataServiceMock = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( dataServiceMock );

        try {
            //Exception should be thrown when a null model is passed in.
            mapper.getTypeDTOFromModel( null, TypeCategory.PLACE );
            assertFalse( "Expected exception, but it was not thrown.", true );
        }
        catch ( PlaceDataException e ) {
            assertTrue( true );
        }

        try {
            //Exception should be thrown when an empty model is passed in.
            mapper.getTypeDTOFromModel( new TypeModel(), TypeCategory.PLACE );
            assertFalse( "Expected exception, but it was not thrown.", true );
        }
        catch ( PlaceDataException e ) {
            assertTrue( true );
        }
    }

    @Test(groups = {"unit"})
    public void testGetPlaceNameTypeDTOFromModel() {
        ReadablePlaceDataService		dataServiceMock;
        TypeModel						typeModel = new TypeModel();
        TypeMapper						mapper;
        TypeDTO							mockTypeDto;
        TypeDTO							typeDto;

        try {
            mockTypeDto = mock( TypeDTO.class );
            when( mockTypeDto.getId() ).thenReturn( 1 );
            when( mockTypeDto.getCode() ).thenReturn( "TYPE" );
            dataServiceMock = mock( ReadablePlaceDataService.class );
            when( dataServiceMock.getTypeById( TypeCategory.NAME, 1 ) ).thenReturn( mockTypeDto );
            when( dataServiceMock.getTypeByCode( TypeCategory.NAME, "TYPE" ) ).thenReturn( mockTypeDto );
            mapper = new TypeMapper( dataServiceMock );

            typeModel.setId( 1 );

            //Verify it can be retrieved by id.
            typeDto = mapper.getTypeDTOFromModel( typeModel, TypeCategory.NAME );
            assertNotNull( typeDto );
            assertEquals( 1, typeDto.getId() );
            assertEquals( "TYPE", typeDto.getCode() );

            typeModel.setCode( "TYPE" );
            typeModel.setId( null );

            //Verify it can be retrieved by code.
            typeDto = mapper.getTypeDTOFromModel( typeModel, TypeCategory.NAME );
            assertNotNull( typeDto );
            assertEquals( 1, typeDto.getId() );
            assertEquals( "TYPE", typeDto.getCode() );
        }
        catch ( PlaceDataException e ) {
            assertFalse( "Exception thrown when it wasn't expected: " + e.toString(), true );
        }
    }

    @Test(groups = {"unit"})
    public void testGetPlaceNameTypeDTOFromModelWithNullValues() {
        ReadablePlaceDataService		dataServiceMock;
        TypeMapper						mapper;

        dataServiceMock = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( dataServiceMock );

        try {
            //Exception should be thrown when a null model is passed in.
            mapper.getTypeDTOFromModel( null, TypeCategory.NAME );
            assertFalse( "Expected exception, but it was not thrown.", true );
        }
        catch ( PlaceDataException e ) {
            assertTrue( true );
        }

        try {
            //Exception should be thrown when an empty model is passed in.
            mapper.getTypeDTOFromModel( new TypeModel(), TypeCategory.NAME );
            assertFalse( "Expected exception, but it was not thrown.", true );
        }
        catch ( PlaceDataException e ) {
            assertTrue( true );
        }
    }

    @Test(groups = {"unit"})
    public void testGetAttributeTypeDTOFromModel() {
        ReadablePlaceDataService        dataServiceMock;
        TypeModel                       typeModel = new TypeModel();
        TypeMapper                      mapper;
        TypeDTO                         mockTypeDto;
        TypeDTO                         typeDto;

        try {
            mockTypeDto = mock( TypeDTO.class );
            when( mockTypeDto.getId() ).thenReturn( 1 );
            when( mockTypeDto.getCode() ).thenReturn( "TYPE" );
            dataServiceMock = mock( ReadablePlaceDataService.class );
            when( dataServiceMock.getTypeById( TypeCategory.ATTRIBUTE, 1 ) ).thenReturn( mockTypeDto );
            when( dataServiceMock.getTypeByCode( TypeCategory.ATTRIBUTE, "TYPE" ) ).thenReturn( mockTypeDto );
            mapper = new TypeMapper( dataServiceMock );

            typeModel.setId( 1 );

            //Verify it can be retrieved by id.
            typeDto = mapper.getTypeDTOFromModel( typeModel, TypeCategory.ATTRIBUTE );
            assertNotNull( typeDto );
            assertEquals( 1, typeDto.getId() );
            assertEquals( "TYPE", typeDto.getCode() );

            typeModel.setCode( "TYPE" );
            typeModel.setId( null );

            //Verify it can be retrieved by code.
            typeDto = mapper.getTypeDTOFromModel( typeModel, TypeCategory.ATTRIBUTE );
            assertNotNull( typeDto );
            assertEquals( 1, typeDto.getId() );
            assertEquals( "TYPE", typeDto.getCode() );
        }
        catch ( PlaceDataException e ) {
            assertFalse( "Exception thrown when it wasn't expected: " + e.toString(), true );
        }
    }

    @Test(groups = {"unit"})
    public void testGetAttributeTypeDTOFromModelWithNullValues() {
        ReadablePlaceDataService        dataServiceMock;
        TypeMapper                      mapper;

        dataServiceMock = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( dataServiceMock );

        try {
            //Exception should be thrown when a null model is passed in.
            mapper.getTypeDTOFromModel( null, TypeCategory.ATTRIBUTE );
            assertFalse( "Expected exception, but it was not thrown.", true );
        }
        catch ( PlaceDataException e ) {
            assertTrue( true );
        }

        try {
            //Exception should be thrown when an empty model is passed in.
            mapper.getTypeDTOFromModel( new TypeModel(), TypeCategory.ATTRIBUTE );
            assertFalse( "Expected exception, but it was not thrown.", true );
        }
        catch ( PlaceDataException e ) {
            assertTrue( true );
        }
    }

    @Test(groups = {"unit"})
    public void testGetCitationTypeDTOFromModel() {
        ReadablePlaceDataService        dataServiceMock;
        TypeModel                       typeModel = new TypeModel();
        TypeMapper                      mapper;
        TypeDTO                         mockTypeDto;
        TypeDTO                         typeDto;

        try {
            mockTypeDto = mock( TypeDTO.class );
            when( mockTypeDto.getId() ).thenReturn( 1 );
            when( mockTypeDto.getCode() ).thenReturn( "TYPE" );
            dataServiceMock = mock( ReadablePlaceDataService.class );
            when( dataServiceMock.getTypeById( TypeCategory.CITATION, 1 ) ).thenReturn( mockTypeDto );
            when( dataServiceMock.getTypeByCode( TypeCategory.CITATION, "TYPE" ) ).thenReturn( mockTypeDto );
            mapper = new TypeMapper( dataServiceMock );

            typeModel.setId( 1 );

            //Verify it can be retrieved by id.
            typeDto = mapper.getTypeDTOFromModel( typeModel, TypeCategory.CITATION );
            assertNotNull( typeDto );
            assertEquals( 1, typeDto.getId() );
            assertEquals( "TYPE", typeDto.getCode() );

            typeModel.setCode( "TYPE" );
            typeModel.setId( null );

            //Verify it can be retrieved by code.
            typeDto = mapper.getTypeDTOFromModel( typeModel, TypeCategory.CITATION );
            assertNotNull( typeDto );
            assertEquals( 1, typeDto.getId() );
            assertEquals( "TYPE", typeDto.getCode() );
        }
        catch ( PlaceDataException e ) {
            assertFalse( "Exception thrown when it wasn't expected: " + e.toString(), true );
        }
    }

    @Test(groups = {"unit"})
    public void testGetCitationTypeDTOFromModelWithNullValues() {
        ReadablePlaceDataService        dataServiceMock;
        TypeMapper                      mapper;

        dataServiceMock = mock( ReadablePlaceDataService.class );
        mapper = new TypeMapper( dataServiceMock );

        try {
            //Exception should be thrown when a null model is passed in.
            mapper.getTypeDTOFromModel( null, TypeCategory.CITATION );
            assertFalse( "Expected exception, but it was not thrown.", true );
        }
        catch ( PlaceDataException e ) {
            assertTrue( true );
        }

        try {
            //Exception should be thrown when an empty model is passed in.
            mapper.getTypeDTOFromModel( new TypeModel(), TypeCategory.CITATION );
            assertFalse( "Expected exception, but it was not thrown.", true );
        }
        catch ( PlaceDataException e ) {
            assertTrue( true );
        }
    }
}
