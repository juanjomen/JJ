package org.familysearch.standards.place.ws.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.familysearch.standards.core.Localized;
import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.PlaceRepresentation;
import org.familysearch.standards.place.data.InvalidPlaceDataException;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.PlaceRepresentationDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.ws.model.CentroidModel;
import org.familysearch.standards.place.ws.model.JurisdictionModel;
import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.model.LocationModel;
import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;
import org.familysearch.standards.place.ws.model.PlaceTypeGroupModel;


/**
 * Utility class to map between {@link PlaceRepresentionDTO} and {@link PlaceRepresentationModel} types.
 * 
 * @author dshellman
 *
 */
public class PlaceRepresentationMapper {

    public static final String  REPS_PATH = "reps/";

	private ReadablePlaceDataService   dataService;
	private TypeMapper                 typeMapper;
	private GroupMapper                groupMapper;
	private CommonMapper               commonMapper;

	public PlaceRepresentationMapper( ReadablePlaceDataService theService ) {
		dataService = theService;
		typeMapper = new TypeMapper( dataService );
		groupMapper = new GroupMapper( dataService );
		commonMapper = new CommonMapper( dataService );
	}


    /**
     * Creates an instance of the PlaceRepresentationModel from a PlaceRepresentation.
     * Basically, this method serializes a PlaceRepresentation into XML/JSON.
     * 
     * @param repDto The Place Representation to map.
     * @param locale locale to be reflected in the results
     * @param path URL path for embedded links
     * @param includeChildren TRUE if we're to include the place-rep's children, FALSE otherwise
     * @param pubOnly TRUE if we're to include published children only, FALSE otherwise
     * 
     * @return Returns a fully populated PlaceRepresentationModel instance.
     */
    public PlaceRepresentationModel createModelFromDTO( PlaceRepresentationDTO repDto, StdLocale locale, String path, boolean includeChildren, boolean pubOnly ) throws PlaceDataException {
        PlaceRepresentationModel		model = this.createModelFromPlaceRepresentation( new PlaceRepresentation( dataService, repDto ), locale, path, false );

        // Retrieve children if so desired
        if ( includeChildren ) {
            List<PlaceRepresentationModel>		modelChildren = new ArrayList<PlaceRepresentationModel>();
            List<PlaceRepresentationDTO>		children = dataService.getChildren( repDto.getId(), repDto.getCreatedUsingVersion() );

            for ( PlaceRepresentationDTO childDto : children ) {
                if ( childDto.isPublished()  ||  ! pubOnly ) {
                    modelChildren.add( this.createModelFromDTO( childDto, locale, path, false, true ) );
                }
            }

            Collections.sort( modelChildren, new DisplayNameComparator() );
            model.setChildren( modelChildren );
        }

        return model;
    }

