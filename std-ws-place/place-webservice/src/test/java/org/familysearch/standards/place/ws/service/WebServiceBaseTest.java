package org.familysearch.standards.place.ws.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import javax.ws.rs.core.UriInfo;

import org.familysearch.standards.core.Localized;
import org.familysearch.standards.core.LocalizedData;
import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.AttributeType;
import org.familysearch.standards.place.CitationType;
import org.familysearch.standards.place.ExtXrefType;
import org.familysearch.standards.place.GenericType;
import org.familysearch.standards.place.Place;
import org.familysearch.standards.place.PlaceName;
import org.familysearch.standards.place.PlaceNameType;
import org.familysearch.standards.place.PlaceRepresentation;
import org.familysearch.standards.place.PlaceRequestBuilder;
import org.familysearch.standards.place.PlaceService;
import org.familysearch.standards.place.PlaceType;
import org.familysearch.standards.place.Group;
import org.familysearch.standards.place.ResolutionType;
import org.familysearch.standards.place.data.AttributeDTO;
import org.familysearch.standards.place.data.CitationDTO;
import org.familysearch.standards.place.data.GroupType;
import org.familysearch.standards.place.data.PlaceDTO;
import org.familysearch.standards.place.data.PlaceNameDTO;
import org.familysearch.standards.place.data.PlaceRepresentationDTO;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.SourceDTO;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.data.WritablePlaceDataService;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.mapping.GroupMapper;
import org.familysearch.standards.place.ws.model.CentroidModel;
import org.familysearch.standards.place.ws.model.JurisdictionModel;
import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.model.LocalizedNameDescModel;
import org.familysearch.standards.place.ws.model.LocationModel;
import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.model.PlaceModel;
import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;
import org.familysearch.standards.place.ws.model.PlaceTypeGroupModel;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.familysearch.standards.place.ws.model.VariantModel;


public abstract class WebServiceBaseTest {

    /** Generate some random numbers */
    protected Random random = new Random();
    
    /** Mock objects for testing purposes ... */
    protected WritablePlaceDataService  writableServiceMock;
    protected ReadablePlaceDataService  readableServiceMock;
    protected SolrDataService           solrServiceMock;
    protected DbDataService             dbServiceMock;
    protected PlaceService              placeServiceMock;
    protected String                    wsVersion;
    protected UriInfo                   uriInfo;
    protected PlaceRequestBuilder       prBuilder;

    /** The locales we'll use for testing */
    protected StdLocale[] testLocales = { StdLocale.ENGLISH, StdLocale.FRENCH, StdLocale.GERMAN };

    //
    // ------------------------------------------------------------------------------------------------
    // Internal methods to create mocks in three varieties:
    //    -- PlaceRepresentation
    //    -- PlaceRepresentationDTO
    //    -- PlaceRepresentationModel
    //
    // Note: methods exist not only for PlaceRepresentation, but for Place, Name, Type, etc.
    // ------------------------------------------------------------------------------------------------
    //

    /**
     * Create a "reasonable" Place mock
     * @return a Place mock
     */
    protected Place mockPlace(PlaceRepresentation placeRep) {
        int id = random.nextInt(1000000);
        Place place = mock(Place.class);

        SortedSet<PlaceName> names = new TreeSet<PlaceName>();
        for (Localized<String> lData : this.getLocalizedData(id, "PlaceName")) {
            PlaceNameType nameType = this.mockPlaceNameType();
            int idx = random.nextInt(1000000);
            PlaceName placeName = mock(PlaceName.class);
            when(placeName.getId()).thenReturn(idx);
            when(placeName.getName()).thenReturn(lData);
            when(placeName.getNormalizedName()).thenReturn(lData.get());
            when(placeName.getType()).thenReturn(nameType);
        }

        when(place.getId()).thenReturn(id);
        when(place.getFromYear()).thenReturn(1900);
        when(place.getToYear()).thenReturn(2013);
        when(place.getNames()).thenReturn(names);
        if (placeRep == null) {
            PlaceRepresentation placeRepX = this.mockPlaceRepBasic();
            when(place.getPreferredRepresentation()).thenReturn(placeRepX);
        } else {
            when(place.getPreferredRepresentation()).thenReturn(placeRep);
        }
        when(place.getRevision()).thenReturn(42);

        return place;
    }

