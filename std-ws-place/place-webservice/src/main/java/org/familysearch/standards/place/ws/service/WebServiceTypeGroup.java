package org.familysearch.standards.place.ws.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import org.familysearch.standards.place.Group;
import org.familysearch.standards.place.data.GroupType;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.ws.mapping.GroupMapper;
import org.familysearch.standards.place.ws.model.RootModel;
import org.familysearch.standards.place.ws.model.PlaceTypeGroupModel;


/**
 * The "places" web service provides endpoints to support the searching and retrieval of place and related
 * resources.  The "search" endpoint provides the ability to search for Place Representation entities by
 * using a variety of optional search parameters.  Additional endpoints provide direct access to Place
 * and Place Representation resources, as well as Place Types, Variant Name Types, and Type Groups.
 * 
 * @author dshellman
 */
@Path(WebServiceTypeGroup.URL_ROOT + "/" + GroupMapper.GROUP_TYPE_PATH)
public class WebServiceTypeGroup extends WebServiceBase {

	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServiceTypeGroup() { }


	/**
	 * Retrieves the list of published place type groups.
	 * 
	 * @return Returns the response.
	 * @throws PlaceDataException 
	 */
	@GET
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	public Response getPlaceTypeGroups( @Context UriInfo uriInfo ) throws PlaceDataException {
		RootModel				root = new RootModel();
		List<PlaceTypeGroupModel>	groupsModel = new ArrayList<PlaceTypeGroupModel>();

		long startTime = System.currentTimeMillis();

		GroupMapper mapper = new GroupMapper( getReadableService() );
		Set<GroupDTO> groups = getReadableService().getAllGroups(GroupType.PLACE_TYPE);
		for (GroupDTO group : groups) {
		    groupsModel.add(mapper.createModelFromTypeGroup(new Group(group), getPath(uriInfo)));
		}

		root.setPlaceTypeGroups( groupsModel );

		log( null, uriInfo.getPath(), "GET", "time", "" + ( System.currentTimeMillis() - startTime ) );

		return Response.ok( root ).expires( this.getExpireDate() ).build();
	}

	/**
     * Create a new place-type group entry
     * 
     * @param uriInfo
     * @param inRoot RootModel containing the new place-type-group
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @POST
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response createPlaceTypeGroup(
            @Context UriInfo uriInfo,
            RootModel inRoot) throws PlaceDataException {

        RootModel  outRoot = new RootModel();
        long       startTime = System.currentTimeMillis();

        GroupMapper mapper = new GroupMapper( getReadableService() );
        PlaceTypeGroupModel grpModel = inRoot.getPlaceTypeGroup();
        GroupDTO groupDto = mapper.createDTOFromTypeGroup(grpModel, true);

        GroupDTO newGroupDto = getWritableService().create(groupDto, "system");
        PlaceTypeGroupModel grpModelX = mapper.createModelFromTypeGroupWithTypes(new Group(newGroupDto), null, getPath(uriInfo));
        outRoot.setPlaceTypeGroup(grpModelX);
        
        log(null, uriInfo.getPath(), "POST", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.created(URI.create(getPath(uriInfo) + GroupMapper.GROUP_TYPE_PATH + newGroupDto.getId())).entity(outRoot).tag(getEntityTag(newGroupDto.getId(), 0)).build();
    }

	/**
	 * Retrieves the details of a specific place type group.
	 * 
	 * @param uriInfo
	 * @param groupId
	 * 
	 * @return Returns the response.
	 * @throws PlaceDataException 
	 */
	@Path("{id}")
	@GET
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	public Response getPlaceTypeGroupById(
	        @Context UriInfo uriInfo,
	        @PathParam("id") int groupId,
	        @QueryParam("accept-language") String acceptLanguage,
	        @HeaderParam("Accept-Language") String headerAcceptLang ) throws PlaceDataException {

	    RootModel root = new RootModel();
		long startTime = System.currentTimeMillis();
		StdLocale outLocale = getLocale( acceptLanguage, headerAcceptLang );
		GroupMapper mapper = new GroupMapper( getReadableService() );

		GroupDTO group = getReadableService().getGroupById( GroupType.PLACE_TYPE, groupId );
		root.setPlaceTypeGroup(mapper.createModelFromTypeGroupWithTypes(new Group(group), outLocale, getPath( uriInfo )));

		log( null, uriInfo.getPath(), "GET", "time", "" + ( System.currentTimeMillis() - startTime ) );

		return Response.ok( root ).expires( this.getExpireDate() ).tag( getEntityTag( group.getId(), 0 ) ).build();
	}

    /**
     * Update an existing place-type group entry
     * 
     * @param uriInfo
     * @param inRoot RootModel containing the existing place-type-group
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}")
    @PUT
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response updatePlaceTypeGroup(
            @Context UriInfo uriInfo,
            RootModel inRoot) throws PlaceDataException {

        RootModel  outRoot = new RootModel();
        long       startTime = System.currentTimeMillis();

        GroupMapper mapper = new GroupMapper( getReadableService() );
        PlaceTypeGroupModel grpModel = inRoot.getPlaceTypeGroup();
        GroupDTO groupDto = mapper.createDTOFromTypeGroup(grpModel, true);

        GroupDTO updGroupDto = getWritableService().update(groupDto, "system");
        PlaceTypeGroupModel grpModelX = mapper.createModelFromTypeGroupWithTypes(new Group(updGroupDto), null, getPath(uriInfo));
        outRoot.setPlaceTypeGroup(grpModelX);
        
        log(null, uriInfo.getPath(), "PUT", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok().entity(outRoot).tag(getEntityTag(updGroupDto.getId(), 0)).build();
    }

}
