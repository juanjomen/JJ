package org.familysearch.standards.place.ws.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.core.logging.Logger;
import org.familysearch.standards.place.Group;
import org.familysearch.standards.place.PlaceRepresentation;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.data.GroupType;
import org.familysearch.standards.place.data.InvalidPlaceDataException;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.PlaceRepresentationDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.CentroidModel;
import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.model.LocalizedNameDescModel;
import org.familysearch.standards.place.ws.model.LocationModel;
import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.model.PlaceRepGroupModel;
import org.familysearch.standards.place.ws.model.PlaceRepSummaryModel;
import org.familysearch.standards.place.ws.model.PlaceTypeGroupModel;
import org.familysearch.standards.place.ws.model.TypeModel;


/**
 * Utility class to map between:
 * <br/>
 * {@link Group} and {@link PlaceTypeGroupModel} types.<br/>
 * 
 * @author dshellman, wjohnson000
 *
 */
public class GroupMapper {
    private static final Logger             logger = new Logger( GroupMapper.class );

    protected static final String           MODULE_NAME = "ws";

    /** URL paths for various entity types */
	public static final String GROUP_TYPE_PATH = "type-groups/";
    public static final String GROUP_PLACE_REP_PATH = "place-rep-groups/";


    /** Data service for doing additional operations for place-type-groups */
	private ReadablePlaceDataService dataService;
	private TypeMapper typeMapper;
	private CommonMapper commonMapper;


	/**
	 * Constructor requires a "ReadablePlaceDataService" for some of the place-type-group
	 * operations.
	 * 
	 * @param theService a "ReadablePlaceDataService"
	 */
	public GroupMapper(ReadablePlaceDataService theService) {
		dataService = theService;
		this.typeMapper  = new TypeMapper(theService);
		this.commonMapper = new CommonMapper(theService);
	}

    /**
     * Convert a {@link Group} instance into a {@link PlaceTypeGroupModel} instance,
     * suitable for returning in a web response.
     * <p/>
     * NOTE: The returned type will be a SHALLOW copy of the input.
     * 
     * @param group a {@link Group} instance
     * @param path base URL path
     * @return a {@link PlaceTypeGroupModel} instance
     */
	public PlaceTypeGroupModel createModelFromTypeGroup(Group group, String path) {
		PlaceTypeGroupModel  model = new PlaceTypeGroupModel();
		LinkModel       selfLink = new LinkModel();

		Map<String,String> nameMap = group.getNames();
		Map<String,String> descMap = group.getDescriptions();
		List<LocalizedNameDescModel> nameAndDescr = new ArrayList<>();
		for (Map.Entry<String,String> entry : nameMap.entrySet()) {
		    LocalizedNameDescModel ndModel = new LocalizedNameDescModel();
		    ndModel.setLocale(entry.getKey());
		    ndModel.setName(entry.getValue());
		    ndModel.setDescription(descMap.get(entry.getKey()));
		    nameAndDescr.add(ndModel);
		}

		model.setId(group.getId());
		model.setName(nameAndDescr);
		model.setIsPublished(group.isPublished());

		selfLink.setRel("self");
		selfLink.setHref(path + GROUP_TYPE_PATH + group.getId());

		model.setSelfLink(selfLink);

		return model;
	}

    /**
     * Convert a {@link Group} instance into a {@link PlaceTypeGroupModel} instance,
     * suitable for returning in a web response.
     * <p/>
     * NOTE: The returned type will be a DEEP copy of the input, with details about the
     * sub-groups and types associated with this Group.
     * 
     * @param group a {@link Group} instance
     * @param locale the locale used for names and descriptions of the place types
     * @param path base URL path
     * @return a {@link PlaceTypeGroupModel} instance
     */
	public PlaceTypeGroupModel createModelFromTypeGroupWithTypes(Group group, StdLocale locale, String path) throws PlaceDataException {
		PlaceTypeGroupModel        model;
		List<TypeModel>            types = new ArrayList<>();
		List<PlaceTypeGroupModel>  subGroups = new ArrayList<>();

		model = createModelFromTypeGroup(group, path);

		// Now, add all of the types and shallow copies of the sub-groups
		for (Integer typeId : group.getMemberIds()) {
		    TypeDTO typeDTO = dataService.getTypeById(TypeCategory.PLACE, typeId);
		    TypeModel typeModel = typeMapper.createModelFromTypeDTO(typeDTO, locale, path, TypeCategory.PLACE);
		    types.add(typeModel);
		}
		for (Group theGroup : group.getSubGroups()) {
			subGroups.add(createModelFromTypeGroup(theGroup, path));
		}

		model.setTypes(types);
		model.setSubGroups(subGroups);
        model.setIsPublished(group.isPublished());

		return model;
	}