    /**
     * Create a "reasonable" PlaceRepresentation mock with three parents and four children.
     * @return a PlaceRepresentation mock
     */
    protected PlaceRepresentation mockPlaceRep() {
        return this.mockPlaceRep(3, 4);
    }

    /**
     * Create a "reasonable" PlaceRepresentation mock with the given number of parents
     * and children.  Each of 
     * @return a PlaceRepresentation mock
     */
    protected PlaceRepresentation mockPlaceRep(int parCnt, int childCnt) {
        int id = random.nextInt(1000000);
        PlaceRepresentation placeRep = this.mockPlaceRepBasic();

        int[] jurisChain = new int[parCnt+1];
        PlaceRepresentation[] parents = new PlaceRepresentation[parCnt+1];
        for (int i=1;  i<parents.length;  i++) {
            parents[i] = this.mockPlaceRepBasic();
            jurisChain[i] = parents[i].getId();
        }
        jurisChain[0] = id;
        parents[0] = placeRep;
        
        PlaceRepresentation[] children = new PlaceRepresentation[childCnt];
        for (int i=0;  i<children.length;  i++) {
            children[i] = this.mockPlaceRepBasic();
        }

        Place place = this.mockPlace(placeRep);
        when(placeRep.getChildren()).thenReturn(children);
        when(placeRep.getJurisdictionChain()).thenReturn(parents);
        when(placeRep.getJurisdictionChainIds()).thenReturn(jurisChain);
        when(placeRep.getPlace()).thenReturn(place);
        when(placeRep.getPlaceId()).thenReturn(11);

        return placeRep;
    }
    /**
     * Create a "reasonable" PlaceRepresentation mock with the given number of parents
     * and children.  Each of 
     * @return a PlaceRepresentation mock
     */
    protected PlaceRepresentation mockPlaceRepBasic() {
        int id = random.nextInt(1000000);
        PlaceRepresentation placeRep = mock(PlaceRepresentation.class);

        List<Localized<String>> names = new ArrayList<Localized<String>>();
        names.addAll(this.getLocalizedData(id, "Name"));

        PlaceType placeType = this.mockPlaceType();
        Group placeTypeGroup = this.mockGroup();

        when(placeRep.getId()).thenReturn(id);
        when(placeRep.getCentroidLatitude()).thenReturn(44.4);
        when(placeRep.getCentroidLongitude()).thenReturn(-55.5);
        for (Localized<String> lData : names) {
            when(placeRep.getDisplayName(lData.getLocale())).thenReturn(lData);
            when(placeRep.getFullDisplayName(lData.getLocale())).thenReturn(lData);
        }
        when(placeRep.getDisplayNames()).thenReturn(names);
        when(placeRep.getFullPreferredDisplayName()).thenReturn(names.get(0));
        when(placeRep.getJurisdictionFromYear()).thenReturn(1900);
        when(placeRep.getJurisdictionToYear()).thenReturn(2000);
        when(placeRep.getPreferredDisplayName()).thenReturn(names.get(0));
        when(placeRep.getPreferredLocale()).thenReturn(names.get(0).getLocale());
        when(placeRep.getRevision()).thenReturn(42);
        when(placeRep.getType()).thenReturn(placeType);
        when(placeRep.getTypeGroup()).thenReturn(placeTypeGroup);
        when(placeRep.getUUID()).thenReturn(String.valueOf(UUID.randomUUID()));

        // May be filled in later
        when(placeRep.getChildren()).thenReturn(new PlaceRepresentation[] { });
        when(placeRep.getJurisdictionChain()).thenReturn(new PlaceRepresentation[] { mock(PlaceRepresentation.class) });
        when(placeRep.getJurisdictionChainIds()).thenReturn(new int[] { id });
        when(placeRep.getPlace()).thenReturn(mock(Place.class));
        when(placeRep.getPlaceId()).thenReturn(11);

        return placeRep;
    }

