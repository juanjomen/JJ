package org.familysearch.standards.place.ws.service;

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


public class WebServicePlaceRepGroupTest extends WebServiceBaseTest {

    /** Note: this will actually be a 'spy' on the real service */
    private WebServicePlaceRepGroup webService;

    private GroupMapper grpMapper;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws URISyntaxException {
        writableServiceMock = mock(WritablePlaceDataService.class);
        readableServiceMock = mock(ReadablePlaceDataService.class);
        solrServiceMock = mock(SolrDataService.class);
        dbServiceMock = mock(DbDataService.class);
        placeServiceMock = mock(PlaceService.class);
        wsVersion = "v1.0";

        grpMapper = new GroupMapper(readableServiceMock);

        WebServicePlaceRepGroup realWebService = new WebServicePlaceRepGroup();
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
    // Get [Read] place-type-group by id methods
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceRepGroupById() throws PlaceDataException {
        doReturn(this.createDTO(this.mockGroup())).when(readableServiceMock).getGroupById(eq(GroupType.PLACE_REP), anyInt());

        Response response = webService.getPlaceRepGroupById(
            uriInfo,
            1234,
            "en",
            "fr");

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlaceRepGroup());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Get [Read] all published place-type-groups
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void getPlaceRepGroups() throws PlaceDataException {
        // Create three published place-type-groups and one unpublished one
        Set<GroupDTO> placePlaceRepGroups = new HashSet<GroupDTO>();
        placePlaceRepGroups.add(this.createDTO(this.mockGroup()));
        placePlaceRepGroups.add(this.createDTO(this.mockGroup()));
        placePlaceRepGroups.add(this.createDTO(this.mockGroup()));
        Group placeType = this.mockGroup();
        when(placeType.isPublished()).thenReturn(false);
        placePlaceRepGroups.add(this.createDTO(placeType));

        doReturn(placePlaceRepGroups).when(readableServiceMock).getAllGroups(GroupType.PLACE_REP);

        Response response = webService.getPlaceRepGroups(uriInfo);
        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        assertNotNull(rootModelOut.getPlaceRepGroups());
        assertEquals(rootModelOut.getPlaceRepGroups().size(), 4);  // Was 3, but now ALL types are returned
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Post [Create] a new place-rep group
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void createPlaceRepGroup() throws PlaceDataException {
        PlaceRepGroupModel prgModelIn = this.makePlaceRepGroupModel(0, false, "en", "name-en", "desc-en");
        PlaceRepGroupModel prgModelOut = this.makePlaceRepGroupModel(1, false, "en", "name-en", "desc-en");
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlaceRepGroup(prgModelIn);

        doReturn(grpMapper.createDTOFromPlaceRepGroup(prgModelOut, true)).when(writableServiceMock).create(isA(GroupDTO.class), eq("system"));

        Response response = webService.createPlaceRepGroup(uriInfo, rootModelIn);
        assertNotNull(response);
        assertEquals(response.getStatus(), 201);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        PlaceRepGroupModel prgModelNew = rootModelOut.getPlaceRepGroup();
        assertNotNull(prgModelNew);
        assertEquals(1, prgModelNew.getId());
        assertEquals("/test/places/place-rep-groups/1", prgModelNew.getSelfLink().getHref());
        assertEquals(3, prgModelNew.getRepSummaries().size());
        assertEquals(0, prgModelNew.getSubGroups().size());
    }

    //
    // ------------------------------------------------------------------------------------------------
    // Post [Create] a new place-rep group
    // ------------------------------------------------------------------------------------------------
    //
    @Test(groups = {"unit"} )
    public void updatePlaceRepGroup() throws PlaceDataException {
        PlaceRepGroupModel prgModelIn = this.makePlaceRepGroupModel(1, false, "en", "name-en", "desc-en");
        PlaceRepGroupModel prgModelOut = this.makePlaceRepGroupModel(1, false, "en", "name-en", "desc-en");
        RootModel rootModelIn = new RootModel();
        rootModelIn.setPlaceRepGroup(prgModelIn);

        doReturn(grpMapper.createDTOFromPlaceRepGroup(prgModelOut, true)).when(writableServiceMock).update(isA(GroupDTO.class), eq("system"));

        Response response = webService.updatePlaceRepGroup(uriInfo, rootModelIn);
        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof RootModel);

        RootModel rootModelOut = (RootModel)response.getEntity();
        PlaceRepGroupModel prgModelNew = rootModelOut.getPlaceRepGroup();
        assertNotNull(prgModelNew);
        assertEquals(1, prgModelNew.getId());
        assertEquals("/test/places/place-rep-groups/1", prgModelNew.getSelfLink().getHref());
        assertEquals(3, prgModelNew.getRepSummaries().size());
        assertEquals(0, prgModelNew.getSubGroups().size());
    }

    /**
     * Create a 'PlaceRepGroupModel' instance with mostly dummy values.
     * 
     * @param id identifier
     * @param makeChildren TRUE if we should add children, FALSE otherwise
     * @param localeNameDesc triplets of locale + name + description
     * @return new 'PlaceRepGroupModel' instance
     */
    private PlaceRepGroupModel makePlaceRepGroupModel(int id, boolean makeChildren, String... localeNameDesc) {
        List<LocalizedNameDescModel> names = new ArrayList<>();
        for (int i=0;  i<localeNameDesc.length;  i+=3) {
            names.add(makeNameAndDesc(localeNameDesc[i], localeNameDesc[i+1], localeNameDesc[i+2]));
        }

        List<PlaceRepSummaryModel> summaries = new ArrayList<>();
        PlaceRepSummaryModel summary;
        summary = new PlaceRepSummaryModel();
        summary.setId( 1 );
        summaries.add( summary );
        summary = new PlaceRepSummaryModel();
        summary.setId( 3 );
        summaries.add( summary );
        summary = new PlaceRepSummaryModel();
        summary.setId( 5 );
        summaries.add( summary );

        List<PlaceRepGroupModel> subGroups = new ArrayList<>();
        if (makeChildren) {
            subGroups.add(makePlaceRepGroupModel(id+1, false, "en", "sub-group-01", "sub-group-01"));
            subGroups.add(makePlaceRepGroupModel(id+2, false, "en", "sub-group-02", "sub-group-02"));
        }

        PlaceRepGroupModel model = new PlaceRepGroupModel();
        model.setId(id);
        model.setIsPublished(true);
        model.setName(names);
        model.setRepSummaries( summaries );
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
