package org.familysearch.standards.place.ws.service;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
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
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServiceCitationTypeTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServiceCitationType webService;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServiceCitationType realWebService = new WebServiceCitationType();
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
    // Get [Read] citation-type by id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getCitationTypeById() throws PlaceDataException {
        doReturn(this.createDTO(this.mockCitationType())).when(readableServiceMock).getTypeById(eq(TypeCategory.CITATION), anyInt());

        Response response = webService.getTypeById(
            uriInfo,
            1234);

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getType());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] all published citation-types
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getCitationTypes() throws PlaceDataException {
        // Create three published citation-types and one unpublished one
        Set<TypeDTO> attrTypes = new HashSet<TypeDTO>();
        attrTypes.add(this.createDTO(this.mockCitationType()));
        attrTypes.add(this.createDTO(this.mockCitationType()));
        attrTypes.add(this.createDTO(this.mockCitationType()));
        CitationType attrType = this.mockCitationType();
        when(attrType.isPublished()).thenReturn(false);
        attrTypes.add(this.createDTO(attrType));

        doReturn(attrTypes).when(readableServiceMock).getAllTypes(TypeCategory.CITATION);

        Response response = webService.getAllTypes(
            uriInfo,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getTypes());
        assertEquals(rootModelOut.getTypes().size(), 3);
    }

    //
    // ------------------------------------------------------------------------------------------------
    // POST [create] Citation-type
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createCitationType() throws PlaceDataException {
        RootModel inRoot = new RootModel();

        CitationType type = this.mockCitationType();
        inRoot.setType(this.createType(type));

        doReturn(this.createDTO(type)).when(writableServiceMock).create(eq(TypeCategory.CITATION), isA(TypeDTO.class), anyString());

        Response response = webService.createType(uriInfo, inRoot);
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getType());
        assertNotNull(response.getMetadata());
        assertNotNull(response.getMetadata().get("Location"));
        assertNotNull(response.getMetadata().get("Location").get(0));
        assertEquals(String.valueOf(response.getMetadata().get("Location").get(0)), "/test/places/citation-types/" + type.getId());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // PUT [update] Citation-type
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void updateCitationType() throws PlaceDataException {
        RootModel inRoot = new RootModel();

        CitationType type = this.mockCitationType();
        inRoot.setType(this.createType(type));

        doReturn(this.createDTO(type)).when(writableServiceMock).update(eq(TypeCategory.CITATION), isA(TypeDTO.class), anyString());

        Response response = webService.updateType(uriInfo, inRoot);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getType());
    }
}