    /**
     * Create a "reasonable" Group mock.
     * @return a Group instance
     */
    protected Group mockGroup() {
        Group placeTypeGroup = mock(Group.class);

        Set<Integer> memberIds = new HashSet<Integer>();
        memberIds.add(11);
        memberIds.add(13);
        memberIds.add(17);

        int id = random.nextInt(1000000);
        when(placeTypeGroup.getId()).thenReturn(id);
        when(placeTypeGroup.getName("en")).thenReturn("Name." + id);
        when(placeTypeGroup.getDescription("en")).thenReturn("Description." + id);
        when(placeTypeGroup.getSubGroups()).thenReturn(new HashSet<Group>());
        when(placeTypeGroup.getMemberIds()).thenReturn(memberIds);
        when(placeTypeGroup.getAllMemberIds()).thenReturn(memberIds);
        when(placeTypeGroup.isPublished()).thenReturn(true);

        return placeTypeGroup;
    }

    /**
     * Create a "reasonable" PlaceType mock.
     * @return a PlaceType instance
     */
    protected PlaceType mockPlaceType() {
        PlaceType placeType = mock(PlaceType.class);

        int id = random.nextInt(1000000);
        when(placeType.getId()).thenReturn(id);
        when(placeType.getCode()).thenReturn("Code." + id);
        when(placeType.isPublished()).thenReturn(true);
        for (LocalizedData<String> lData : this.getLocalizedData(id, "TypeName")) {
            when(placeType.getName(lData.getLocale())).thenReturn(lData);
        }
        for (LocalizedData<String> lData : this.getLocalizedData(id, "Description")) {
            when(placeType.getDescription(lData.getLocale())).thenReturn(lData);
        }

        return placeType;
    }

    /**
     * Create a "reasonable" PlaceNameType mock.
     * @return a PlaceType instance
     */
    protected PlaceNameType mockPlaceNameType() {
        PlaceNameType placeNameType = mock(PlaceNameType.class);

        int id = random.nextInt(1000000);
        when(placeNameType.getId()).thenReturn(id);
        when(placeNameType.getCode()).thenReturn("Code." + id);
        when(placeNameType.isPublished()).thenReturn(true);
        for (LocalizedData<String> lData : this.getLocalizedData(id, "TypeName")) {
            when(placeNameType.getName(lData.getLocale())).thenReturn(lData);
        }
        for (LocalizedData<String> lData : this.getLocalizedData(id, "Description")) {
            when(placeNameType.getDescription(lData.getLocale())).thenReturn(lData);
        }

        return placeNameType;
    }

    /**
     * Create a "reasonable" AttribyteType mock.
     * @return a AttributeType instance
     */
    protected AttributeType mockAttributeType() {
        AttributeType attributeType = mock(AttributeType.class);

        int id = random.nextInt(1000000);
        when(attributeType.getId()).thenReturn(id);
        when(attributeType.getCode()).thenReturn("Code." + id);
        when(attributeType.isPublished()).thenReturn(true);
        for (LocalizedData<String> lData : this.getLocalizedData(id, "TypeName")) {
            when(attributeType.getName(lData.getLocale())).thenReturn(lData);
        }
        for (LocalizedData<String> lData : this.getLocalizedData(id, "Description")) {
            when(attributeType.getDescription(lData.getLocale())).thenReturn(lData);
        }

        return attributeType;
    }
    
    /**
     * Create a "reasonable" AttribyteType mock.
     * @return a CitationType instance
     */
    protected CitationType mockCitationType() {
        CitationType citationType = mock(CitationType.class);

        int id = random.nextInt(1000000);
        when(citationType.getId()).thenReturn(id);
        when(citationType.getCode()).thenReturn("Code." + id);
        when(citationType.isPublished()).thenReturn(true);
        for (LocalizedData<String> lData : this.getLocalizedData(id, "TypeName")) {
            when(citationType.getName(lData.getLocale())).thenReturn(lData);
        }
        for (LocalizedData<String> lData : this.getLocalizedData(id, "Description")) {
            when(citationType.getDescription(lData.getLocale())).thenReturn(lData);
        }

        return citationType;
    }

