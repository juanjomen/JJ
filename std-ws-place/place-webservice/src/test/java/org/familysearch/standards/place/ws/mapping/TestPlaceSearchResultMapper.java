package org.familysearch.standards.place.ws.mapping;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.PlaceRepresentation;
import org.familysearch.standards.place.data.GroupType;
import org.familysearch.standards.place.data.PlaceDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.PlaceRepresentationDTO;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.scoring.Scorer;
import org.familysearch.standards.place.search.ParsedInputText;
import org.familysearch.standards.place.search.RequestMetrics;
import org.familysearch.standards.place.search.interp.Interpretation;
import org.familysearch.standards.place.search.parse.ParsedToken;
import org.familysearch.standards.place.search.spatial.SpatialResultMetadata;
import org.familysearch.standards.place.ws.model.MetricsModel;
import org.familysearch.standards.place.ws.model.PlaceSearchResultModel;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class TestPlaceSearchResultMapper {

    PlaceSearchResultMapper mapper;
    ReadablePlaceDataService service;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws PlaceDataException {
        service = mock(ReadablePlaceDataService.class);
        mapper = new PlaceSearchResultMapper(service);

//        when(service.getVersionFromRevision(anyInt())).thenReturn("v1.0");
    }


    @Test(groups = {"unit"})
    public void PlaceSearchResultMapper() {
        assertNotNull(mapper);
    }

    @Test(groups = {"unit"})
    public void createModelFromMetrics() {
        Scorer scorer01 = mock(Scorer.class);
        Scorer scorer02 = mock(Scorer.class);
        Scorer scorer03 = mock(Scorer.class);

        RequestMetrics metrics = new RequestMetrics();
        metrics.setParseTime(11L);
        metrics.setIdentifyCandidatesTime(12L);
        metrics.setIdentifyCandidateLookupTime(13L);
        metrics.setIdentifyCandidateMaxHitFilterTime(14L);
        metrics.setIdentifyCandidateTailMatchTime(15L);
        metrics.setScoringTime(16L);
        metrics.setAssemblyTime(17L);
        metrics.setTotalTime(18L);

        metrics.setRawCandidateCount(19);
        metrics.setPreScoringCandidateCount(20);
        metrics.setInitialParsedInputTextCount(21);
        metrics.setFinalParsedInputTextCount(22);

        metrics.setScorerTime(scorer01, 101L);
        metrics.setScorerTime(scorer02, 102L);
        metrics.setScorerTime(scorer03, 103L);

        MetricsModel metricsModel = mapper.createModelFromMetrics(metrics);
        assertNotNull(metricsModel);
        assertEquals(metricsModel.getTimings().getParseTime(), Long.valueOf(11));
        assertEquals(metricsModel.getTimings().getIdentifyCandidatesTime(), Long.valueOf(12L));
        assertEquals(metricsModel.getTimings().getIdentifyCandidatesLookupTime(), Long.valueOf(13L));
        assertEquals(metricsModel.getTimings().getIdentifyCandidatesMaxHitFilterTime(), Long.valueOf(14L));
        assertEquals(metricsModel.getTimings().getIdentifyCandidatesTailMatchTime(), Long.valueOf(15L));
        assertEquals(metricsModel.getTimings().getScoringTime(), Long.valueOf(16L));
        assertEquals(metricsModel.getTimings().getAssemblyTime(), Long.valueOf(17L));
        assertEquals(metricsModel.getTimings().getTotalTime(), Long.valueOf(18L));

        assertEquals(metricsModel.getCounts().getRawCandidateCount(), Integer.valueOf(19));
        assertEquals(metricsModel.getCounts().getPreScoringCandidateCount(), Integer.valueOf(20));
        assertEquals(metricsModel.getCounts().getInitialParsedInputTextCount(), Integer.valueOf(21));
        assertEquals(metricsModel.getCounts().getFinalParsedInputTextCount(), Integer.valueOf(22));

        assertEquals(metricsModel.getScorers().getScorers().size(), 3);
    }

    @Test(groups = {"unit"})
    public void createSearchResultFromPlaceRepresentation() throws PlaceDataException {
        // Grand-Parent information
        PlaceRepresentationDTO gparPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> gparNamesMap = new HashMap<String,String>();
        gparNamesMap.put("en", "Name-EN");
        gparNamesMap.put("fr", "Name-FR");

        when(gparPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 1 });
        when(gparPlaceRepDTO.getDisplayNames()).thenReturn(gparNamesMap);

        // Parent information
        PlaceRepresentationDTO parPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> parNamesMap = new HashMap<String,String>();
        parNamesMap.put("en", "Name-EN");
        parNamesMap.put("fr", "Name-FR");

        when(parPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 11, 1 });

        // Leaf information
        PlaceRepresentationDTO placeRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> namesMap = new HashMap<String,String>();
        namesMap.put("en", "Name-EN");
        namesMap.put("fr", "Name-FR");

        // Other mocks
        PlaceDTO ownerMock = mock(PlaceDTO.class);
        GroupDTO typeGroupMock = mock(GroupDTO.class);

        Map<String,String> naMap = new HashMap<String,String>();
        naMap.put("en", "Group-Name");
        Map<String,String> deMap = new HashMap<String,String>();
        deMap.put("en", "Group-Description");

        when(placeRepDTO.getJurisdictionChain()).thenReturn(new int[] { 111, 11, 1 });
        when(placeRepDTO.getDisplayNames()).thenReturn(namesMap);
        when(placeRepDTO.getUUID()).thenReturn("abcd-1234");
        when(placeRepDTO.getRevision()).thenReturn(55);
        when(placeRepDTO.getType()).thenReturn(17);
        when(placeRepDTO.getFromYear()).thenReturn(1900);
        when(placeRepDTO.getToYear()).thenReturn(2000);
        when(placeRepDTO.getOwnerId()).thenReturn(157);
        when(placeRepDTO.getPreferredLocale()).thenReturn("en");
        when(placeRepDTO.isPublished()).thenReturn(true);
        when(placeRepDTO.isValidated()).thenReturn(true);
        when(placeRepDTO.getLatitude()).thenReturn(55.5);
        when(placeRepDTO.getLongitude()).thenReturn(-111.1);
        when(placeRepDTO.getTypeGroup()).thenReturn(166);

        when(ownerMock.getId()).thenReturn(157);
        when(typeGroupMock.getId()).thenReturn(166);
        when(typeGroupMock.getNames()).thenReturn(naMap);
        when(typeGroupMock.getDescriptions()).thenReturn(deMap);

        when(service.getPlaceRepresentations(isA(int[].class), anyString()))
            .thenReturn(new PlaceRepresentationDTO[] { placeRepDTO, parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { gparPlaceRepDTO });
        when(service.getTypeById(eq(TypeCategory.PLACE), anyInt())).thenReturn(mock(TypeDTO.class));
        when(service.getPlaceById(157, null)).thenReturn(ownerMock);
        when(service.getGroupById(GroupType.PLACE_TYPE, 166)).thenReturn(typeGroupMock);

        PlaceRepresentation placeRep = new PlaceRepresentation(service, placeRepDTO);

        PlaceSearchResultModel searchResultModel =
                mapper.createSearchResultFromPlaceRepresentation(placeRep, new StdLocale("en"), "http://localhost/blah/", false);

        assertNotNull(searchResultModel);
        assertNotNull(searchResultModel.getRep());
        assertEquals(searchResultModel.getRep().getId(), Integer.valueOf(111));
        assertEquals(searchResultModel.getRep().getDisplayName().getLocale(), "en");
        assertEquals(searchResultModel.getRep().getDisplayName().getName(), "Name-EN");
        assertEquals(searchResultModel.getRep().getFromYear(), Integer.valueOf(1900));
        assertEquals(searchResultModel.getRep().getToYear(), Integer.valueOf(2000));
        assertEquals(searchResultModel.getRep().getFullDisplayName().getLocale(), "en");
        assertEquals(searchResultModel.getRep().getGroup().getId(), 166);
        assertEquals(searchResultModel.getRep().getGroup().getLocalizedName().get(0).getName(), "Group-Name");
        assertEquals(searchResultModel.getRep().getGroup().getLocalizedName().get(0).getDescription(), "Group-Description");
        assertEquals(searchResultModel.getRep().getJurisdiction().getId(), 11);
        assertEquals(searchResultModel.getRep().getLinks().size(), 2);
        assertEquals(searchResultModel.getRep().getLinks().get(0).getHref(), "http://localhost/blah/reps/111");
        assertEquals(searchResultModel.getRep().getLinks().get(1).getHref(), "http://localhost/blah/157");
        assertEquals(searchResultModel.getRep().getLocation().getCentroid().getLatitude(), 55.5, 0.0001);
        assertEquals(searchResultModel.getRep().getLocation().getCentroid().getLongitude(), -111.1, 0.0001);
        assertEquals(searchResultModel.getRep().getPreferredLocale(), "en");
        assertEquals(searchResultModel.getRep().getRevision(), Integer.valueOf(55));
        assertEquals(searchResultModel.getRep().getType().getId(), Integer.valueOf(17));
        assertEquals(searchResultModel.getRep().getUUID(), "abcd-1234");
        assertTrue(searchResultModel.getRep().isPublished());
        assertTrue(searchResultModel.getRep().isValidated());
    }

    @Test(groups = {"unit"})
    public void createSearchResultFromPlaceRepresentationSearchMetaData() throws PlaceDataException {
        // Grand-Parent information
        PlaceRepresentationDTO gparPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> gparNamesMap = new HashMap<String,String>();
        gparNamesMap.put("en", "Name-EN");
        gparNamesMap.put("fr", "Name-FR");

        when(gparPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 1 });
        when(gparPlaceRepDTO.getDisplayNames()).thenReturn(gparNamesMap);

        // Parent information
        PlaceRepresentationDTO parPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> parNamesMap = new HashMap<String,String>();
        parNamesMap.put("en", "Name-EN");
        parNamesMap.put("fr", "Name-FR");

        when(parPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 11, 1 });

        // Leaf information
        PlaceRepresentationDTO placeRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> namesMap = new HashMap<String,String>();
        namesMap.put("en", "Name-EN");
        namesMap.put("fr", "Name-FR");

        // Metadata
        List<ParsedToken>	tokens = new ArrayList<ParsedToken>();
        tokens.add( new ParsedToken( "token" ) );
        ParsedInputText		inputText = new ParsedInputText( "token", tokens );
