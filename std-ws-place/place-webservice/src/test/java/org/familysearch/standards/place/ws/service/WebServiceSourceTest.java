package org.familysearch.standards.place.ws.service;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

import org.familysearch.standards.place.*;
import org.familysearch.standards.place.data.*;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.mapping.SourceMapper;
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServiceSourceTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServiceSource webService;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServiceSource realWebService = new WebServiceSource();
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
    // Get [Read] source by id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getSourceById() throws PlaceDataException {
        doReturn(this.createSourceDTO()).when(readableServiceMock).getSourceById(anyInt());

        Response response = webService.getSourceById(
            uriInfo,
            1234);

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getSource());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] all published attribute-types
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getSources() throws PlaceDataException {
        // Create three published attribute-types and one unpublished one
        Set<SourceDTO> sources = new HashSet<SourceDTO>();
        sources.add(this.createSourceDTO());
        sources.add(this.createSourceDTO());
        sources.add(this.createSourceDTO());
        SourceDTO source = mock(SourceDTO.class);
        when(source.getId()).thenReturn(-111);
        when(source.isPublished()).thenReturn(false);
        sources.add(source);

        doReturn(sources).when(readableServiceMock).getAllSources();

        Response response = webService.getSources(
            uriInfo,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getSources());
        assertEquals(rootModelOut.getSources().size(), 3);
    }

    //
    // ------------------------------------------------------------------------------------------------
    // POST [create] Citation-type
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createSource() throws PlaceDataException {
        RootModel inRoot = new RootModel();

        SourceDTO srcDTO = this.createSourceDTO();
        SourceModel srcModel = new SourceMapper().createModelFromDTO(srcDTO, "/test");
        inRoot.setSource(srcModel);

        doReturn(srcDTO).when(writableServiceMock).create(isA(SourceDTO.class), anyString());

        Response response = webService.createSource(uriInfo, inRoot);
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getSource());
        assertNotNull(response.getMetadata());
        assertNotNull(response.getMetadata().get("Location"));
        assertNotNull(response.getMetadata().get("Location").get(0));
        assertEquals(String.valueOf(response.getMetadata().get("Location").get(0)), "/test/places/sources/" + srcDTO.getId());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // PUT [update] Citation-type
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void updateSource() throws PlaceDataException {
        RootModel inRoot = new RootModel();

        SourceDTO srcDTO = this.createSourceDTO();
        SourceModel srcModel = new SourceMapper().createModelFromDTO(srcDTO, "/test");
        inRoot.setSource(srcModel);

        doReturn(srcDTO).when(writableServiceMock).update(isA(SourceDTO.class), anyString());

        Response response = webService.updateSource(uriInfo, inRoot);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getSource());
    }
}