    /**
     * Create a "reasonable" AttribyteType mock.
     * @return a ResolutionType instance
     */
    protected ResolutionType mockResolutionType() {
        ResolutionType resolutionType = mock(ResolutionType.class);

        int id = random.nextInt(1000000);
        when(resolutionType.getId()).thenReturn(id);
        when(resolutionType.getCode()).thenReturn("Code." + id);
        when(resolutionType.isPublished()).thenReturn(true);
        for (LocalizedData<String> lData : this.getLocalizedData(id, "TypeName")) {
            when(resolutionType.getName(lData.getLocale())).thenReturn(lData);
        }
        for (LocalizedData<String> lData : this.getLocalizedData(id, "Description")) {
            when(resolutionType.getDescription(lData.getLocale())).thenReturn(lData);
        }

        return resolutionType;
    }

    /**
     * Create a "reasonable" AttribyteType mock.
     * @return a ExtXrefType instance
     */
    protected ExtXrefType mockExtXrefType() {
        ExtXrefType extXrefType = mock(ExtXrefType.class);

        int id = random.nextInt(1000000);
        when(extXrefType.getId()).thenReturn(id);
        when(extXrefType.getCode()).thenReturn("Code." + id);
        when(extXrefType.isPublished()).thenReturn(true);
        for (LocalizedData<String> lData : this.getLocalizedData(id, "TypeName")) {
            when(extXrefType.getName(lData.getLocale())).thenReturn(lData);
        }
        for (LocalizedData<String> lData : this.getLocalizedData(id, "Description")) {
            when(extXrefType.getDescription(lData.getLocale())).thenReturn(lData);
        }

        return extXrefType;
    }

    /**
     * Generate three localized-data instances, one each for ENGLISH, FRENCH and GERMAN.
     * @param id identifier to introduce some degree of "randomness"
     * @param prefix prefix for the text value
     * @return Set of localized data instances
     */
    protected List<LocalizedData<String>> getLocalizedData(int id, String prefix) {
        List<LocalizedData<String>> results = new ArrayList<LocalizedData<String>>();

        results.add(new LocalizedData<String>(prefix + ".EN." + id, StdLocale.ENGLISH));
        results.add(new LocalizedData<String>(prefix + ".FR." + id, StdLocale.FRENCH));
        results.add(new LocalizedData<String>(prefix + ".DE." + id, StdLocale.GERMAN));

        return results;
    }

    /**
     * Convert a "Place" thing into a "PlaceDTO" thing
     * 
     * @param place Place thing
     * @return PlaceDTO thing
     */
    protected PlaceDTO createDTO(Place place) {
        List<PlaceNameDTO> names = new ArrayList<PlaceNameDTO>();
        for (PlaceName plName : place.getNames()) {
            names.add(this.createDTO(plName));
        }

        PlaceDTO dto = new PlaceDTO(
            place.getId(),
            names,
            place.getFromYear(),
            place.getToYear(),
            place.getRevision(),
            null);

        return dto;
    }

    /**
     * Convert a "PlaceRepresentation" thing into a "PlaceRepresentationDTO" thing
     * 
     * @param placeRep PlaceRepresentation thing
     * @return PlaceRepresentationDTO thing
     */
    protected PlaceRepresentationDTO createDTO(PlaceRepresentation placeRep) {
        Map<String,String> displayNames = new HashMap<String,String>();
        for (Localized<String> name : placeRep.getDisplayNames()) {
            displayNames.put(name.getLocale().toString(), name.get());
        }

        PlaceRepresentationDTO dto = new PlaceRepresentationDTO(
            placeRep.getJurisdictionChainIds(),
            placeRep.getPlaceId(),
            placeRep.getJurisdictionFromYear(),
            placeRep.getJurisdictionToYear(),
            placeRep.getType().getId(),
            String.valueOf(placeRep.getPreferredLocale()),
            displayNames,
            placeRep.getCentroidLatitude(),
            placeRep.getCentroidLongitude(),
            placeRep.isPublished(),
            placeRep.isValidated(),
            placeRep.getRevision(),
            placeRep.getUUID(),
            placeRep.getTypeGroup().getId(),
            null);

        return dto;
    }

