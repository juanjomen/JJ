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


public class WebServiceAttributeTypeTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServiceAttributeType webService;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServiceAttributeType realWebService = new WebServiceAttributeType();
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
    // Get [Read] attribute-type by id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getAttributeTypeById() throws PlaceDataException {
        doReturn(this.createDTO(this.mockAttributeType())).when(readableServiceMock).getTypeById(eq(TypeCategory.ATTRIBUTE), anyInt());

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
    // Get [Read] all published attribute-types
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getAttributeTypes() throws PlaceDataException {
        // Create three published attribute-types and one unpublished one
        Set<TypeDTO> attrTypes = new HashSet<TypeDTO>();
        attrTypes.add(this.createDTO(this.mockAttributeType()));
        attrTypes.add(this.createDTO(this.mockAttributeType()));
        attrTypes.add(this.createDTO(this.mockAttributeType()));
        AttributeType attrType = this.mockAttributeType();
        when(attrType.isPublished()).thenReturn(false);
        attrTypes.add(this.createDTO(attrType));

        doReturn(attrTypes).when(readableServiceMock).getAllTypes(TypeCategory.ATTRIBUTE);

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
    // POST [create] attribute-type
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createAttributeType() throws PlaceDataException {
        RootModel inRoot = new RootModel();

        AttributeType type = this.mockAttributeType();
        TypeModel typeModel = new TypeMapper(readableServiceMock).createModelFromType(type, StdLocale.ENGLISH, "/test", TypeCategory.ATTRIBUTE);
        inRoot.setType(typeModel);

        doReturn(this.createDTO(type)).when(writableServiceMock).create(eq(TypeCategory.ATTRIBUTE), isA(TypeDTO.class), anyString());

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
        assertEquals(String.valueOf(response.getMetadata().get("Location").get(0)), "/test/places/attribute-types/" + type.getId());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // PUT [update] attribute-type
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void updateAttributeType() throws PlaceDataException {
        RootModel inRoot = new RootModel();

        AttributeType type = this.mockAttributeType();
        TypeModel typeModel = new TypeMapper(readableServiceMock).createModelFromType(type, StdLocale.ENGLISH, "/test", TypeCategory.ATTRIBUTE);
        inRoot.setType(typeModel);

        doReturn(this.createDTO(type)).when(writableServiceMock).update(eq(TypeCategory.ATTRIBUTE), isA(TypeDTO.class), anyString());

        Response response = webService.updateType(uriInfo, inRoot);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getType());
    }
}
