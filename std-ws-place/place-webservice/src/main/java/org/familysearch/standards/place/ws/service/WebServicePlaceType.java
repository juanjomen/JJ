package org.familysearch.standards.place.ws.service;

import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.familysearch.standards.place.data.GroupType;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.mapping.TypeMapper;
import org.familysearch.standards.place.ws.model.RootModel;


/**
 * The "Place-Type" service has end-points for retrieving information about
 * place types, as well as managing the creation, update and deletion of
 * these types.
 * 
 * @author dshellman, wjohnson000
 */
@Path(WebServicePlaceType.URL_ROOT + "/" + TypeMapper.PLACE_TYPE_PATH)
public class WebServicePlaceType extends WebServiceGenericType {

	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServicePlaceType() { }


    /* (non-Javadoc)
     * @see org.familysearch.standards.place.ws.service.WebServiceGenericType#getTypeCat()
     */
    @Override
    TypeCategory getTypeCat() {
       return TypeCategory.PLACE;
    }

    @Override
	@GET
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	public Response getAllTypes(
	        @Context UriInfo uriInfo,
	        @QueryParam("accept-language") String acceptLanguage,
	        @HeaderParam("Accept-Language") String headerAcceptLang) throws PlaceDataException {

        return super.getAllTypes(uriInfo, acceptLanguage, headerAcceptLang);
	}

	/* (non-Javadoc)
	 * @see org.familysearch.standards.place.ws.service.WebServiceGenericType#createType(javax.ws.rs.core.UriInfo, org.familysearch.standards.place.ws.model.RootModel)
	 */
	@Override
    @POST
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response createType(
            @Context UriInfo uriInfo,
            RootModel inRoot) throws PlaceDataException {

	    return super.createType(uriInfo, inRoot);
    }

	/* (non-Javadoc)
	 * @see org.familysearch.standards.place.ws.service.WebServiceGenericType#getTypeById(javax.ws.rs.core.UriInfo, int)
	 */
	@Override
	@Path("{id}")
	@GET
	@Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
	public Response getTypeById(
	        @Context UriInfo uriInfo,
	        @PathParam("id") int typeId) throws PlaceDataException {

	    // We can't simply call the "super.getTypeById(...)" method since this has to
	    // include the place-type-groups associated with this group-type
		RootModel root = new RootModel();
		long startTime = System.currentTimeMillis();

		TypeMapper typeMapper = new TypeMapper(getReadableService());
		TypeDTO    typeDto = getReadableService().getTypeById(getTypeCat(), typeId);
		Set<GroupDTO> groups = getReadableService().getGroupsByMemberId(GroupType.PLACE_TYPE, typeId);
		root.setType(typeMapper.createModelFromPlaceTypeDTO(typeDto, groups, getPath(uriInfo)));

		log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

		return Response.ok(root).expires(this.getExpireDate()).tag(getEntityTag(typeDto.getId(), 0)).build();
	}

	/* (non-Javadoc)
	 * @see org.familysearch.standards.place.ws.service.WebServiceGenericType#updateType(javax.ws.rs.core.UriInfo, org.familysearch.standards.place.ws.model.RootModel)
	 */
	@Override
    @Path("{id}")
    @PUT
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response updateType(
            @Context UriInfo uriInfo,
            RootModel inRoot) throws PlaceDataException {

        return super.updateType(uriInfo, inRoot);
    }
}