    /**
     * Convert a "PlaceName" thing into a "PlaceNameDTO" thing
     * 
     * @param placeName PlaceName thing
     * @return PlaceNameDTO thing
     */
    protected PlaceNameDTO createDTO(PlaceName placeName) {
        PlaceNameDTO dto = new PlaceNameDTO(
            placeName.getId(),
            placeName.getName(),
            placeName.getType().getId());

        return dto;
    }

    /**
     * Convert a "PlaceType" thing into a "TypeDTO" thing
     * 
     * @param placeType PlaceType thing
     * @return TypeDTO thing
     */
    protected TypeDTO createDTO(PlaceType placeType) {
        Map<String,String> names = new HashMap<String,String>();
        Map<String,String> descr = new HashMap<String,String>();
        for (StdLocale locale : testLocales) {
            Localized<String> name = placeType.getName(locale);
            if (name != null) names.put(name.getLocale().toString(), name.get());
            Localized<String> desc = placeType.getDescription(locale);
            if (name != null) descr.put(desc.getLocale().toString(), desc.get());
        }

        TypeDTO dto = new TypeDTO(
            placeType.getId(),
            placeType.getCode(),
            names,
            descr,
            placeType.isPublished() );

        return dto;
    }

    /**
     * Convert a "PlaceNameType" thing into a "TypeDTO" thing
     * 
     * @param nameType PlaceNameType thing
     * @return TypeDTO thing
     */
    protected TypeDTO createDTO(PlaceNameType nameType) {
        Map<String,String> names = new HashMap<String,String>();
        Map<String,String> descr = new HashMap<String,String>();
        for (StdLocale locale : testLocales) {
            Localized<String> name = nameType.getName(locale);
            if (name != null) names.put(name.getLocale().toString(), name.get());
            Localized<String> desc = nameType.getDescription(locale);
            if (name != null) descr.put(desc.getLocale().toString(), desc.get());
        }

        TypeDTO dto = new TypeDTO(
            nameType.getId(),
            nameType.getCode(),
            names,
            descr,
            nameType.isPublished() );

        return dto;
    }

    /**
     * Convert a "AttributeType" thing into a "TypeDTO" thing
     * 
     * @param attributeType AttributeType thing
     * @return TypeDTO thing
     */
    protected TypeDTO createDTO(AttributeType attributeType) {
        Map<String,String> names = new HashMap<String,String>();
        Map<String,String> descr = new HashMap<String,String>();
        for (StdLocale locale : testLocales) {
            Localized<String> name = attributeType.getName(locale);
            if (name != null) names.put(name.getLocale().toString(), name.get());
            Localized<String> desc = attributeType.getDescription(locale);
            if (name != null) descr.put(desc.getLocale().toString(), desc.get());
        }

        TypeDTO dto = new TypeDTO(
            attributeType.getId(),
            attributeType.getCode(),
            names,
            descr,
            attributeType.isPublished() );

        return dto;
    }

    /**
     * Convert a "CitationType" thing into a "TypeDTO" thing
     * 
     * @param citationType CitationType thing
     * @return TypeDTO thing
     */
    protected TypeDTO createDTO(CitationType citationType) {
        Map<String,String> names = new HashMap<String,String>();
        Map<String,String> descr = new HashMap<String,String>();
        for (StdLocale locale : testLocales) {
            Localized<String> name = citationType.getName(locale);
            if (name != null) names.put(name.getLocale().toString(), name.get());
            Localized<String> desc = citationType.getDescription(locale);
            if (name != null) descr.put(desc.getLocale().toString(), desc.get());
        }

        TypeDTO dto = new TypeDTO(
            citationType.getId(),
            citationType.getCode(),
            names,
            descr,
            citationType.isPublished() );

        return dto;
    }

