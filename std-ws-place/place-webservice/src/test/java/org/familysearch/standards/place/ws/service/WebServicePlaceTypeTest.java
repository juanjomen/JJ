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

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.*;
import org.familysearch.standards.place.data.*;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.mapping.TypeMapper;
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServicePlaceTypeTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServicePlaceType webService;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServicePlaceType realWebService = new WebServicePlaceType();
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
    // Get [Read] place-type by id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceTypeById() throws PlaceDataException {
        doReturn(this.createDTO(this.mockPlaceType())).when(readableServiceMock).getTypeById(eq(TypeCategory.PLACE), anyInt());

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
    // Get [Read] all published place-types
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceTypes() throws PlaceDataException {
        // Create three published place-types and one unpublished one
        Set<TypeDTO> placeTypes = new HashSet<TypeDTO>();
        placeTypes.add(this.createDTO(this.mockPlaceType()));
        placeTypes.add(this.createDTO(this.mockPlaceType()));
        placeTypes.add(this.createDTO(this.mockPlaceType()));
        PlaceType placeType = this.mockPlaceType();
        when(placeType.isPublished()).thenReturn(false);
        placeTypes.add(this.createDTO(placeType));

        doReturn(placeTypes).when(readableServiceMock).getAllTypes(TypeCategory.PLACE);

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
    // POST [create] Name-type
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createNameType() throws PlaceDataException {
        RootModel inRoot = new RootModel();

        PlaceType type = this.mockPlaceType();
        TypeModel typeModel = new TypeMapper(readableServiceMock).createModelFromType(type, StdLocale.ENGLISH, "/test", TypeCategory.PLACE);
        inRoot.setType(typeModel);

        doReturn(this.createDTO(type)).when(writableServiceMock).create(eq(TypeCategory.PLACE), isA(TypeDTO.class), anyString());

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
        assertEquals(String.valueOf(response.getMetadata().get("Location").get(0)), "/test/places/place-types/" + type.getId());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // POST [update] Name-type
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void updateNameType() throws PlaceDataException {
        RootModel inRoot = new RootModel();

        PlaceType type = this.mockPlaceType();
        TypeModel typeModel = new TypeMapper(readableServiceMock).createModelFromType(type, StdLocale.ENGLISH, "/test", TypeCategory.PLACE);
        inRoot.setType(typeModel);

        doReturn(this.createDTO(type)).when(writableServiceMock).update(eq(TypeCategory.PLACE), isA(TypeDTO.class), anyString());

        Response response = webService.updateType(uriInfo, inRoot);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getType());
    }
}
