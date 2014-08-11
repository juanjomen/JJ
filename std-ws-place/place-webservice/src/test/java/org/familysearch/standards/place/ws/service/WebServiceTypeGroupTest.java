package org.familysearch.standards.place.ws.service;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

import org.familysearch.standards.place.*;
import org.familysearch.standards.place.data.*;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.mapping.GroupMapper;
import org.familysearch.standards.place.ws.model.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class WebServiceTypeGroupTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServiceTypeGroup webService;

    private GroupMapper grpMapper;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        WebServiceTypeGroup realWebService = new WebServiceTypeGroup();
        webService = spy(realWebService);

        grpMapper = new GroupMapper(readableServiceMock);

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
    // Get [Read] place-type-group by id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceTypeGroupById() throws PlaceDataException {
        doReturn(this.createDTO(this.mockGroup())).when(readableServiceMock).getGroupById(eq(GroupType.PLACE_TYPE), anyInt());
        doReturn(this.createDTO(this.mockPlaceType())).when(readableServiceMock).getTypeById(eq(TypeCategory.PLACE), anyInt());
        doReturn(this.mockGroup()).when(placeServiceMock).getPlaceTypeGroupById(anyInt());

        Response response = webService.getPlaceTypeGroupById(
            uriInfo,
            1234,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlaceTypeGroup());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] all published place-type-groups
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceTypeGroups() throws PlaceDataException {
        // Create three published place-type-groups and one unpublished one
        Set<GroupDTO> placeTypeGroups = new HashSet<GroupDTO>();
        placeTypeGroups.add(this.createDTO(this.mockGroup()));
        placeTypeGroups.add(this.createDTO(this.mockGroup()));
        placeTypeGroups.add(this.createDTO(this.mockGroup()));
        Group placeType = this.mockGroup();
        when(placeType.isPublished()).thenReturn(false);
        placeTypeGroups.add(this.createDTO(placeType));

        doReturn(placeTypeGroups).when(readableServiceMock).getAllGroups(GroupType.PLACE_TYPE);

        Response response = webService.getPlaceTypeGroups(uriInfo);
        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlaceTypeGroups());
        assertEquals(rootModelOut.getPlaceTypeGroups().size(), 4);  // Was 3, but now ALL types are returned
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Post [Create] a new place-rep group
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createPlaceTypeGroup() throws PlaceDataException {
        PlaceTypeGroupModel ptgModelIn = this.makePlaceTypeGroupModel(0, false, "en", "name-en", "desc-en");
        PlaceTypeGroupModel ptgModelOut = this.makePlaceTypeGroupModel(1, false, "en", "name-en", "desc-en");
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlaceTypeGroup(ptgModelIn);

        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.PLACE, 1);
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.PLACE, 3);
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.PLACE, 5);
        doReturn(grpMapper.createDTOFromTypeGroup(ptgModelOut, true)).when(writableServiceMock).create(isA(GroupDTO.class), eq("system"));

        Response response = webService.createPlaceTypeGroup(uriInfo, rootModelIn);
        assertNotNull(response);
        assertEquals(response.getStatus(), 201);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        PlaceTypeGroupModel ptgModelNew = rootModelOut.getPlaceTypeGroup();
        assertNotNull(ptgModelNew);
        assertEquals(1, ptgModelNew.getId());
        assertEquals("/test/places/type-groups/1", ptgModelNew.getSelfLink().getHref());
        assertEquals(0, ptgModelNew.getSubGroups().size());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Post [Create] a new place-rep group
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void updatePlaceTypeGroup() throws PlaceDataException {
        PlaceTypeGroupModel ptgModelIn = this.makePlaceTypeGroupModel(1, false, "en", "name-en", "desc-en");
        PlaceTypeGroupModel ptgModelOut = this.makePlaceTypeGroupModel(1, false, "en", "name-en", "desc-en");
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlaceTypeGroup(ptgModelIn);

        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.PLACE, 1);
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.PLACE, 3);
        doReturn(mock(TypeDTO.class)).when(readableServiceMock).getTypeById(TypeCategory.PLACE, 5);
        doReturn(grpMapper.createDTOFromTypeGroup(ptgModelOut, true)).when(writableServiceMock).update(isA(GroupDTO.class), eq("system"));

        Response response = webService.updatePlaceTypeGroup(uriInfo, rootModelIn);
        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        PlaceTypeGroupModel ptgModelNew = rootModelOut.getPlaceTypeGroup();
        assertNotNull(ptgModelNew);
        assertEquals(1, ptgModelNew.getId());
        assertEquals("/test/places/type-groups/1", ptgModelNew.getSelfLink().getHref());
        assertEquals(0, ptgModelNew.getSubGroups().size());
    }


    /**
     * Create a 'PlaceTypeGroupModel' instance with mostly dummy values.
     * 
     * @param id identifier
     * @param makeChildren TRUE if we should add children, FALSE otherwise
     * @param localeNameDesc triplets of locale + name + description
     * @return new 'PlaceTypeGroupModel' instance
     */
    private PlaceTypeGroupModel makePlaceTypeGroupModel(int id, boolean makeChildren, String... localeNameDesc) {
        List<LocalizedNameDescModel> names = new ArrayList<>();
        for (int i=0;  i<localeNameDesc.length;  i+=3) {
            names.add(makeNameAndDesc(localeNameDesc[i], localeNameDesc[i+1], localeNameDesc[i+2]));
        }

        List<TypeModel> types = new ArrayList<>();
        for (int i=1;  i<=5;  i+=2) {
            TypeModel type = mock(TypeModel.class);
            when(type.getId()).thenReturn(i);
            types.add(type);
        }

        List<PlaceTypeGroupModel> subGroups = new ArrayList<>();
        if (makeChildren) {
            subGroups.add(makePlaceTypeGroupModel(id+1, false, "en", "sub-group-01", "sub-group-01"));
            subGroups.add(makePlaceTypeGroupModel(id+2, false, "en", "sub-group-02", "sub-group-02"));
        }

        PlaceTypeGroupModel model = new PlaceTypeGroupModel();
        model.setId(id);
        model.setIsPublished(true);
        model.setName(names);
        model.setTypes(types);
        model.setSubGroups(subGroups);

        return model;
    }

    /**
     * Create a localized name given the locale, name and description
     * 
     * @param locale locale
     * @param name name
     * @param desc description
     * @return localized name/description
     */
    private LocalizedNameDescModel makeNameAndDesc(String locale, String name, String desc) {
        LocalizedNameDescModel nameDesc = new LocalizedNameDescModel();

        nameDesc.setLocale(locale);
        nameDesc.setName(name);
        nameDesc.setDescription(desc);

        return nameDesc;
    }
}