    /**
     * Convert a "ResolutionType" thing into a "TypeDTO" thing
     * 
     * @param resolutionType ResolutionType thing
     * @return TypeDTO thing
     */
    protected TypeDTO createDTO(ResolutionType resolutionType) {
        Map<String,String> names = new HashMap<String,String>();
        Map<String,String> descr = new HashMap<String,String>();
        for (StdLocale locale : testLocales) {
            Localized<String> name = resolutionType.getName(locale);
            if (name != null) names.put(name.getLocale().toString(), name.get());
            Localized<String> desc = resolutionType.getDescription(locale);
            if (name != null) descr.put(desc.getLocale().toString(), desc.get());
        }

        TypeDTO dto = new TypeDTO(
            resolutionType.getId(),
            resolutionType.getCode(),
            names,
            descr,
            resolutionType.isPublished() );

        return dto;
    }

    /**
     * Convert a "ExtXrefType" thing into a "TypeDTO" thing
     * 
     * @param extXrefType ExtXrefType thing
     * @return TypeDTO thing
     */
    protected TypeDTO createDTO(ExtXrefType extXrefType) {
        Map<String,String> names = new HashMap<String,String>();
        Map<String,String> descr = new HashMap<String,String>();
        for (StdLocale locale : testLocales) {
            Localized<String> name = extXrefType.getName(locale);
            if (name != null) names.put(name.getLocale().toString(), name.get());
            Localized<String> desc = extXrefType.getDescription(locale);
            if (name != null) descr.put(desc.getLocale().toString(), desc.get());
        }

        TypeDTO dto = new TypeDTO(
            extXrefType.getId(),
            extXrefType.getCode(),
            names,
            descr,
            extXrefType.isPublished() );

        return dto;
    }

    /**
     * Convert a "Group" thing into a "GroupDTO" thing
     * 
     * @param placeTypeGrup Group thing
     * @return GroupDTO thing
     */
    protected GroupDTO createDTO(Group placeTypeGroup) {
        Map<String,String> names = new HashMap<>();
        names.put("en", placeTypeGroup.getName("en"));

        Map<String,String> descr = new HashMap<>();
        descr.put("en", placeTypeGroup.getDescription("en"));

        GroupDTO dto = new GroupDTO(
            placeTypeGroup.getId(),
            GroupType.PLACE_TYPE,
            names,
            descr,
            placeTypeGroup.isPublished(),
            placeTypeGroup.getAllMemberIds());

        return dto;
    }

    /**
     * Create a "SourceDTO"
     * 
     * @return SourceDTO thing
     */
    protected SourceDTO createSourceDTO() {
        int id = random.nextInt(1000000);
        return new SourceDTO(id, "title", "description", true);
    }

    /**
     * Create an "AttributeDTO"
     * 
     * @return AttributeDTO thing
     */
    protected AttributeDTO createAttributeDTO() {
        int id = random.nextInt(1000000);
        return new AttributeDTO(id, 1234, 1234, 2010, "value." + id, 10);
    }

    /**
     * Create an "CitationDTO"
     * 
     * @return CitationDTO thing
     */
    protected CitationDTO createCitationDTO() {
        int id = random.nextInt(1000000);
        return new CitationDTO(id, 111, 222, 333, null, "description", "source-ref", 10);
    }

    /**
     * Convert a "Place" thing into a "PlaceModel" thing
     * 
     * @param place Place thing
     * @return PlaceModel thing
     */
    protected PlaceModel createModel(Place place) {
        List<PlaceRepresentationModel> placeReps = new ArrayList<PlaceRepresentationModel>();

        List<VariantModel> variants = new ArrayList<VariantModel>();
        for (PlaceName plName : place.getNames()) {
            NameModel name = this.createName(plName.getName());
            TypeModel type = this.createType(plName.getType());
            
            VariantModel variant = new VariantModel();
            variant.setId(plName.getId());
            variant.setName(name);
            variant.setType(type);
        }

        PlaceModel model = new PlaceModel();
        model.setId(place.getId());
        model.setFromYear(place.getFromYear());
        model.setToYear(place.getToYear());
        model.setReps(placeReps);
        model.setVariants(variants);

        return model;
    }

