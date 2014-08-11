package org.familysearch.standards.place.ws.service;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

import org.familysearch.standards.place.*;
import org.familysearch.standards.place.data.*;
import org.familysearch.standards.place.data.DataMetrics.Metric;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServicePlaceTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServicePlace webService;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServicePlace realWebService = new WebServicePlace();
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
    // POST [Create] place methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createPlace() throws PlaceDataException {
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlace(this.createModel(this.mockPlace(null)));
        rootModelIn.setPlaceRepresentation(this.createModel(this.mockPlaceRep()));
        rootModelIn.getPlace().getReps().add(rootModelIn.getPlaceRepresentation());

        doReturn(this.createDTO(this.mockPlace(null))).when(readableServiceMock).getPlaceById(anyInt(), eq((String)null));
        doReturn(this.createDTO(this.mockPlaceRep())).when(writableServiceMock).create(isA(PlaceDTO.class), isA(PlaceRepresentationDTO.class), anyString());
        doReturn(this.createDTO(this.mockPlaceType())).when(readableServiceMock).getTypeById(eq(TypeCategory.PLACE), anyInt());
        doReturn(this.createDTO(this.mockGroup())).when(readableServiceMock).getGroupById(eq(GroupType.PLACE_TYPE), anyInt());

        Response response = webService.createPlace(
            uriInfo,
            rootModelIn,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlace());
    }

    @Test(groups = {"unit"} )
    public void createPlaceNoPlaceRepException() throws PlaceDataException {
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlace(this.createModel(this.mockPlace(null)));
        rootModelIn.getPlace().getReps().clear();

        Response response = webService.createPlace(
            uriInfo,
            rootModelIn,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        assertNotNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // POST [Create] place-rep methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createPlaceRep() throws PlaceDataException {
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlaceRepresentation(this.createModel(this.mockPlaceRep()));

        doReturn(this.createDTO(this.mockPlace(null))).when(readableServiceMock).getPlaceById(anyInt(), eq((String)null));
        doReturn(this.createDTO(this.mockPlaceRep())).when(writableServiceMock).create(isA(PlaceRepresentationDTO.class), anyString());
        doReturn(this.createDTO(this.mockPlaceType())).when(readableServiceMock).getTypeById(eq(TypeCategory.PLACE), anyInt());
        doReturn(this.createDTO(this.mockGroup())).when(readableServiceMock).getGroupById(eq(GroupType.PLACE_TYPE), anyInt());

        Response response = webService.createPlaceRep(
            uriInfo,
            1234,
            rootModelIn,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlaceRepresentation());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] place methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlace() throws PlaceDataException {
        doReturn(this.createDTO(this.mockPlace(null))).when(readableServiceMock).getPlaceById(anyInt(), eq("v1.0"));

        Response response = webService.getPlace(
            uriInfo,
            1234,
            "en",
            "fr",
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlace());
    }

    @Test(groups = {"unit"} )
    public void getPlaceNotFound() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceById(anyInt(), eq("v1.0"));

        Response response = webService.getPlace(
            uriInfo,
            1234,
            "en",
            "fr",
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // PUT [Update] place methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void putPlace() throws PlaceDataException {
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlace(this.createModel(this.mockPlace(null)));

        doReturn(this.createDTO(this.mockPlace(null))).when(writableServiceMock).update(isA(PlaceDTO.class), anyString());

        Response response = webService.putPlace(
            uriInfo,
            1234,
            rootModelIn,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlace());
    }

    @Test(groups = {"unit"} )
    public void getHealth() {
        DataMetrics metrics = mock(DataMetrics.class);
        DataMetrics.Metric revisionMetric = new Metric();
        revisionMetric.setValue(123);

        when(solrServiceMock.getMetrics()).thenReturn(metrics);
        when(metrics.getCurrentRevisionNumber()).thenReturn(revisionMetric);

        Response response = webService.getHealth(uriInfo);
        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModel = (RootModel)response.getEntity();
        assertNotNull(rootModel.getHealthCheck());
        assertEquals(rootModel.getHealthCheck().getAPIVersion(), "1.1");
        assertEquals(rootModel.getHealthCheck().getWSVersion(), "v1.0");
        assertEquals(rootModel.getHealthCheck().getCurrentRevision(), Integer.valueOf(123));
        assertEquals(rootModel.getHealthCheck().getLinks().size(), 9);
        assertEquals(rootModel.getHealthCheck().getStatus(), "read-write");
    }

}
