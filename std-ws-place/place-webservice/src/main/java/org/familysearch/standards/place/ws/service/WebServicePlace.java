package org.familysearch.standards.place.ws.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.data.PlaceDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.PlaceRepresentationDTO;
import org.familysearch.standards.place.ws.mapping.GroupMapper;
import org.familysearch.standards.place.ws.mapping.PlaceMapper;
import org.familysearch.standards.place.ws.mapping.PlaceRepresentationMapper;
import org.familysearch.standards.place.ws.mapping.SourceMapper;
import org.familysearch.standards.place.ws.mapping.TypeMapper;
import org.familysearch.standards.place.ws.model.HealthCheckModel;
import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.model.PlaceModel;
import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;
import org.familysearch.standards.place.ws.model.RootModel;


/**
 * The "places" web service provides endpoints to support the searching and retrieval of place and related
 * resources.  The "search" endpoint provides the ability to search for Place Representation entities by
 * using a variety of optional search parameters.  Additional endpoints provide direct access to Place
 * and Place Representation resources, as well as Place Types, Variant Name Types, and Type Groups.
 * 
 * @author dshellman
 */
@Path(WebServicePlaceRep.URL_ROOT)
public class WebServicePlace extends WebServiceBase{


	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServicePlace() { }

    /**
     * Health check.  This endpoint is used to check to see if the service is up and running.  It also
     * provides information about the endpoints supported.
     * 
     * @param uriInfo URI Info instance
     * 
     * @return Returns endpoint info, as well as general info about the service and its health.
     */
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getHealth( @Context UriInfo uriInfo ) {
        RootModel               root = new RootModel();
        HealthCheckModel        health = new HealthCheckModel();
        List<LinkModel>         links = new ArrayList<LinkModel>();
        LinkModel               link;

        // Setup the links
        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + PlaceMapper.PLACE_PATH );
        link.setRel( "places" );
        link.setTitle( "Places" );
        links.add( link );

        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + PlaceRepresentationMapper.REPS_PATH );
        link.setRel( "place-reps" );
        link.setTitle( "Place Representations" );
        links.add( link );

        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + SEARCH_ROOT );
        link.setRel( "search" );
        link.setTitle( "Search" );
        links.add( link );

        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + GroupMapper.GROUP_TYPE_PATH );
        link.setRel( "type-groups" );
        link.setTitle( "Type Groups" );
        links.add( link );

        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + TypeMapper.NAME_TYPE_PATH );
        link.setRel( "name-types" );
        link.setTitle( "Name Types" );
        links.add( link );

        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + TypeMapper.PLACE_TYPE_PATH );
        link.setRel( "place-types" );
        link.setTitle( "Place Types" );
        links.add( link );

        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + TypeMapper.ATTRIBUTE_TYPE_PATH );
        link.setRel( "attribute-types" );
        link.setTitle( "Attribute Types" );
        links.add( link );

        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + TypeMapper.CITATION_TYPE_PATH );
        link.setRel( "citation-types" );
        link.setTitle( "Citation Types" );
        links.add( link );

        link = new LinkModel();
        link.setHref( getPath( uriInfo ) + SourceMapper.SOURCES_PATH );
        link.setRel( "sources" );
        link.setTitle( "Sources" );
        links.add( link );

        String status = "";
        if (getWritableService().isWriteReady()  &&  getReadableService().isReadReady()) {
            status = "read-write";
        } else if (getReadableService().isReadReady()) {
            status = "read-only";
        } else {
            status = "error-config";
        }

        health.setAPIVersion( getPlaceService().getAPIVersion() );
        health.setWSVersion( getWsVersion() );
        health.setCurrentRevision( ( int ) getSolrService().getMetrics().getCurrentRevisionNumber().getValue() );
        health.setStatus( status );
        health.setLinks( links );

        root.setHealthCheck( health );

        return Response.ok( root ).build();
    }

	/**
	 * Retrieves an existing Place by its id.
	 * 
	 * @param placeId The place id.
	 * @param acceptLanguage The request locale for the returned data.
	 * @param headerAcceptLang The request locale (from the header) for the returned data.
	 * @param version The version of the place requested.
	 * 
	 * @return Returns the place data.
	 * @throws PlaceDataException 
	 */
	@Path(PlaceMapper.PLACE_PATH + "{id}")
	@GET
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	public Response getPlace( @Context UriInfo uriInfo,
						 	  @PathParam("id") int placeId,
						 	  @QueryParam("accept-language") String acceptLanguage,
						 	  @HeaderParam("Accept-Language") String headerAcceptLang,
						 	  @QueryParam("vTag") String version ) throws PlaceDataException {

	    RootModel  root = new RootModel();
		long       startTime = System.currentTimeMillis();

		//Figure out what language they want the info back in.
		StdLocale outLocale = getLocale( acceptLanguage, headerAcceptLang );

		PlaceMapper placeMapper = new PlaceMapper( getReadableService() );
		PlaceDTO    placeDto = getReadableService().getPlaceById( placeId, version );
		if ( placeDto == null ) {
		    //This means that the place doesn't exist by that id.
		    log( null, uriInfo.getPath(), "GET", "user_error", "not found" );
		    return Response.status( Response.Status.NOT_FOUND ).build();
		}
		root.setPlace( placeMapper.createModelFromDTO( placeDto, outLocale, getPath( uriInfo ) ) );

		log( null, uriInfo.getPath(), "GET", "version", version, "time", "" + ( System.currentTimeMillis() - startTime ) );

		return Response.ok( root ).expires( this.getExpireDate() ).tag( getEntityTag( placeDto.getId(), placeDto.getRevision() ) ).build();
	}

	/**
	 * Updates an existing Place.  This is done by a PUT to /places/<placeId>.
	 * 
	 * @param inPlaces Contains the data for the place
	 * 
	 * @return Returns the status of the update call.
	 * @throws PlaceDataException 
	 */
	@Path(PlaceMapper.PLACE_PATH + "{id}")
	@PUT
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	@Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
	public Response putPlace( @Context UriInfo uriInfo,
							   @PathParam("id") int placeId,
							   RootModel inRoot,
							   @QueryParam("accept-language") String acceptLanguage,
							   @HeaderParam("Accept-Language") String headerAcceptLang ) throws PlaceDataException {

	    RootModel  outRoot = new RootModel();
		long       startTime = System.currentTimeMillis();

		PlaceMapper placeMapper = new PlaceMapper( getReadableService() );
		PlaceDTO    placeDto = placeMapper.createDTOFromModel( inRoot.getPlace() );
		placeDto = getWritableService().update( placeDto, "system" );
		outRoot.setPlace( placeMapper.createModelFromDTO( placeDto, getLocale( acceptLanguage, headerAcceptLang ), getPath( uriInfo ) ) );

		log( null, uriInfo.getPath(), "PUT", "time", "" + ( System.currentTimeMillis() - startTime ) );

		return Response.ok( outRoot ).tag( getEntityTag( placeDto.getId(), placeDto.getRevision() ) ).build();
	}

	/**
	 * Creates a new PlaceRepresentation instance.  This is done by a POST to /places/<placeId>.
	 * 
	 * @param inPlaces Contains the data for the place representation
	 * 
	 * @return Returns the status of the create call.
	 * @throws PlaceDataException 
	 */
	@Path(PlaceMapper.PLACE_PATH + "{id}")
	@POST
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	@Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
	public Response createPlaceRep( @Context UriInfo uriInfo,
									 @PathParam("id") int placeId,
									 RootModel inRoot,
									 @QueryParam("accept-language") String acceptLanguage,
									 @HeaderParam("Accept-Language") String headerAcceptLang ) throws PlaceDataException {

	    RootModel					outRoot = new RootModel();
		PlaceRepresentationDTO		repDto;
		PlaceRepresentationModel	repModel;
		PlaceRepresentationMapper	mapper;

		long startTime = System.currentTimeMillis();

		mapper = new PlaceRepresentationMapper( getReadableService() );
		repModel = inRoot.getPlaceRepresentation();
		repDto = mapper.createDTOFromModel( repModel, null );
		repDto = getWritableService().create( repDto, "system" );
		outRoot.setPlaceRepresentation( mapper.createModelFromDTO( repDto, getLocale( acceptLanguage, headerAcceptLang ), getPath( uriInfo ), false, true ) );

		log( null, uriInfo.getPath(), "POST", "placeRepId", "" + repDto.getId(), "time", "" + ( System.currentTimeMillis() - startTime ) );

		return Response.created( URI.create( getPath( uriInfo ) + PlaceRepresentationMapper.REPS_PATH + repDto.getId() ) ).entity( outRoot ).tag( getEntityTag( repDto.getId(), repDto.getRevision() ) ).build();
	}

	/**
	 * Creates a new Place instance.  This is done by a POST to /places.  Note that this
	 * requires the creation of a new place representation, as well.
	 * 
	 * @param inPlaces Contains the data for the place
	 * 
	 * @return Returns the status of the create call.
	 * @throws PlaceDataException 
	 */
	@POST
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	@Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
	public Response createPlace( @Context UriInfo uriInfo,
								  RootModel inRoot,
			 					  @QueryParam("accept-language") String acceptLanguage,
			 					  @HeaderParam("Accept-Language") String headerAcceptLang ) throws PlaceDataException {
		RootModel						outRoot = new RootModel();
		PlaceRepresentationDTO			repDto, createRepDto;
		PlaceDTO						placeDto, createPlaceDto;
		long							startTime;
		PlaceModel						place;
		PlaceRepresentationModel		placeRep;
		List<PlaceRepresentationModel>	placeReps = new ArrayList<PlaceRepresentationModel>();
		PlaceMapper						placeMapper;
		PlaceRepresentationMapper		placeRepMapper;

		startTime = System.currentTimeMillis();

		StdLocale outLocale = getLocale( acceptLanguage, headerAcceptLang );
		placeRepMapper = new PlaceRepresentationMapper( getReadableService() );
		placeMapper = new PlaceMapper( getReadableService() );
		place = inRoot.getPlace();
		if ( place.getReps().size() != 1 ) {
		    log( null, "/", "POST", "user_error", "Must include one and only one Place Representation during the creation of a Place." );
		    return Response.status( Response.Status.BAD_REQUEST ).entity( "Must include one and only one Place Representation during the creation of a Place." ).build();
		}
		placeRep = place.getReps().get( 0 );

		placeDto = placeMapper.createDTOFromModel( place );
		repDto = placeRepMapper.createDTOFromModel( placeRep, null );

		createRepDto = getWritableService().create( placeDto, repDto, "system" );
		createPlaceDto = getReadableService().getPlaceById(createRepDto.getOwnerId(), null);

		if ( createPlaceDto == null ) {
		    createPlaceDto = placeDto;
		}
		place = placeMapper.createModelFromDTO( createPlaceDto, outLocale, getPath( uriInfo ) );
		placeReps.add( placeRepMapper.createModelFromDTO( createRepDto, outLocale, getPath( uriInfo ), false, true ) );
		place.setReps( placeReps );
		outRoot.setPlace( place );

		log( null, "/", "POST", "placeId", "" + createRepDto.getOwnerId(), "placeRepId", "" + createRepDto.getId(), "time", "" + ( System.currentTimeMillis() - startTime ) );

		return Response.created( URI.create( getPath( uriInfo ) + PlaceMapper.PLACE_PATH + createRepDto.getOwnerId() ) ).entity( outRoot ).tag( getEntityTag( createPlaceDto.getId(), createPlaceDto.getRevision() ) ).build();
	}

}