    /**
     * Convert a "PlaceRepresentation" thing into a "PlaceRepresentationModel" thing
     * 
     * @param place PlaceRepresentation thing
     * @return PlaceRepresentationModel thing
     */
    protected PlaceRepresentationModel createModel(PlaceRepresentation place) {
    JurisdictionModel jurisdiction = new JurisdictionModel();
        TypeModel type = this.createType(place.getType());
        PlaceTypeGroupModel typeGroup = this.createTypeGroup(place.getTypeGroup());
        LocationModel location = this.createLocation(place.getCentroidLatitude(), place.getCentroidLongitude());
        List<LinkModel> links = new ArrayList<LinkModel>();
        NameModel displayName = this.createName(place.getPreferredDisplayName());
        NameModel fullName = this.createName(place.getFullPreferredDisplayName());
        List<NameModel> displayNames = new ArrayList<NameModel>();
        for (Localized<String> name : place.getDisplayNames()) {
            displayNames.add(this.createName(name));
        }
        List<PlaceRepresentationModel> children = new ArrayList<PlaceRepresentationModel>();

        PlaceRepresentationModel model = new PlaceRepresentationModel();
        model.setId(place.getId());
        model.setJurisdiction(jurisdiction);
        model.setType(type);
        model.setOwnerId(place.getPlaceId());
        model.setGroup(typeGroup);
        model.setFromYear(place.getJurisdictionFromYear());
        model.setToYear(place.getJurisdictionToYear());
        model.setPreferredLocale(String.valueOf(place.getPreferredLocale()));
        model.setPublished(place.isPublished());
        model.setValidated(place.isValidated());
        model.setLocation(location);
        model.setUUID(place.getUUID());
        model.setRevision(place.getRevision());
        model.setLinks(links);
        model.setDisplayName(displayName);
        model.setFullDisplayName(fullName);
        model.setDisplayNames(displayNames);
        model.setChildren(children);
 
        return model;
    }

    /**
     * Convert a "Group" thing into a "TypeGroupModel" thing
     * 
     * @param place Group thing
     * @return TypeGroupModel thing
     */
    protected PlaceTypeGroupModel createTypeGroup(Group typeGroup) {
        GroupMapper groupMapper = new GroupMapper(readableServiceMock);
        return groupMapper.createModelFromTypeGroup(typeGroup, "dummy");
    }

    /**
     * Convert a "GenericType" thing -- place, name, attribute, citation -- into a
     * "TypeModel" thing.
     * 
     * @param place PlaceNameType thing
     * @return TypeModel thing
     */
    protected TypeModel createType(GenericType anyType) {
        List<LocalizedNameDescModel> nameDescs = new ArrayList<LocalizedNameDescModel>();
        for (StdLocale locale : testLocales) {
            Localized<String> name = anyType.getName(locale);
            Localized<String> desc = anyType.getDescription(locale);
            if (name != null  &&  desc != null) {
                LocalizedNameDescModel nameDesc = new LocalizedNameDescModel();
                nameDesc.setLocale(locale.toString());
                nameDesc.setName(name.get());
                nameDesc.setDescription(desc.get());
                nameDescs.add(nameDesc);
            }
        }

        TypeModel model = new TypeModel();

        model.setId(anyType.getId());
        model.setCode(anyType.getCode());
        model.setIsPublished(anyType.isPublished());
        model.setName(nameDescs);

        return model;
    }

    /**
     * Convert a "Localized" thing into a "NameModel" thing
     * 
     * @param place Localized thing
     * @return NameModel thing
     */
    protected NameModel createName(Localized<String> placeName) {
        NameModel name = new NameModel();

        name.setLocale(String.valueOf(placeName.getLocale()));
        name.setName(placeName.get());

        return name;
    }

    /**
     * Convert latitude and longitude into a "LocationModel" thing
     * 
     * @param latitude
     * @param longitude
     * @return LocationModel thing
     */
    protected LocationModel createLocation(Double latitude, Double longitude) {
        LocationModel location = new LocationModel();

        CentroidModel centroid = new CentroidModel();
        centroid.setLatitude(latitude);
        centroid.setLongitude(longitude);
        location.setCentroid(centroid);

        return location;
    }

}