    /**
     * Convert a {@link PlaceTypeGroupModel} instance into a {@link GroupDTO} instance,
     * suitable for passing into a data service.
     * 
     * @param model a {@link PlaceTypeGroupModel} instance
     * @param copyChildren TRUE if the children are to be included, false otherwise
     * @return a {@link GroupDTO} instance
     */
	public GroupDTO createDTOFromTypeGroup(PlaceTypeGroupModel model, boolean copyChildren) throws PlaceDataException {
	    // Collect the name/description pairs
	    Map<String,String> names = new HashMap<>();
	    Map<String,String> descr = new HashMap<>();
	    if ( model.getLocalizedName() == null ) {
	    	throw new InvalidPlaceDataException( "No name specified for this group.  At least one name must be specifid." );
	    }
	    for (LocalizedNameDescModel nameDesc : model.getLocalizedName()) {
	        names.put(nameDesc.getLocale(), nameDesc.getName());
	        String description = nameDesc.getDescription();
	        if (description != null  &&  description.length() > 0) {
	            descr.put(nameDesc.getLocale(), description);
	        }
	    }

	    // Collect the sub-group instances if so instructed
        Set<GroupDTO> children = new HashSet<GroupDTO>();
	    if (copyChildren && model.getSubGroups() != null) {
	        for (PlaceTypeGroupModel childModel : model.getSubGroups()) {
	            children.add(this.createDTOFromTypeGroup(childModel, false));
	        }
	    }

	    // Collect the member identifiers (place-type identifiers)
	    Set<Integer> memberIds = new HashSet<Integer>();
	    if (model.getTypes() != null) {
	        for (TypeModel type : model.getTypes()) {
	            memberIds.add(type.getId());
	        }
	    }

	    boolean isPublished = model.isPublished() != null  && model.isPublished();
	    return new GroupDTO(model.getId(), GroupType.PLACE_TYPE, names, descr, isPublished, memberIds, children);
	}

    /**
     * Convert a {@link Group} instance into a {@link PlaceRepGroupModel} instance,
     * suitable for returning in a web response.
     * <p/>
     * NOTE: The returned type will be a SHALLOW copy of the input.
     * 
     * @param group a {@link Group} instance
     * @param path base URL path
     * @return a {@link PlaceRepGroupModel} instance
     */
    public PlaceRepGroupModel createModelFromPlaceRepGroup(Group group, String path) {
        PlaceRepGroupModel  model = new PlaceRepGroupModel();
        LinkModel  selfLink = new LinkModel();

        Map<String,String> nameMap = group.getNames();
        Map<String,String> descMap = group.getDescriptions();
        List<LocalizedNameDescModel> nameAndDescr = new ArrayList<>();
        for (Map.Entry<String,String> entry : nameMap.entrySet()) {
            LocalizedNameDescModel ndModel = new LocalizedNameDescModel();
            ndModel.setLocale(entry.getKey());
            ndModel.setName(entry.getValue());
            ndModel.setDescription(descMap.get(entry.getKey()));
            nameAndDescr.add(ndModel);
        }

        selfLink.setRel("self");
        selfLink.setHref(path + GROUP_PLACE_REP_PATH + group.getId());

        model.setId(group.getId());
        model.setName(nameAndDescr);
        model.setSelfLink(selfLink);
        model.setIsPublished(group.isPublished());

        return model;
    }

