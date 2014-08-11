package org.familysearch.standards.place.ws.service;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriInfo;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.*;
import org.familysearch.standards.place.PlaceRequest.FuzzyType;
import org.familysearch.standards.place.data.*;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServiceCommonTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServiceSearch webService;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServiceSearch realWebService = new WebServiceSearch();
        webService = spy(realWebService);

        doReturn(writableServiceMock).when(webService).getWritableService();
        doReturn(readableServiceMock).when(webService).getReadableService();
        doReturn(solrServiceMock).when(webService).getSolrService();
        doReturn(dbServiceMock).when(webService).getDbService();
        doReturn(wsVersion).when(webService).getWsVersion();
        doReturn(placeServiceMock).when(webService).getPlaceService();
        doReturn(placeServiceMock).when(webService).getPlaceService(anyString());
        doReturn(true).when(writableServiceMock).isWriteReady();
        doReturn(true).when(readableServiceMock).isReadReady();

        doReturn("1.1").when(placeServiceMock).getAPIVersion();

        URI baseURI = new URI("http://localhost:8080/test/");
        uriInfo = mock(UriInfo.class);
        when(uriInfo.getBaseUri()).thenReturn(baseURI);

        prBuilder = new PlaceRequestBuilder();
        prBuilder.setVersion("42");
    }


    @Test(groups = {"unit"} )
    public void PlaceWebService() {
        assertNotNull(webService);
    }

    @Test(groups = {"unit"} )
    public void getPath() {
        String expected = "/test/places/";

        assertEquals(webService.getPath(uriInfo), expected);
    }

    @Test(groups = {"unit"} )
    public void constructRequestFromRequestWrapper() {
        PlaceSearchRequestModel wrapper = new PlaceSearchRequestModel();
        wrapper.setText("the-text");
        wrapper.setRequiredYears("1900");
        wrapper.setOptionalYears("1950");
        wrapper.setOptionalParents("11");
        wrapper.setRequiredTypes("1");
        wrapper.setOptionalTypes("2");
        wrapper.setFilterParents("3");
        wrapper.setFilterTypes("4");
        wrapper.setFilterTypeGroups("5");
        wrapper.setReqTypeGroups("6");
        wrapper.setPriorityTypes("7");
        wrapper.setLimit(8);
        wrapper.setFilter(true);
        wrapper.setThreshold(9);
        wrapper.setPartial(true);
        wrapper.setLatitude(55.5);
        wrapper.setLongitude(66.6);
        wrapper.setDistance("10k");
        wrapper.setFuzzy("ed");
        wrapper.setRequiredParents("10");
        wrapper.setRequiredDirParents("11");
        wrapper.setPublishedType("pub_non_pub");
        wrapper.setValidatedType("val_non_val");

        String version = null;

        when(placeServiceMock.getPlaceRepresentation(anyInt(), eq(version))).thenReturn(mock(PlaceRepresentation.class));
        when(placeServiceMock.getPlaceTypeGroupById(anyInt())).thenReturn(mock(Group.class));
        try {
        	when(readableServiceMock.getTypeById( TypeCategory.PLACE, 1 )).thenReturn( mock(TypeDTO.class) );
        	when(readableServiceMock.getTypeById( TypeCategory.PLACE, 2 )).thenReturn( mock(TypeDTO.class) );
        	when(readableServiceMock.getTypeById( TypeCategory.PLACE, 4 )).thenReturn( mock(TypeDTO.class) );
        }
        catch ( PlaceDataException e ) {
        	assertTrue(false,"Error getting type dto: " + e.getMessage());
        }

        prBuilder = webService.constructRequestFromRequestWrapper(wrapper, placeServiceMock, StdLocale.ENGLISH);
        assertEquals(prBuilder.getText().get(), "the-text");
        assertEquals(prBuilder.getText().getLocale(), StdLocale.ENGLISH);
        assertNotNull(prBuilder.getRequiredDate());
        assertNotNull(prBuilder.getOptionalDate());
        assertEquals(prBuilder.getOptionalParents().size(), 1);
        assertEquals(prBuilder.getRequiredPlaceTypes().size(), 1);
        assertEquals(prBuilder.getOptionalPlaceTypes().size(), 1);
        assertEquals(prBuilder.getFilterParents().size(), 1);
        assertEquals(prBuilder.getFilteredPlaceTypes().size(), 1);
        assertEquals(prBuilder.getFilteredPlaceTypeGroups().size(), 1);
        assertEquals(prBuilder.getRequiredPlaceTypeGroups().size(), 1);
        assertEquals(prBuilder.getResultsLimit(), Integer.valueOf(8));
        assertTrue(prBuilder.shouldFilterResults());
        assertEquals(prBuilder.getFilterThreshold(), 9);
        assertTrue(prBuilder.isPartialInput());
        assertEquals(prBuilder.getCentroid().getLatitude(), 55.5, 0.0001);
        assertEquals(prBuilder.getCentroid().getLongitude(), 66.6, 0.0001);
        assertEquals(prBuilder.getDistanceInKM(), 10, 0.0001);
        assertEquals(prBuilder.getFuzzyType(), FuzzyType.EDIT_DISTANCE);
        assertEquals(prBuilder.getRequiredParents().size(), 1);
        assertEquals(prBuilder.getRequiredDirectParents().size(), 1);
        assertEquals(prBuilder.getPublishedType(), PlaceRequest.PublishedType.PUB_AND_NON_PUB);
        assertEquals(prBuilder.getValidatedType(), PlaceRequest.ValidatedType.VALIDATED_AND_NON_VALIDATED);
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Other random methods being tested
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getEntityTag() {
        String expected = "11-22";

        assertEquals(webService.getEntityTag(11, 22), expected);
    }

    @Test(groups = {"unit"} )
    public void getLocale() {
        StdLocale locale = webService.getLocale("fr", null);
        assertEquals(locale.toString(), "fr");
    }

    @Test(groups = {"unit"} )
    public void getLocaleNull() {
        StdLocale locale = webService.getLocale(null, null);
        assertEquals(locale.toString(), "en");

        locale = webService.getLocale("", null);
        assertEquals(locale.toString(), "en");
    }

    @Test(groups = {"unit"} )
    public void getLocaleWithHeader() {
        StdLocale locale = webService.getLocale(null, "fr");
        assertEquals(locale.toString(), "fr");

        locale = webService.getLocale(null, " ");
        assertEquals(locale.toString(), "en");
    }

    //
    // ------------------------------------------------------------------------------------------------
    // A variety of "handle...()" methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void handleParamCentroid() {
        assertNull(prBuilder.getCentroid());

        webService.handleParamCentroid(placeServiceMock, prBuilder, -55.5, 55.5);
        assertEquals(prBuilder.getCentroid().getLongitude(), -55.5, 0.0001);
        assertEquals(prBuilder.getCentroid().getLatitude(), 55.5, 0.0001);
    }

    @Test(groups = {"unit"} )
    public void handleParamCentroidInvalid() {
        assertNull(prBuilder.getCentroid());

        webService.handleParamCentroid(placeServiceMock, prBuilder, -55.5, null);
        assertNull(prBuilder.getCentroid());

        webService.handleParamCentroid(placeServiceMock, prBuilder, null, 55.5);
        assertNull(prBuilder.getCentroid());

        webService.handleParamCentroid(placeServiceMock, prBuilder, null, null);
        assertNull(prBuilder.getCentroid());
    }

    @Test(groups = {"unit"} )
    public void handleParamDistance() {
        assertEquals(prBuilder.getDistanceInKM(), PlaceRequest.DEFAULT_DISTANCE, 0.0001);

        webService.handleParamDistance(placeServiceMock, prBuilder, "55.5k");
        assertEquals(prBuilder.getDistanceInKM(), 55.5, 0.0001);

        webService.handleParamDistance(placeServiceMock, prBuilder, "66.6");
        assertEquals(prBuilder.getDistanceInKM(), 66.6, 0.0001);

        webService.handleParamDistance(placeServiceMock, prBuilder, "100.0m");
        assertEquals(prBuilder.getDistanceInKM(), 160.934, 0.0001);
    }

    @Test(groups = {"unit"} )
    public void handleParamDistanceInvalid() {
        assertEquals(prBuilder.getDistanceInKM(), PlaceRequest.DEFAULT_DISTANCE, 0.0001);

        webService.handleParamDistance(placeServiceMock, prBuilder, " ");
        assertEquals(prBuilder.getDistanceInKM(), PlaceRequest.DEFAULT_DISTANCE, 0.0001);

        webService.handleParamDistance(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getDistanceInKM(), PlaceRequest.DEFAULT_DISTANCE, 0.0001);

        webService.handleParamDistance(placeServiceMock, prBuilder, "1.1.X");
        assertEquals(prBuilder.getDistanceInKM(), PlaceRequest.DEFAULT_DISTANCE, 0.0001);
    }

    @Test(groups = {"unit"} )
    public void handleParamFilter() {
        assertFalse(prBuilder.shouldFilterResults());

        webService.handleParamFilter(placeServiceMock, prBuilder, false);
        assertFalse(prBuilder.shouldFilterResults());

        webService.handleParamFilter(placeServiceMock, prBuilder, true);
        assertTrue(prBuilder.shouldFilterResults());
    }

    @Test(groups = {"unit"} )
    public void handleParamFilterPlaces() {
        assertEquals(prBuilder.getFilterParents().size(), 0);

        when(placeServiceMock.getPlaceRepresentation(111, "42")).thenReturn(mock(PlaceRepresentation.class));
        when(placeServiceMock.getPlaceRepresentation(222, "42")).thenReturn(mock(PlaceRepresentation.class));

        webService.handleParamFilterPlaces(placeServiceMock, prBuilder, "111,222");
        assertEquals(prBuilder.getFilterParents().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamFilterPlacesException() {
        assertEquals(prBuilder.getFilterParents().size(), 0);

        webService.handleParamFilterPlaces(placeServiceMock, prBuilder, "111X,222");
        assertEquals(prBuilder.getFilterParents().size(), 0);

        webService.handleParamFilterPlaces(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getFilterParents().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamFilterTypeGroups() {
        assertEquals(prBuilder.getFilteredPlaceTypeGroups().size(), 0);

        when(placeServiceMock.getPlaceTypeGroupById(55)).thenReturn(mock(Group.class));
        when(placeServiceMock.getPlaceTypeGroupById(77)).thenReturn(mock(Group.class));

        webService.handleParamFilterTypeGroups(placeServiceMock, prBuilder, "55,77");
        assertEquals(prBuilder.getFilteredPlaceTypeGroups().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamFilterTypeGroupsException() {
        assertEquals(prBuilder.getFilteredPlaceTypeGroups().size(), 0);

        webService.handleParamFilterTypeGroups(placeServiceMock, prBuilder, "55X,77");
        assertEquals(prBuilder.getFilteredPlaceTypeGroups().size(), 0);

        webService.handleParamFilterTypeGroups(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getFilteredPlaceTypeGroups().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamFilterTypes() {
    	TypeDTO			dto1, dto2;

    	dto1 = mock( TypeDTO.class );
    	when( dto1.getId() ).thenReturn( 55 );
    	dto2 = mock( TypeDTO.class );
    	when( dto2.getId() ).thenReturn( 77 );

        assertEquals(prBuilder.getFilteredPlaceTypes().size(), 0);

        try {
	        when(readableServiceMock.getTypeById( TypeCategory.PLACE, 55 )).thenReturn(dto1);
	        when(readableServiceMock.getTypeByCode( TypeCategory.PLACE, "A1-STATE" )).thenReturn(dto2);
        }
        catch ( PlaceDataException e ) {
        	assertTrue( false, "Error getting type dto: " + e.getMessage() );
        }

        webService.handleParamFilterTypes(placeServiceMock, prBuilder, "55,A1-STATE");
        assertEquals(prBuilder.getFilteredPlaceTypes().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamFilterTypesException() {
        assertEquals(prBuilder.getFilteredPlaceTypes().size(), 0);

        webService.handleParamFilterTypes(placeServiceMock, prBuilder, "55X,77");
        assertEquals(prBuilder.getFilteredPlaceTypes().size(), 0);

        webService.handleParamFilterTypes(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getFilteredPlaceTypes().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamFuzzyType() {
        assertNull(prBuilder.getFuzzyType());

        webService.handleParamFuzzyType(placeServiceMock, prBuilder, "ed");
        assertEquals(prBuilder.getFuzzyType(), FuzzyType.EDIT_DISTANCE);
    }

    @Test(groups = {"unit"} )
    public void handleParamFuzzyTypeNoMatch() {
        assertNull(prBuilder.getFuzzyType());

        webService.handleParamFuzzyType(placeServiceMock, prBuilder, "no-match");
        assertNull(prBuilder.getFuzzyType());

        webService.handleParamFuzzyType(placeServiceMock, prBuilder, null);
        assertNull(prBuilder.getFuzzyType());
    }

    @Test(groups = {"unit"} )
    public void handleParamIncludeMetrics() {
        assertFalse(prBuilder.shouldCollectMetrics());

        webService.handleParamIncludeMetrics(placeServiceMock, prBuilder, true);
        assertTrue(prBuilder.shouldCollectMetrics());
    }

    @Test(groups = {"unit"} )
    public void handleParamOptionalDate() {
        assertNull(prBuilder.getOptionalDate());

        webService.handleParamOptionalDate(placeServiceMock, prBuilder, "1900-01-01");
        assertNotNull(prBuilder.getOptionalDate());
    }

    @Test(groups = {"unit"} )
    public void handleParamOptionalDateNull() {
        assertNull(prBuilder.getOptionalDate());

        webService.handleParamOptionalDate(placeServiceMock, prBuilder, null);
        assertNull(prBuilder.getOptionalDate());
    }

    @Test(groups = {"unit"} )
    public void handleParamOptionalDirectParent() {
        assertEquals(prBuilder.getOptionalDirectParents().size(), 0);

        when(placeServiceMock.getPlaceRepresentation(111, "42")).thenReturn(mock(PlaceRepresentation.class));
        when(placeServiceMock.getPlaceRepresentation(222, "42")).thenReturn(mock(PlaceRepresentation.class));

        webService.handleParamOptionalDirectParent(placeServiceMock, prBuilder, "111,222");
        assertEquals(prBuilder.getOptionalDirectParents().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamOptionalDirectParentException() {
        assertEquals(prBuilder.getOptionalDirectParents().size(), 0);

        webService.handleParamOptionalDirectParent(placeServiceMock, prBuilder, "111X,222");
        assertEquals(prBuilder.getOptionalDirectParents().size(), 0);

        webService.handleParamOptionalDirectParent(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getOptionalDirectParents().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamOptionalParent() {
        assertEquals(prBuilder.getOptionalParents().size(), 0);

        when(placeServiceMock.getPlaceRepresentation(111, "42")).thenReturn(mock(PlaceRepresentation.class));
        when(placeServiceMock.getPlaceRepresentation(222, "42")).thenReturn(mock(PlaceRepresentation.class));

        webService.handleParamOptionalParent(placeServiceMock, prBuilder, "111,222");
        assertEquals(prBuilder.getOptionalParents().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamOptionalParentException() {
        assertEquals(prBuilder.getOptionalParents().size(), 0);

        webService.handleParamOptionalParent(placeServiceMock, prBuilder, "111X,222");
        assertEquals(prBuilder.getOptionalParents().size(), 0);

        webService.handleParamOptionalParent(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getOptionalParents().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamOptionalTypes() {
    	TypeDTO			dto1, dto2;

    	dto1 = mock( TypeDTO.class );
    	when( dto1.getId() ).thenReturn( 55 );
    	dto2 = mock( TypeDTO.class );
    	when( dto2.getId() ).thenReturn( 77 );

    	assertEquals(prBuilder.getOptionalPlaceTypes().size(), 0);

        try {
	        when(readableServiceMock.getTypeById( TypeCategory.PLACE, 55 )).thenReturn(dto1);
	        when(readableServiceMock.getTypeByCode( TypeCategory.PLACE, "A1-STATE" )).thenReturn(dto2);
        }
        catch ( PlaceDataException e ) {
        	assertTrue( false, "Error getting type dto: " + e.getMessage() );
        }
//        when(placeServiceMock.getPlaceTypeById(55)).thenReturn(mock(PlaceType.class));
//        when(placeServiceMock.getPlaceTypeById(77)).thenReturn(mock(PlaceType.class));

        webService.handleParamOptionalTypes(placeServiceMock, prBuilder, "55,A1-STATE");
        assertEquals(prBuilder.getOptionalPlaceTypes().size(), 2);

        webService.handleParamOptionalTypes(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getOptionalPlaceTypes().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamOptionalTypesException() {
        assertEquals(prBuilder.getOptionalPlaceTypes().size(), 0);

        webService.handleParamOptionalTypes(placeServiceMock, prBuilder, "55X,77");
        assertEquals(prBuilder.getOptionalPlaceTypes().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamPartialInput() {
        assertFalse(prBuilder.isPartialInput());

        webService.handleParamPartialInput(placeServiceMock, prBuilder, true);
        assertTrue(prBuilder.isPartialInput());
    }

    @Test(groups = {"unit"} )
    public void handleParamPoints() {
        assertEquals(prBuilder.getPolygonPoints().size(), 0);

        webService.handleParamPoints(placeServiceMock, prBuilder, "11,11;22,22;33,33");
        assertEquals(prBuilder.getPolygonPoints().size(), 3);
    }

    @Test(groups = {"unit"} )
    public void handleParamPointsNull() {
        assertEquals(prBuilder.getPolygonPoints().size(), 0);

        webService.handleParamPoints(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getPolygonPoints().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamPriorityTypes() {
    	TypeDTO			dto1, dto2, dto3;

    	dto1 = mock( TypeDTO.class );
    	when( dto1.getId() ).thenReturn( 55 );
    	dto2 = mock( TypeDTO.class );
    	when( dto2.getId() ).thenReturn( 77 );
    	dto3 = mock( TypeDTO.class );
    	when( dto3.getId() ).thenReturn( 112 );

    	PlaceType plType01 = new PlaceType( dto1 );
        PlaceType plType02 = new PlaceType( dto2 );
        PlaceType plType03 = new PlaceType( dto3 );

        try {
	        when(readableServiceMock.getTypeById( TypeCategory.PLACE, 55 )).thenReturn(dto1);
	        when(readableServiceMock.getTypeById( TypeCategory.PLACE, 77 )).thenReturn(dto2);
	        when(readableServiceMock.getTypeByCode( TypeCategory.PLACE, "A1-STATE" )).thenReturn(dto3);
        }
        catch ( PlaceDataException e ) {
        	assertTrue( false, "Error getting type dto: " + e.getMessage() );
        }

        assertEquals(prBuilder.getPriorityForPlaceType(plType01), PlaceRequest.DEFAULT_PLACE_TYPE_PRIORITY);
        assertEquals(prBuilder.getPriorityForPlaceType(plType02), PlaceRequest.DEFAULT_PLACE_TYPE_PRIORITY);
        assertEquals(prBuilder.getPriorityForPlaceType(plType03), PlaceRequest.DEFAULT_PLACE_TYPE_PRIORITY);

        webService.handleParamPriorityTypes(placeServiceMock, prBuilder, "55:10,77:8,A1-STATE:7");
        assertEquals(prBuilder.getPriorityForPlaceType(plType01), 10);
        assertEquals(prBuilder.getPriorityForPlaceType(plType02), 8);
        assertEquals(prBuilder.getPriorityForPlaceType(plType03), 7);
    }

    @Test(groups = {"unit"} )
    public void handleParamPriorityTypesException() {
        PlaceType plType01 = mock(PlaceType.class);

        when(placeServiceMock.getPlaceTypeById(55)).thenReturn(plType01);

        assertEquals(prBuilder.getPriorityForPlaceType(plType01), PlaceRequest.DEFAULT_PLACE_TYPE_PRIORITY);

        webService.handleParamPriorityTypes(placeServiceMock, prBuilder, "55X:10");
        assertEquals(prBuilder.getPriorityForPlaceType(plType01), PlaceRequest.DEFAULT_PLACE_TYPE_PRIORITY);

        webService.handleParamPriorityTypes(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getPriorityForPlaceType(plType01), PlaceRequest.DEFAULT_PLACE_TYPE_PRIORITY);
    }

    @Test(groups = {"unit"} )
    public void handleParamPublished() {
        assertEquals(prBuilder.getPublishedType(), PlaceRequest.PublishedType.PUB_ONLY);

        webService.handleParamPublished(placeServiceMock, prBuilder, "pub_non_pub");
        assertEquals(prBuilder.getPublishedType(), PlaceRequest.PublishedType.PUB_AND_NON_PUB);
    }

    @Test(groups = {"unit"} )
    public void handleParamPublishedNoMatch() {
        assertEquals(prBuilder.getPublishedType(), PlaceRequest.PublishedType.PUB_ONLY);

        webService.handleParamPublished(placeServiceMock, prBuilder, "no-match");
        assertEquals(prBuilder.getPublishedType(), PlaceRequest.PublishedType.PUB_ONLY);

        webService.handleParamPublished(placeServiceMock, prBuilder, "");
        assertEquals(prBuilder.getPublishedType(), PlaceRequest.PublishedType.PUB_ONLY);

        webService.handleParamPublished(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getPublishedType(), PlaceRequest.PublishedType.PUB_ONLY);
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredDate() {
        assertNull(prBuilder.getRequiredDate());

        webService.handleParamRequiredDate(placeServiceMock, prBuilder, "1900-01-01");
        assertNotNull(prBuilder.getRequiredDate());
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredDateNull() {
        assertNull(prBuilder.getRequiredDate());

        webService.handleParamRequiredDate(placeServiceMock, prBuilder, null);
        assertNull(prBuilder.getRequiredDate());
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredDirectParent() {
        assertEquals(prBuilder.getRequiredDirectParents().size(), 0);

        when(placeServiceMock.getPlaceRepresentation(111, "42")).thenReturn(mock(PlaceRepresentation.class));
        when(placeServiceMock.getPlaceRepresentation(222, "42")).thenReturn(mock(PlaceRepresentation.class));

        webService.handleParamRequiredDirectParent(placeServiceMock, prBuilder, "111,222");
        assertEquals(prBuilder.getRequiredDirectParents().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredDirectParentException() {
        assertEquals(prBuilder.getRequiredDirectParents().size(), 0);

        webService.handleParamRequiredDirectParent(placeServiceMock, prBuilder, "111X,222");
        assertEquals(prBuilder.getRequiredDirectParents().size(), 0);

        webService.handleParamRequiredDirectParent(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getRequiredDirectParents().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredParent() {
        assertEquals(prBuilder.getRequiredParents().size(), 0);

        when(placeServiceMock.getPlaceRepresentation(111, "42")).thenReturn(mock(PlaceRepresentation.class));
        when(placeServiceMock.getPlaceRepresentation(222, "42")).thenReturn(mock(PlaceRepresentation.class));

        webService.handleParamRequiredParent(placeServiceMock, prBuilder, "111,222");
        assertEquals(prBuilder.getRequiredParents().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredParentException() {
        assertEquals(prBuilder.getRequiredParents().size(), 0);

        webService.handleParamRequiredParent(placeServiceMock, prBuilder, "111X,222");
        assertEquals(prBuilder.getRequiredParents().size(), 0);

        webService.handleParamRequiredParent(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getRequiredParents().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredTypeGroups() {
        assertEquals(prBuilder.getRequiredPlaceTypeGroups().size(), 0);

        when(placeServiceMock.getPlaceTypeGroupById(55)).thenReturn(mock(Group.class));
        when(placeServiceMock.getPlaceTypeGroupById(77)).thenReturn(mock(Group.class));

        webService.handleParamRequiredTypeGroups(placeServiceMock, prBuilder, "55,77");
        assertEquals(prBuilder.getRequiredPlaceTypeGroups().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredTypeGroupsException() {
        assertEquals(prBuilder.getRequiredPlaceTypeGroups().size(), 0);

        webService.handleParamRequiredTypeGroups(placeServiceMock, prBuilder, "55X,77");
        assertEquals(prBuilder.getRequiredPlaceTypeGroups().size(), 0);

        webService.handleParamRequiredTypeGroups(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getRequiredPlaceTypeGroups().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredTypes() {
    	TypeDTO			dto1, dto2;

    	dto1 = mock( TypeDTO.class );
    	when( dto1.getId() ).thenReturn( 55 );
    	dto2 = mock( TypeDTO.class );
    	when( dto2.getId() ).thenReturn( 77 );

    	assertEquals(prBuilder.getRequiredPlaceTypes().size(), 0);

        try {
	        when(readableServiceMock.getTypeById( TypeCategory.PLACE, 55 )).thenReturn(dto1);
	        when(readableServiceMock.getTypeByCode( TypeCategory.PLACE, "A1-STATE" )).thenReturn(dto2);
        }
        catch ( PlaceDataException e ) {
        	assertTrue( false, "Error getting type dto: " + e.getMessage() );
        }

        webService.handleParamRequiredTypes(placeServiceMock, prBuilder, "55,A1-STATE");
        assertEquals(prBuilder.getRequiredPlaceTypes().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void handleParamRequiredTypesException() {
        assertEquals(prBuilder.getRequiredPlaceTypes().size(), 0);

        webService.handleParamRequiredTypes(placeServiceMock, prBuilder, "55X,77");
        assertEquals(prBuilder.getRequiredPlaceTypes().size(), 0);

        webService.handleParamRequiredTypes(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getRequiredPlaceTypes().size(), 0);
    }

    @Test(groups = {"unit"} )
    public void handleParamResultsLimit() {
        assertNull(prBuilder.getResultsLimit());

        webService.handleParamResultsLimit(placeServiceMock, prBuilder, 111);
        assertEquals(prBuilder.getResultsLimit(), Integer.valueOf(111));
    }

    @Test(groups = {"unit"} )
    public void handleParamText() {
        assertNull(prBuilder.getText());

        webService.handleParamText(placeServiceMock, prBuilder, "SomeText", StdLocale.ENGLISH);
        assertNotNull(prBuilder.getText());
        assertEquals(prBuilder.getText().get(), "SomeText");
        assertEquals(prBuilder.getText().getLocale(), StdLocale.ENGLISH);
    }

    @Test(groups = {"unit"} )
    public void handleParamTextNull() {
        assertNull(prBuilder.getText());

        webService.handleParamText(placeServiceMock, prBuilder, " ", StdLocale.ENGLISH);
        assertNull(prBuilder.getText());

        webService.handleParamText(placeServiceMock, prBuilder, null, StdLocale.ENGLISH);
        assertNull(prBuilder.getText());
    }

    @Test(groups = {"unit"} )
    public void handleParamThreshold() {
        assertEquals(prBuilder.getFilterThreshold(), 0);

        webService.handleParamThreshold(placeServiceMock, prBuilder, 55);
        assertEquals(prBuilder.getFilterThreshold(), 55);

        webService.handleParamThreshold(placeServiceMock, prBuilder, -10);
        assertEquals(prBuilder.getFilterThreshold(), 55);
    }

    @Test(groups = {"unit"} )
    public void handleParamValidated() {
        assertEquals(prBuilder.getValidatedType(), PlaceRequest.ValidatedType.VALIDATED_AND_NON_VALIDATED);

        webService.handleParamValidated(placeServiceMock, prBuilder, "val_non_val");
        assertEquals(prBuilder.getValidatedType(), PlaceRequest.ValidatedType.VALIDATED_AND_NON_VALIDATED);
    }

    @Test(groups = {"unit"} )
    public void handleParamValidatedNoMatch() {
        assertEquals(prBuilder.getValidatedType(), PlaceRequest.ValidatedType.VALIDATED_AND_NON_VALIDATED);

        webService.handleParamValidated(placeServiceMock, prBuilder, "no-match");
        assertEquals(prBuilder.getValidatedType(), PlaceRequest.ValidatedType.VALIDATED_AND_NON_VALIDATED);

        webService.handleParamValidated(placeServiceMock, prBuilder, "");
        assertEquals(prBuilder.getValidatedType(), PlaceRequest.ValidatedType.VALIDATED_AND_NON_VALIDATED);

        webService.handleParamValidated(placeServiceMock, prBuilder, null);
        assertEquals(prBuilder.getValidatedType(), PlaceRequest.ValidatedType.VALIDATED_AND_NON_VALIDATED);
    }

    @Test(groups = {"unit"} )
    public void handleParamVersion() {
        prBuilder = new PlaceRequestBuilder();
        assertNull(prBuilder.getVersion());

        webService.handleParamVersion(placeServiceMock, prBuilder, "v1.1.1");
        assertEquals(prBuilder.getVersion(), "v1.1.1");
    }

    @Test(groups = {"unit"} )
    public void handleParamVersionNull() {
        prBuilder = new PlaceRequestBuilder();
        assertNull(prBuilder.getVersion());

        webService.handleParamVersion(placeServiceMock, prBuilder, " ");
        assertNull(prBuilder.getVersion());

        webService.handleParamVersion(placeServiceMock, prBuilder, null);
        assertNull(prBuilder.getVersion());
    }

    @Test(groups = {"unit"} )
    public void handleParamWildcards() {
        assertFalse(prBuilder.shouldUseWildcards());

        webService.handleParamWildcards(placeServiceMock, prBuilder, true);
        assertTrue(prBuilder.shouldUseWildcards());
    }
}
