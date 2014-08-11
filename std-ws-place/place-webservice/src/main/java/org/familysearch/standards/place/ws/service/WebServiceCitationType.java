package org.familysearch.standards.place.ws.service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.ws.mapping.TypeMapper;
import org.familysearch.standards.place.ws.model.RootModel;


/**
 * The "Citation-Type" service has end-points for retrieving information about
 * citation types, as well as managing the creation, update and deletion of
 * these types.
 * 
 * @author dshellman, wjohnson000
 */
@Path(WebServiceCitationType.URL_ROOT + "/" + TypeMapper.CITATION_TYPE_PATH)
public class WebServiceCitationType extends WebServiceGenericType {

	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServiceCitationType() { }


    /* (non-Javadoc)
     * @see org.familysearch.standards.place.ws.service.WebServiceGenericType#getTypeCat()
     */
    @Override
    TypeCategory getTypeCat() {
       return TypeCategory.CITATION;
    }

    /* (non-Javadoc)
     * @see org.familysearch.standards.place.ws.service.WebServiceGenericType#getAllTypes(javax.ws.rs.core.UriInfo, java.lang.String, java.lang.String)
     */
    @Override
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getAllTypes(
            @Context UriInfo uriInfo,
            @QueryParam("accept-language") String acceptLanguage,
            @HeaderParam("Accept-Language") String headerAcceptLang ) throws PlaceDataException {

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
            @PathParam("id") int typeId ) throws PlaceDataException {

        return super.getTypeById(uriInfo, typeId);
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