	/**
	 * Creates a PlaceRepresentationDTO from a PlaceRepresentationModel.
	 * 
	 * @param model The model to use.
	 * @param placeRepId The place-rep identifier for an existing place-representation
	 * 
	 * @return Returns an instance of a PlaceRepresentationDTO.
	 */
	public PlaceRepresentationDTO createDTOFromModel( PlaceRepresentationModel model, Integer placeRepId) throws PlaceDataException {
		Map<String,String>				displayNames = new HashMap<String,String>();
		List<NameModel>					nameModelList;
		Integer							groupId = null;
		Double							latitude = null, longitude = null;
		Integer                         fromYear = null, toYear = null;
		int[]							chain, parents;
		int								typeId;
		Integer							id;
		Integer							ownerId;
		Integer							revision;

		//Make sure that the identifiers passed in match properly.
		if ( model.getId() != null && placeRepId != null && !placeRepId.equals( model.getId() ) ) {
			throw new InvalidPlaceDataException(
			    "Place Representation identifiers don't match between data and endpoint.  Endpoint identifier: " + placeRepId + ".  Identifer in data: " + model.getId() );
		}

		nameModelList = model.getDisplayNames();
		if ( nameModelList != null ) {
			for ( NameModel name : nameModelList ) {
			    if ( displayNames.containsKey(name.getLocale()) ) {
		            throw new InvalidPlaceDataException(
		                "Place Representation names '" + name.getName() + "' and '" + displayNames.get(name.getLocale()) + "' both have the same locale [" + name.getLocale() + "]");
			    }
				displayNames.put( name.getLocale(), name.getName() );
			}
		}

		if ( model.getGroup() != null ) {
			groupId = model.getGroup().getId();
		}

		if ( model.getLocation() != null && model.getLocation().getCentroid() != null ) {
			latitude = model.getLocation().getCentroid().getLatitude();
			longitude = model.getLocation().getCentroid().getLongitude();
		}

		//If there's no jurisdiction, then that means there is no parent,
		//so the DTO will report to world.
		if ( model.getJurisdiction() == null ) {
		    if (placeRepId == null) {
	            chain = new int[ 0 ];
		    } else {
	            chain = new int[] { placeRepId };
		    }
		}
		else {
			id = placeRepId;
			parents = commonMapper.createJurisdictionChainFromJurisdictionModel( model.getJurisdiction() );
			//We need to support two different scenarios:
			//First, a valid rep id is passed in.  That needs to be added to the chain.
			//Second, no rep id is passed in.  This happens when we're creating a new
			//place rep, so don't add the id.  If parents exist, add them.
			if ( id == null ) {
				chain = new int[ parents.length ];
				if ( parents.length > 0 ) {
					System.arraycopy( parents, 0, chain, 0, parents.length );
				}
			}
			else {
				chain = new int[ parents.length + 1 ];
				chain[ 0 ] = id;
				if ( parents.length > 0 ) {
					System.arraycopy( parents, 0, chain, 1, parents.length );
				}
			}
		}
		ownerId = model.getOwnerId();
		if ( ownerId == null ) {
			ownerId = -1;
		}
		revision = model.getRevision();
		if ( revision == null ) {
			revision = -1;
		}

		if ( model.getFromYear() != null  &&  model.getFromYear() != Integer.MAX_VALUE ) {
            fromYear = model.getFromYear();
        }
        if ( model.getToYear() != null  &&  model.getToYear() != Integer.MAX_VALUE ) {
            toYear = model.getToYear();
        }

		//Get the place type id
		if ( model.getType() != null ) {
			typeId = typeMapper.getTypeDTOFromModel( model.getType(), TypeCategory.PLACE ).getId();
		}
		else {
			//This should cause the underlying back-end code to ignore the type
			//TODO:  Technically, we should probably look up the value instead of passing this through
			typeId = -1;
		}
		return new PlaceRepresentationDTO( chain,
										   ownerId,
										   fromYear,
										   toYear,
										   typeId,
										   model.getPreferredLocale(),
										   displayNames,
										   latitude,
										   longitude,
										   model.isPublished(),
										   model.isValidated(),
										   revision,
										   model.getUUID(),
										   groupId,
										   null );
	}

