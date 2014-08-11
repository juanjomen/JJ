package org.familysearch.standards.place.ws.service;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

import org.familysearch.standards.place.*;
import org.familysearch.standards.place.data.*;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServiceXrefTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServiceXref webService;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServiceXref realWebService = new WebServiceXref();
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
    // Get [Read] all external-xref types, which for now is an empty list
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceXref() throws PlaceDataException {
        Response response = webService.getAllExtXrefTypes(
            uriInfo,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNull(rootModelOut.getLinks());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] place-reps by external xref
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceXrefById() throws PlaceDataException {
        List<ExternalXrefDTO> links = new ArrayList<>();
        links.add(new ExternalXrefDTO(1, 11, 22, "1234", true));
        links.add(new ExternalXrefDTO(3, 12, 22, "1234", true));
        links.add(new ExternalXrefDTO(5, 13, 22, "1234", true));

        doReturn(links).when(readableServiceMock).getExtXrefByTypeAndKey("CODE", "1234");

        Response response = webService.getPlaceRepsByLinkId(
            uriInfo,
            "CODE.1234");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getLinks());
        assertEquals(3, rootModelOut.getLinks().size());
    }

    @Test(groups = {"unit"} )
    public void getPlaceXrefByIdInvalidId() throws PlaceDataException {
        Response response = webService.getPlaceRepsByLinkId(
            uriInfo,
            "CODE-no-period-1234");

        assertNotNull(response);
        assertEquals(response.getStatus(), 400);
        assertNotNull(response.getEntity());
        assertEquals("Link ID is malformed - must be of the form <code>.<external-key>", response.getEntity().toString());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Update place-reps by external xref
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    @SuppressWarnings("unchecked")
    public void createOrUpdatePlaceRepsByLinkId() throws PlaceDataException {
        List<ExternalXrefDTO> links = new ArrayList<>();
        links.add(new ExternalXrefDTO(1, 11, 22, "1234", true));
        links.add(new ExternalXrefDTO(3, 12, 22, "1234", true));
        links.add(new ExternalXrefDTO(5, 13, 22, "1234", true));

        RootModel rootModel = new RootModel();
        rootModel.setRepIds(Arrays.asList(11, 12, 13));

        doReturn(links).when(writableServiceMock).createOrUpdate(eq("CODE"), eq("1234"), isA(List.class));

        Response response = webService.createOrUpdatePlaceRepsByLinkId(
            uriInfo,
            "CODE.1234",
            rootModel);

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getLinks());
        assertEquals(3, rootModelOut.getLinks().size());
    }

    @Test(groups = {"unit"} )
    public void createOrUpdatePlaceRepsByLinkIdInvalidId() throws PlaceDataException {
        Response response = webService.createOrUpdatePlaceRepsByLinkId(
            uriInfo,
            "CODE-no-period-1234",
            new RootModel());

        assertNotNull(response);
        assertEquals(response.getStatus(), 400);
        assertNotNull(response.getEntity());
        assertEquals("Link ID is malformed - must be of the form <code>.<external-key>", response.getEntity().toString());
    }
}
