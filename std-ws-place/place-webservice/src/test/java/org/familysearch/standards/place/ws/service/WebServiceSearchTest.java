package org.familysearch.standards.place.ws.service;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

import org.familysearch.standards.place.*;
import org.familysearch.standards.place.data.*;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.search.RequestMetrics;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServiceSearchTest extends WebServiceBaseTest {

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

    //
    // ------------------------------------------------------------------------------------------------
    // GET [Search] place-reps methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void requestPlaceRepsUriInfoFull() {
        PlaceResults placeResults = mock(PlaceResults.class);
        RequestMetrics metrics = mock(RequestMetrics.class);
        when(placeResults.getMetrics()).thenReturn(metrics);

        doReturn(this.mockPlaceRep()).when(placeServiceMock).getPlaceRepresentation(anyInt(), eq("42"));
        doReturn(this.mockGroup()).when(placeServiceMock).getPlaceTypeGroupById(anyInt());
        doReturn(this.mockPlaceType()).when(placeServiceMock).getPlaceTypeById(anyInt());
        when(placeServiceMock.requestPlaces(isA(PlaceRequest.class))).thenReturn(placeResults);
        doReturn(new PlaceRepresentation[] { this.mockPlaceRep(), this.mockPlaceRep() }).when(placeResults).getPlaceRepresentations();

        Response response = webService.requestPlaceReps(
            uriInfo,
            "the-text",
            "1900",
            "1950",
            "11",
            "12",
            "13",
            true,
            14,
            15,
            PlaceService.DEFAULT_PROFILE,
            "en",
            "fr",
            "en",
            "fr",
            "16",
            "17",
            "18",
            "19",
            "20",
            "21",
            true,
            55.5,
            66.6,
            "11,11;22,22;33,33",
            "10k",
            true,
            "ed_60",
            "22",
            "23",
            true,
            "42",
            "pub_non_pub",
            "val_non_val",
            false);

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModel = (RootModel)response.getEntity();
        assertNotNull(rootModel.getSearchResults());
        assertEquals(rootModel.getSearchResults().size(), 1);
        assertNotNull(rootModel.getSearchResults().get(0).getMetrics());
        assertEquals(rootModel.getSearchResults().get(0).getResults().size(), 2);
    }

    @Test(groups = {"unit"} )
    public void requestPlaceRepsUriInfoRootModelStringString() {
        RootModel rootModelIn = new RootModel();

        PlaceResults placeResults = mock(PlaceResults.class);
        RequestMetrics metrics = mock(RequestMetrics.class);

        List<PlaceSearchRequestModel> reqList = new ArrayList<PlaceSearchRequestModel>();
        reqList.add(mock(PlaceSearchRequestModel.class));

        rootModelIn.setRequests(reqList);

        when(placeResults.getMetrics()).thenReturn(metrics);
        when(placeServiceMock.requestPlaces(isA(PlaceRequest.class))).thenReturn(placeResults);
        doReturn(this.mockPlaceRep()).when(placeServiceMock).getPlaceRepresentation(anyInt(), eq("42"));
        doReturn(this.mockGroup()).when(placeServiceMock).getPlaceTypeGroupById(anyInt());
        doReturn(this.mockPlaceType()).when(placeServiceMock).getPlaceTypeById(anyInt());
        doReturn(new PlaceRepresentation[] { this.mockPlaceRep(), this.mockPlaceRep() }).when(placeResults).getPlaceRepresentations();

        Response response = webService.requestPlaceReps(
            uriInfo,
            rootModelIn,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getSearchResults());
        assertEquals(rootModelOut.getSearchResults().size(), 1);
        assertNull(rootModelOut.getSearchResults().get(0).getMetrics());
        assertEquals(rootModelOut.getSearchResults().get(0).getResults().size(), 2);
    }
}