    /**
     * Creates an instance of the PlaceRepresentationModel from a PlaceRepresentation.
     * Basically, this method serializes a PlaceRepresentation into XML/JSON.
     * 
     * @param rep The Place Representation to map.
     * 
     * @return Returns a fully populated PlaceRepresentationModel instance.
     */
    private PlaceRepresentationModel createModelFromPlaceRepresentation( PlaceRepresentation rep, StdLocale locale, String path, boolean includeChildren ) {
        PlaceRepresentationModel            model = new PlaceRepresentationModel();
        NameModel                           displayName = new NameModel();
        NameModel                           fullDisplayName = new NameModel();
        List<NameModel>                     displayNames = new ArrayList<NameModel>();
        PlaceRepresentation[]               chain;
        List<LinkModel>                     links = new ArrayList<LinkModel>();
        LinkModel                           selfLink, placeLink, attrLink, citLink;

        //Set up the atom links
        selfLink = new LinkModel();
        selfLink.setRel( "self" );
        selfLink.setHref( path + REPS_PATH + rep.getId() );
        links.add( selfLink );

        placeLink = new LinkModel();
        placeLink.setRel( "related" );
        placeLink.setHref( path + PlaceMapper.PLACE_PATH + rep.getPlaceId() );
        links.add( placeLink );

        attrLink = new LinkModel();
        attrLink.setRel( "attributes" );
        attrLink.setHref( path + REPS_PATH + rep.getId() + AttributeMapper.ATTRIBUTES_PATH );
        links.add( attrLink );

        citLink = new LinkModel();
        citLink.setRel( "citations" );
        citLink.setHref( path + REPS_PATH + rep.getId() + CitationMapper.CITATIONS_PATH );
        links.add( citLink );

        //Extract the display name.
        displayName.setName( rep.getDisplayName( locale ).get() );
        displayName.setLocale( rep.getDisplayName( locale ).getLocale().toString() );

        //Extract the full display name.
        fullDisplayName.setName( rep.getFullDisplayName( locale ).get() );
        fullDisplayName.setLocale( rep.getFullDisplayName( locale ).getLocale().toString() );

        //Copy from the place rep to the model.
        model.setId( rep.getId() );
        model.setUUID( rep.getUUID() );
        model.setRevision( rep.getRevision() );
        model.setType( typeMapper.createModelFromType( rep.getType(), locale, path, TypeCategory.PLACE ) );
        model.setDisplayName( displayName );
        model.setFullDisplayName( fullDisplayName );
        model.setLinks( links );
        if ( rep.getJurisdictionToYear() != Integer.MAX_VALUE ) {
            model.setToYear( rep.getJurisdictionToYear() );
        }
        if ( rep.getJurisdictionFromYear() != Integer.MIN_VALUE ) {
            model.setFromYear( rep.getJurisdictionFromYear() );
        }
        model.setOwnerId( rep.getPlaceId() );
        if ( rep.getTypeGroup() != null ) {
            PlaceTypeGroupModel group = groupMapper.createModelFromTypeGroup(rep.getTypeGroup(), path);
            model.setGroup( group );
        }
        model.setPreferredLocale( rep.getPreferredLocale().toString() );
        model.setPublished( rep.isPublished() );
        model.setValidated( rep.isValidated() );
        if ( rep.getCentroidLatitude() != null && rep.getCentroidLongitude() != null ) {
            LocationModel                       location = new LocationModel();
            CentroidModel                       centroid = new CentroidModel();

            centroid.setLatitude( rep.getCentroidLatitude() );
            centroid.setLongitude( rep.getCentroidLongitude() );
            location.setCentroid( centroid );

            model.setLocation( location );
        }

        //Handle the jurisdiction chain
        chain = rep.getJurisdictionChain();
        if ( chain.length > 1 ) {
            model.setJurisdiction( commonMapper.createJurisdictionModelFromJurisdictionChain( chain[ 1 ], locale, path ) );
        }

        //Handle all of the display names
        for ( Localized<String> name : rep.getDisplayNames() ) {
            NameModel           nameModel;

            nameModel = new NameModel();
            nameModel.setName( name.get() );
            nameModel.setLocale( name.getLocale().toString() );
            displayNames.add( nameModel );
        }
        model.setDisplayNames( displayNames );

        //Handle children, if necessary
        if ( includeChildren ) {
            PlaceRepresentation[]           children;
            List<PlaceRepresentationModel>  modelChildren = new ArrayList<PlaceRepresentationModel>();

            children = rep.getChildren();
            for ( int i = 0; i < children.length; i++ ) {
                PlaceRepresentationModel    child;

                child = createModelFromPlaceRepresentation( children[ i ], locale, path, false );
                modelChildren.add( child );
            }
            Collections.sort( modelChildren, new DisplayNameComparator() );
            model.setChildren( modelChildren );
        }

        return model;
    }

}