    /**
     * Convert a {@link Group} instance into a {@link PlaceRepGroupModel} instance,
     * suitable for returning in a web response.
     * <p/>
     * NOTE: The returned type will be a DEEP copy of the input, with details about the
     * sub-groups and types associated with this Group.
     * 
     * @param group a {@link Group} instance
     * @param locale the locale used for names and descriptions of the place types
     * @param path base URL path
     * @return a {@link PlaceRepGroupModel} instance
     */
    public PlaceRepGroupModel createModelFromPlaceRepGroupWithTypes(Group group, StdLocale locale, String path) throws PlaceDataException {
        PlaceRepGroupModel          model;
        List<PlaceRepGroupModel>    subGroups = new ArrayList<>();
        List<PlaceRepSummaryModel>  repSummaries = new ArrayList<>();

        model = createModelFromPlaceRepGroup(group, path);

        // Now, add all of the place-reps and shallow copies of the sub-groups
        for ( Integer repId : group.getMemberIds() ) {
            PlaceRepSummaryModel        repModel = new PlaceRepSummaryModel();
            PlaceRepresentation[]       chain;
            PlaceRepresentationDTO      dto;
            PlaceRepresentation         rep;
            NameModel                   name;
            List<LinkModel>             links = new ArrayList<LinkModel>();
            LinkModel                   selfLink, placeLink, attrLink, citLink;

            dto = dataService.getPlaceRepresentationById( repId, null );
            if ( dto == null ) {
                logger.error( null, MODULE_NAME, "Unknown/invalid place rep found in group during rep group mapping.", "repId", String.valueOf( repId ), "groupId", String.valueOf( group.getId() ) );
                repModel.setId( repId );
                repSummaries.add( repModel );
                continue;
            }
            rep = new PlaceRepresentation( dataService, dto );
            repModel.setId( rep.getId() );
            repModel.setOwnerId( rep.getPlaceId() );

            name = new NameModel();
            name.setName( rep.getDisplayName( locale ).get() );
            name.setLocale( rep.getDisplayName( locale ).getLocale().toString() );
            repModel.setDisplayName( name );

            name = new NameModel();
            name.setName( rep.getFullDisplayName( locale ).get() );
            name.setLocale( rep.getFullDisplayName( locale ).getLocale().toString() );
            repModel.setFullDisplayName( name );

            chain = rep.getJurisdictionChain();
            if ( chain.length > 1 ) {
                repModel.setJurisdiction( commonMapper.createJurisdictionModelFromJurisdictionChain( chain[ 1 ], locale, path ) );
            }

            //Set up the atom links
            selfLink = new LinkModel();
            selfLink.setRel( "self" );
            selfLink.setHref( path + PlaceRepresentationMapper.REPS_PATH + rep.getId() );
            links.add( selfLink );

            placeLink = new LinkModel();
            placeLink.setRel( "related" );
            placeLink.setHref( path + PlaceMapper.PLACE_PATH + rep.getPlaceId() );
            links.add( placeLink );

            attrLink = new LinkModel();
            attrLink.setRel( "attributes" );
            attrLink.setHref( path + PlaceRepresentationMapper.REPS_PATH + rep.getId() + AttributeMapper.ATTRIBUTES_PATH );
            links.add( attrLink );

            citLink = new LinkModel();
            citLink.setRel( "citations" );
            citLink.setHref( path + PlaceRepresentationMapper.REPS_PATH + rep.getId() + CitationMapper.CITATIONS_PATH );
            links.add( citLink );

            repModel.setLinks( links );

            if ( rep.getCentroidLatitude() != null && rep.getCentroidLongitude() != null ) {
                LocationModel                       location = new LocationModel();
                CentroidModel                       centroid = new CentroidModel();

                centroid.setLatitude( rep.getCentroidLatitude() );
                centroid.setLongitude( rep.getCentroidLongitude() );
                location.setCentroid( centroid );

                repModel.setLocation( location );
            }
            repModel.setType( typeMapper.createModelFromType( rep.getType(), locale, path, TypeCategory.PLACE ) );

            repSummaries.add( repModel );
        }
        for (Group theGroup : group.getSubGroups()) {
            subGroups.add(createModelFromPlaceRepGroup(theGroup, path));
        }

        model.setSubGroups(subGroups);
        model.setRepSummaries( repSummaries );
        model.setIsPublished(group.isPublished());

        return model;
    }

    /**
     * Convert a {@link PlaceTypeGroupModel} instance into a {@link GroupDTO} instance,
     * suitable for passing into a data service.
     * 
     * @param model a {@link PlaceTypeGroupModel} instance
     * @param copyChildren TRUE if the children are to be included, false otherwise
     * @return a {@link GroupDTO} instance
     */
    public GroupDTO createDTOFromPlaceRepGroup(PlaceRepGroupModel model, boolean copyChildren) throws PlaceDataException {
        // Collect the name/description pairs
        Map<String,String> names = new HashMap<>();
        Map<String,String> descr = new HashMap<>();
        for (LocalizedNameDescModel nameDesc : model.getLocalizedName()) {
            names.put(nameDesc.getLocale(), nameDesc.getName());
            String description = nameDesc.getDescription();
            if (description != null  &&  description.length() > 0) {
                descr.put(nameDesc.getLocale(), description);
            }
        }

        // Collect the sub-group instances
        Set<GroupDTO> children = new HashSet<GroupDTO>();
        if (copyChildren  &&  model.getSubGroups() != null) {
            for (PlaceRepGroupModel childModel : model.getSubGroups()) {
                children.add(this.createDTOFromPlaceRepGroup(childModel, false));
            }
        }

        // Collect the member identifiers (place-type identifiers)
        Set<Integer> memberIds = new HashSet<Integer>();
        if ( model.getRepSummaries() != null ) {
            for ( PlaceRepSummaryModel repSummary : model.getRepSummaries() ) {
                memberIds.add( repSummary.getId() );
            }
        }

        boolean isPublished = model.isPublished() != null  && model.isPublished();
        return new GroupDTO(model.getId(), GroupType.PLACE_REP, names, descr,isPublished, memberIds, children);
    }
}
