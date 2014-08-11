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
import org.familysearch.standards.place.ws.mapping.AttributeMapper;
import org.familysearch.standards.place.ws.mapping.CitationMapper;
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServicePlaceRepTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServicePlaceRep webService;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServicePlaceRep realWebService = new WebServicePlaceRep();
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
    // DELETE [Delete] place-rep methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void deletePlaceRep() throws PlaceDataException {
        String nullStr = null;
        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, nullStr);
        doReturn(this.createDTO(this.mockPlaceType())).when(readableServiceMock).getTypeById(eq(TypeCategory.PLACE), anyInt());
        doReturn(this.createDTO(this.mockPlace(null))).when(readableServiceMock).getPlaceById(anyInt(), eq((String)null));
        doReturn(this.createDTO(this.mockGroup())).when(readableServiceMock).getGroupById(eq(GroupType.PLACE_TYPE), anyInt());

        doReturn(mock(PlaceRepresentationDTO.class)).when(writableServiceMock).delete(isA(PlaceRepresentationDTO.class), eq(200), anyString());

        Response response = webService.deletePlaceRep(
            uriInfo,
            1234,
            200);

        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // GET [Read] place-rep methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceRep() throws PlaceDataException {
        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");
        doReturn(this.createDTO(this.mockPlaceType())).when(readableServiceMock).getTypeById(eq(TypeCategory.PLACE), anyInt());
        doReturn(this.createDTO(this.mockPlace(null))).when(readableServiceMock).getPlaceById(anyInt(), eq((String)null));
        doReturn(this.createDTO(this.mockGroup())).when(readableServiceMock).getGroupById(eq(GroupType.PLACE_TYPE), anyInt());

        Response response = webService.getPlaceRep(
            uriInfo,
            1234,
            "en",
            "fr",
            "v1.0",
            false,
            true);

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlaceRepresentation());
    }

    @Test(groups = {"unit"} )
    public void getPlaceRepNotFound() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");

        Response response = webService.getPlaceRep(
            uriInfo,
            1234,
            "en",
            "fr",
            "v1.0",
            false,
            true);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] attributes by place-rep-id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getAttributesByRepId() throws PlaceDataException {
        Set<AttributeDTO> attributeSet = new HashSet<AttributeDTO>();
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");
        doReturn(attributeSet).when(readableServiceMock).getAttributesByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.ATTRIBUTE, 1234);

        Response response = webService.getPlaceRepAttrs(
            uriInfo,
            1234,
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getAttributes());
        assertEquals(rootModelOut.getAttributes().size(), 4);
    }

    @Test(groups = {"unit"} )
    public void getAttributesByRepIdNoPlaceRep() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");

        Response response = webService.getPlaceRepAttrs(
            uriInfo,
            1234,
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // POST [Create] attribute methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createAttribute() throws PlaceDataException {
        AttributeDTO attrDTO = this.createAttributeDTO();

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(10, null);
        doReturn(this.createDTO(this.mockAttributeType())).when(readableServiceMock).getTypeById(eq(TypeCategory.ATTRIBUTE), anyInt());
        doReturn(attrDTO).when(writableServiceMock).create(isA(AttributeDTO.class), eq("system"));

        AttributeModel attrModel = new AttributeMapper(readableServiceMock).createModelFromDTO(attrDTO, "/places/test");
        RootModel rootModelIn = new RootModel();
        rootModelIn.setAttribute(attrModel);

        Response response = webService.createPlaceRepAttr(uriInfo, 10, rootModelIn);

        assertNotNull(response);
        assertEquals(response.getStatus(), 201);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getAttribute());
        assertNotNull(response.getMetadata());
        assertNotNull(response.getMetadata().get("Location"));
        assertNotNull(response.getMetadata().get("Location").get(0));
        assertEquals(String.valueOf(response.getMetadata().get("Location").get(0)), "/test/places/reps/10/attributes/" + attrModel.getId());
    }

    @Test(groups = {"unit"} )
    public void createAttributeNotFound() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(10, null);

        RootModel rootModelIn = new RootModel();

        Response response = webService.createPlaceRepAttr(uriInfo, 10, rootModelIn);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] specific attribute by place-rep-id and attr-id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getAttributeByRepIdAndAttrId() throws PlaceDataException {
        Set<AttributeDTO> attributeSet = new HashSet<AttributeDTO>();
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");
        doReturn(attributeSet).when(readableServiceMock).getAttributesByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.ATTRIBUTE, 1234);

        Response response = webService.getPlaceRepAttrById(
            uriInfo,
            1234,
            attributeSet.toArray(new AttributeDTO[0])[0].getId(),  // Ensure that the attribute ID matches
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getAttribute());
    }

    @Test(groups = {"unit"} )
    public void getAttributeByRepIdAndAttrIdNoPlaceRep() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(10, null);

        Response response = webService.getPlaceRepAttrById(
                uriInfo,
                1234,
                -1111,
                "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test(groups = {"unit"} )
    public void getAttributeByRepIdAndAttrIdNoAttr() throws PlaceDataException {
        Set<AttributeDTO> attributeSet = new HashSet<AttributeDTO>();
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");
        doReturn(attributeSet).when(readableServiceMock).getAttributesByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.ATTRIBUTE, 1234);

        Response response = webService.getPlaceRepAttrById(
            uriInfo,
            1234,
            -1111,
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Update specific attribute by place-rep-id and attr-id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void updateAttributeByRepIdAndAttrId() throws PlaceDataException {
        AttributeDTO attrDTO = this.createAttributeDTO();

        Set<AttributeDTO> attributeSet = new HashSet<AttributeDTO>();
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());

        int placeRepId = 11;
        int attrId = attributeSet.iterator().next().getId();

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(placeRepId, null);
        doReturn(this.createDTO(this.mockAttributeType())).when(readableServiceMock).getTypeById(eq(TypeCategory.ATTRIBUTE), anyInt());
        doReturn(attrDTO).when(writableServiceMock).update(isA(AttributeDTO.class), eq("system"));
        doReturn(attributeSet).when(readableServiceMock).getAttributesByRepId(placeRepId, null);
        
        AttributeModel attrModel = new AttributeMapper(readableServiceMock).createModelFromDTO(attrDTO, "/places/test");
        RootModel rootModelIn = new RootModel();
        rootModelIn.setAttribute(attrModel);

        Response response = webService.updatePlaceRepAttr(uriInfo, placeRepId, attrId, rootModelIn);

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getAttribute());
        assertNotNull(response.getMetadata());
    }

    @Test(groups = {"unit"} )
    public void updateAttributeByRepIdAndAttrIdNoPlaceRep() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(10, null);

        Response response = webService.updatePlaceRepAttr(uriInfo, 10, 20, new RootModel());

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test(groups = {"unit"} )
    public void updateAttributeByRepIdAndAttrIdNoAttr() throws PlaceDataException {
        Set<AttributeDTO> attributeSet = new HashSet<AttributeDTO>();
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, null);
        doReturn(attributeSet).when(readableServiceMock).getAttributesByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.ATTRIBUTE, 1234);

        Response response = webService.updatePlaceRepAttr(uriInfo, 1234, -22, new RootModel());

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Delete specific attribute by place-rep-id and attr-id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void deleteAttributeByRepIdAndAttrId() throws PlaceDataException {
        AttributeDTO attrDTO = this.createAttributeDTO();

        Set<AttributeDTO> attributeSet = new HashSet<AttributeDTO>();
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());

        int placeRepId = 11;
        int attrId = attributeSet.iterator().next().getId();

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(placeRepId, null);
        doReturn(this.createDTO(this.mockAttributeType())).when(readableServiceMock).getTypeById(eq(TypeCategory.ATTRIBUTE), anyInt());
        doReturn(attrDTO).when(writableServiceMock).delete(isA(AttributeDTO.class), eq("system"));
        doReturn(attributeSet).when(readableServiceMock).getAttributesByRepId(placeRepId, null);

        Response response = webService.deletePlaceRepAttr(uriInfo, placeRepId, attrId);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test(groups = {"unit"} )
    public void deleteAttributeByRepIdAndAttrIdNoPlaceRep() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(10, null);

        Response response = webService.deletePlaceRepAttr(uriInfo, 10, 20);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test(groups = {"unit"} )
    public void deleteAttributeByRepIdAndAttrIdNoAttr() throws PlaceDataException {
        Set<AttributeDTO> attributeSet = new HashSet<AttributeDTO>();
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());
        attributeSet.add(this.createAttributeDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, null);
        doReturn(attributeSet).when(readableServiceMock).getAttributesByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.ATTRIBUTE, 1234);

        Response response = webService.deletePlaceRepAttr(uriInfo, 1234, -22);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] citations by place-rep-id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getCitationsByRepId() throws PlaceDataException {
        Set<CitationDTO> citationSet = new HashSet<CitationDTO>();
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");
        doReturn(citationSet).when(readableServiceMock).getCitationsByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.CITATION, 333);

        Response response = webService.getPlaceRepCitations(
            uriInfo,
            1234,
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getCitations());
        assertEquals(rootModelOut.getCitations().size(), 4);
    }

    @Test(groups = {"unit"} )
    public void getCitationsByRepIdNotFound() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");

        Response response = webService.getPlaceRepCitations(
            uriInfo,
            1234,
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] specific citation by place-rep-id and attr-id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getCitationByRepIdAndAttrId() throws PlaceDataException {
        Set<CitationDTO> citationSet = new HashSet<CitationDTO>();
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");
        doReturn(citationSet).when(readableServiceMock).getCitationsByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.CITATION, 333);

        Response response = webService.getPlaceRepCitationById(
            uriInfo,
            1234,
            citationSet.toArray(new CitationDTO[0])[0].getId(),  // Ensure that the citation ID matches
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getCitation());
    }

    @Test(groups = {"unit"} )
    public void getCitationByRepIdAndAttrIdNoAttr() throws PlaceDataException {
        Set<CitationDTO> citationSet = new HashSet<CitationDTO>();
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");
        doReturn(citationSet).when(readableServiceMock).getCitationsByRepId(1234, "v1.0");

        Response response = webService.getPlaceRepCitationById(
            uriInfo,
            1234,
            -1111,
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test(groups = {"unit"} )
    public void getCitationByRepIdAndAttrIdNoPlaceRep() throws PlaceDataException {
        // Simulate an exception
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(1234, "v1.0");

        Response response = webService.getPlaceRepCitationById(
            uriInfo,
            1234,
            1111,
            "v1.0");

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // PUT [Update] place-rep methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void putPlaceRep() throws PlaceDataException {
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlaceRepresentation(this.createModel(this.mockPlaceRep()));

        doReturn(this.createDTO(this.mockPlace(null))).when(readableServiceMock).getPlaceById(anyInt(), eq((String)null));
        doReturn(this.createDTO(this.mockPlaceRep())).when(writableServiceMock).update(isA(PlaceRepresentationDTO.class), anyString());
        doReturn(this.createDTO(this.mockPlaceType())).when(readableServiceMock).getTypeById(eq(TypeCategory.PLACE), anyInt());
        doReturn(this.createDTO(this.mockGroup())).when(readableServiceMock).getGroupById(eq(GroupType.PLACE_TYPE), anyInt());

        Response response = webService.putPlaceRep(
            uriInfo,
            rootModelIn.getPlaceRepresentation().getId(),
            rootModelIn,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlaceRepresentation());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // POST [Create] citation methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createCitation() throws PlaceDataException {
        CitationDTO citDTO = this.createCitationDTO();

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(10, null);
        doReturn(this.createDTO(this.mockCitationType())).when(readableServiceMock).getTypeById(eq(TypeCategory.CITATION), anyInt());
        doReturn(citDTO).when(writableServiceMock).create(isA(CitationDTO.class), eq("system"));

        CitationModel citModel = new CitationMapper(readableServiceMock).createModelFromDTO(citDTO, "/places/test");

        RootModel rootModelIn = new RootModel();
        rootModelIn.setCitation(citModel);

        Response response = webService.createPlaceRepCitation(uriInfo, 10, rootModelIn);

        assertNotNull(response);
        assertEquals(response.getStatus(), 201);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getCitation());
        assertNotNull(response.getMetadata());
        assertNotNull(response.getMetadata().get("Location"));
        assertNotNull(response.getMetadata().get("Location").get(0));
        assertEquals(String.valueOf(response.getMetadata().get("Location").get(0)), "/test/places/reps/10/citations/" + citModel.getId());
    }

    @Test(groups = {"unit"} )
    public void createCitationNotFound() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(10, null);

        RootModel rootModelIn = new RootModel();

        Response response = webService.createPlaceRepCitation(uriInfo, 10, rootModelIn);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Update specific citation by place-rep-id and cit-id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void updateCitationByRepIdAndAttrId() throws PlaceDataException {
        CitationDTO citDTO = this.createCitationDTO();

        Set<CitationDTO> citationSet = new HashSet<CitationDTO>();
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());

        int placeRepId = 11;
        int citId = citationSet.iterator().next().getId();

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(placeRepId, null);
        doReturn(this.createDTO(this.mockCitationType())).when(readableServiceMock).getTypeById(eq(TypeCategory.CITATION), anyInt());
        doReturn(citDTO).when(writableServiceMock).update(isA(CitationDTO.class), eq("system"));
        doReturn(citationSet).when(readableServiceMock).getCitationsByRepId(placeRepId, null);
        
        CitationModel citModel = new CitationMapper(readableServiceMock).createModelFromDTO(citDTO, "/places/test");
        RootModel rootModelIn = new RootModel();
        rootModelIn.setCitation(citModel);

        Response response = webService.updatePlaceRepCitation(uriInfo, placeRepId, citId, rootModelIn);

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getCitation());
        assertNotNull(response.getMetadata());
    }

    @Test(groups = {"unit"} )
    public void updateCitationByRepIdAndAttrIdNoPlaceRep() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(10, null);

        Response response = webService.updatePlaceRepCitation(uriInfo, 10, 20, new RootModel());

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test(groups = {"unit"} )
    public void updateCitationByRepIdAndAttrIdNoAttr() throws PlaceDataException {
        Set<CitationDTO> citationSet = new HashSet<CitationDTO>();
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, null);
        doReturn(citationSet).when(readableServiceMock).getCitationsByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.CITATION, 1234);

        Response response = webService.updatePlaceRepCitation(uriInfo, 1234, 20, new RootModel());

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Delete specific citation by place-rep-id and cit-id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void deleteCitationByRepIdAndCitationId() throws PlaceDataException {
        CitationDTO citDTO = this.createCitationDTO();

        Set<CitationDTO> citationSet = new HashSet<CitationDTO>();
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());

        int placeRepId = 11;
        int citId = citationSet.iterator().next().getId();

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(placeRepId, null);
        doReturn(this.createDTO(this.mockCitationType())).when(readableServiceMock).getTypeById(eq(TypeCategory.CITATION), anyInt());
        doReturn(citDTO).when(writableServiceMock).delete(isA(CitationDTO.class), eq("system"));
        doReturn(citationSet).when(readableServiceMock).getCitationsByRepId(placeRepId, null);

        Response response = webService.deletePlaceRepCitation(uriInfo, placeRepId, citId);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test(groups = {"unit"} )
    public void deleteCitationByRepIdAndCitationIdNoPlaceRep() throws PlaceDataException {
        doReturn(null).when(readableServiceMock).getPlaceRepresentationById(10, null);

        Response response = webService.deletePlaceRepCitation(uriInfo, 10, 20);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

    @Test(groups = {"unit"} )
    public void deleteCitationByRepIdAndCitationIdNoCitation() throws PlaceDataException {
        Set<CitationDTO> citationSet = new HashSet<CitationDTO>();
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());
        citationSet.add(this.createCitationDTO());

        doReturn(this.createDTO(this.mockPlaceRep())).when(readableServiceMock).getPlaceRepresentationById(1234, null);
        doReturn(citationSet).when(readableServiceMock).getCitationsByRepId(1234, "v1.0");
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.CITATION, 1234);

        Response response = webService.deletePlaceRepCitation(uriInfo, 1234, -22);

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNull(response.getEntity());
    }

}