//        SearchResultMetadata metadata = new SearchResultMetadata(placeRepDTO, 66, 77);
        Interpretation		metadata;

        // Other mocks
        PlaceDTO ownerMock = mock(PlaceDTO.class);
        GroupDTO typeGroupMock = mock(GroupDTO.class);

        when(placeRepDTO.getJurisdictionChain()).thenReturn(new int[] { 111, 11, 1 });
        when(placeRepDTO.getDisplayNames()).thenReturn(namesMap);
        when(placeRepDTO.getUUID()).thenReturn("abcd-1234");
        when(placeRepDTO.getRevision()).thenReturn(55);
        when(placeRepDTO.getType()).thenReturn(17);
        when(placeRepDTO.getFromYear()).thenReturn(1900);
        when(placeRepDTO.getToYear()).thenReturn(2000);
        when(placeRepDTO.getOwnerId()).thenReturn(157);
        when(placeRepDTO.getPreferredLocale()).thenReturn("en");
        when(placeRepDTO.isPublished()).thenReturn(true);
        when(placeRepDTO.isValidated()).thenReturn(true);
        when(placeRepDTO.getLatitude()).thenReturn(55.5);
        when(placeRepDTO.getLongitude()).thenReturn(-111.1);
        when(placeRepDTO.getTypeGroup()).thenReturn(166);

        when(ownerMock.getId()).thenReturn(157);
        when(typeGroupMock.getId()).thenReturn(166);
        when(typeGroupMock.getName("en")).thenReturn("Group-Name");
        when(typeGroupMock.getDescription("en")).thenReturn("Group-Description");

        when(service.getPlaceRepresentations(isA(int[].class), anyString()))
            .thenReturn(new PlaceRepresentationDTO[] { placeRepDTO, parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { gparPlaceRepDTO });
        when(service.getTypeById(eq(TypeCategory.PLACE), anyInt())).thenReturn(mock(TypeDTO.class));
        when(service.getPlaceById(157, null)).thenReturn(ownerMock);
        when(service.getGroupById(GroupType.PLACE_TYPE, 166)).thenReturn(typeGroupMock);
        
        metadata = new Interpretation( service, placeRepDTO, 0, inputText );
        metadata.getScorecard().recordScore( mock(Scorer.class), 66 );
        metadata.getScorecard().assignRelevanceScore( 77 );
        
        PlaceRepresentation placeRep = new PlaceRepresentation(service, placeRepDTO, metadata);

        PlaceSearchResultModel searchResultModel =
                mapper.createSearchResultFromPlaceRepresentation(placeRep, new StdLocale("en"), "http://localhost/blah/", false);

        assertNotNull(searchResultModel);
        assertNotNull(searchResultModel.getRawScore());
        assertNotNull(searchResultModel.getRelevanceScore());
        assertNotNull(searchResultModel.getTokenMatches());
        assertEquals(searchResultModel.getRawScore(), Integer.valueOf(66));
        assertEquals(searchResultModel.getRelevanceScore(), Integer.valueOf(77));
        assertEquals(searchResultModel.getTokenMatches().size(),1);
        assertNotNull(searchResultModel.getTokenMatches().get( 0 ));
        assertEquals(searchResultModel.getTokenMatches().get( 0 ).getId(),Integer.valueOf( 111 ));
        assertEquals(searchResultModel.getTokenMatches().get( 0 ).getToken(),"token");
    }

    @Test(groups = {"unit"})
    public void createSearchResultFromPlaceRepresentationSpatialMetaData() throws PlaceDataException {
        // Grand-Parent information
        PlaceRepresentationDTO gparPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> gparNamesMap = new HashMap<String,String>();
        gparNamesMap.put("en", "Name-EN");
        gparNamesMap.put("fr", "Name-FR");

        when(gparPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 1 });
        when(gparPlaceRepDTO.getDisplayNames()).thenReturn(gparNamesMap);

        // Parent information
        PlaceRepresentationDTO parPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> parNamesMap = new HashMap<String,String>();
        parNamesMap.put("en", "Name-EN");
        parNamesMap.put("fr", "Name-FR");

        when(parPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 11, 1 });

        // Leaf information
        PlaceRepresentationDTO placeRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> namesMap = new HashMap<String,String>();
        namesMap.put("en", "Name-EN");
        namesMap.put("fr", "Name-FR");

        // Metadata
        SpatialResultMetadata metadata = new SpatialResultMetadata(placeRepDTO, 66, 77, 88.0);

        // Other mocks
        PlaceDTO ownerMock = mock(PlaceDTO.class);
        GroupDTO typeGroupMock = mock(GroupDTO.class);

        when(placeRepDTO.getJurisdictionChain()).thenReturn(new int[] { 111, 11, 1 });
        when(placeRepDTO.getDisplayNames()).thenReturn(namesMap);
        when(placeRepDTO.getUUID()).thenReturn("abcd-1234");
        when(placeRepDTO.getRevision()).thenReturn(55);
        when(placeRepDTO.getType()).thenReturn(17);
        when(placeRepDTO.getFromYear()).thenReturn(1900);
        when(placeRepDTO.getToYear()).thenReturn(2000);
        when(placeRepDTO.getOwnerId()).thenReturn(157);
        when(placeRepDTO.getPreferredLocale()).thenReturn("en");
        when(placeRepDTO.isPublished()).thenReturn(true);
        when(placeRepDTO.isValidated()).thenReturn(true);
        when(placeRepDTO.getLatitude()).thenReturn(55.5);
        when(placeRepDTO.getLongitude()).thenReturn(-111.1);
        when(placeRepDTO.getTypeGroup()).thenReturn(166);

        when(ownerMock.getId()).thenReturn(157);
        when(typeGroupMock.getId()).thenReturn(166);
        when(typeGroupMock.getName("en")).thenReturn("Group-Name");
        when(typeGroupMock.getDescription("en")).thenReturn("Group-Description");

        when(service.getPlaceRepresentations(isA(int[].class), anyString()))
            .thenReturn(new PlaceRepresentationDTO[] { placeRepDTO, parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { gparPlaceRepDTO });
        when(service.getTypeById(eq(TypeCategory.PLACE), anyInt())).thenReturn(mock(TypeDTO.class));
        when(service.getPlaceById(157, null)).thenReturn(ownerMock);
        when(service.getGroupById(GroupType.PLACE_TYPE, 166)).thenReturn(typeGroupMock);

        PlaceRepresentation placeRep = new PlaceRepresentation(service, placeRepDTO, metadata);

        PlaceSearchResultModel searchResultModel =
                mapper.createSearchResultFromPlaceRepresentation(placeRep, new StdLocale("en"), "http://localhost/blah/", false);

        assertNotNull(searchResultModel);
        assertNotNull(searchResultModel.getDistanceInKM());
        assertNotNull(searchResultModel.getDistanceInMiles());
        assertEquals(searchResultModel.getDistanceInKM(), 88, 0.001);
        assertEquals(searchResultModel.getDistanceInMiles(), 54.6808, 0.001);
    }

    @Test(groups = {"unit"})
    public void createSearchResultFromPlaceRepresentationNullStuff() throws PlaceDataException {
        // Grand-Parent information
        PlaceRepresentationDTO gparPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> gparNamesMap = new HashMap<String,String>();
        gparNamesMap.put("en", "Name-EN");
        gparNamesMap.put("fr", "Name-FR");

        when(gparPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 1 });
        when(gparPlaceRepDTO.getDisplayNames()).thenReturn(gparNamesMap);

        // Parent information
        PlaceRepresentationDTO parPlaceRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> parNamesMap = new HashMap<String,String>();
        parNamesMap.put("en", "Name-EN");
        parNamesMap.put("fr", "Name-FR");

        when(parPlaceRepDTO.getJurisdictionChain()).thenReturn(new int[] { 11, 1 });

        // Leaf information
        PlaceRepresentationDTO placeRepDTO = mock(PlaceRepresentationDTO.class);

        Map<String,String> namesMap = new HashMap<String,String>();
        namesMap.put("en", "Name-EN");
        namesMap.put("fr", "Name-FR");

        // Other mocks
        PlaceDTO ownerMock = mock(PlaceDTO.class);

        when(placeRepDTO.getJurisdictionChain()).thenReturn(new int[] { 111, 11, 1 });
        when(placeRepDTO.getDisplayNames()).thenReturn(namesMap);
        when(placeRepDTO.getUUID()).thenReturn("abcd-1234");
        when(placeRepDTO.getRevision()).thenReturn(55);
        when(placeRepDTO.getType()).thenReturn(17);
        when(placeRepDTO.getFromYear()).thenReturn(Integer.MIN_VALUE);
        when(placeRepDTO.getToYear()).thenReturn(Integer.MAX_VALUE);
        when(placeRepDTO.getOwnerId()).thenReturn(157);
        when(placeRepDTO.getPreferredLocale()).thenReturn("en");
        when(placeRepDTO.isPublished()).thenReturn(true);
        when(placeRepDTO.isValidated()).thenReturn(true);
        when(placeRepDTO.getLatitude()).thenReturn(null);
        when(placeRepDTO.getLongitude()).thenReturn(null);
        when(placeRepDTO.getTypeGroup()).thenReturn(null);

        when(ownerMock.getId()).thenReturn(157);

        when(service.getPlaceRepresentations(isA(int[].class), anyString()))
            .thenReturn(new PlaceRepresentationDTO[] { placeRepDTO, parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { parPlaceRepDTO, gparPlaceRepDTO })
            .thenReturn(new PlaceRepresentationDTO[] { gparPlaceRepDTO });
        when(service.getTypeById(eq(TypeCategory.PLACE), anyInt())).thenReturn(mock(TypeDTO.class));
        when(service.getPlaceById(157, null)).thenReturn(ownerMock);
        when(service.getGroupById(eq(GroupType.PLACE_TYPE), anyInt())).thenReturn(null);

        PlaceRepresentation placeRep = new PlaceRepresentation(service, placeRepDTO);

        PlaceSearchResultModel searchResultModel =
                mapper.createSearchResultFromPlaceRepresentation(placeRep, new StdLocale("en"), "http://localhost/blah/", false);

        assertNotNull(searchResultModel);
        assertNotNull(searchResultModel.getRep());
        assertEquals(searchResultModel.getRep().getId(), Integer.valueOf(111));
        assertEquals(searchResultModel.getRep().getDisplayName().getLocale(), "en");
        assertEquals(searchResultModel.getRep().getDisplayName().getName(), "Name-EN");
        assertNull(searchResultModel.getRep().getFromYear());
        assertNull(searchResultModel.getRep().getToYear());
        assertEquals(searchResultModel.getRep().getFullDisplayName().getLocale(), "en");
        assertNull(searchResultModel.getRep().getGroup());
        assertEquals(searchResultModel.getRep().getJurisdiction().getId(), 11);
        assertEquals(searchResultModel.getRep().getLinks().size(), 2);
        assertEquals(searchResultModel.getRep().getLinks().get(0).getHref(), "http://localhost/blah/reps/111");
        assertEquals(searchResultModel.getRep().getLinks().get(1).getHref(), "http://localhost/blah/157");
        assertNull(searchResultModel.getRep().getLocation());
        assertEquals(searchResultModel.getRep().getPreferredLocale(), "en");
        assertEquals(searchResultModel.getRep().getRevision(), Integer.valueOf(55));
        assertEquals(searchResultModel.getRep().getType().getId(), Integer.valueOf(17));
        assertEquals(searchResultModel.getRep().getUUID(), "abcd-1234");
        assertTrue(searchResultModel.getRep().isPublished());
        assertTrue(searchResultModel.getRep().isValidated());
    }
}
