package org.familysearch.standards.place.ws.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.familysearch.standards.place.data.ExternalXrefDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.ws.mapping.LinkMapper;
import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.model.RootModel;


/**
 * The "XREF" service has end-points for creating and retrieving external cross-reference
 * associations for place-reps.
 * 
 * @author dshellman, wjohnson000
 */
@Path(WebServiceXref.URL_ROOT + LinkMapper.LINKS_PATH)
public class WebServiceXref extends WebServiceBase {

	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServiceXref() { }


    /**
     * Maybe it retrieves a list of links to all published external-xref types.
     * NOTE: for now this will return a "200" status and an empty "RootModel" payload.
     * 
     * @param uriInfo
     * @param acceptLanguage
     * @param headerAcceptLang
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getAllExtXrefTypes(
            @Context UriInfo uriInfo,
            @QueryParam("accept-language") String acceptLanguage,
            @HeaderParam("Accept-Language") String headerAcceptLang) throws PlaceDataException {

        RootModel root = new RootModel();
        long startTime = System.currentTimeMillis();

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).build();
    }

    /**
     * Retrieve the list of place-rep links tied to an external cross-reference.
     * 
     * @param uriInfo
     * @param linkId the composite identifier, which consists of an ext-xref "code" and
     *        an external-key, separated by a period (.).  The external-key must be
     *        URL-Encoded if there are special characters as part of the value.
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{linkId}")
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getPlaceRepsByLinkId(
            @Context UriInfo uriInfo,
            @PathParam("linkId") String linkId) throws PlaceDataException {

        RootModel  root = new RootModel();
        long startTime = System.currentTimeMillis();

        int ndx = linkId.indexOf('.');
        if (ndx < 0) {
            log(null, "/", "GET", "user_error", "Invalid link-id: " + linkId);
            return Response.status(Response.Status.BAD_REQUEST).entity("Link ID is malformed - must be of the form <code>.<external-key>").build();
        }

        LinkMapper linkMapper = new LinkMapper(getReadableService());
        String extCode = linkId.substring(0, ndx);
        String extKey;
        try {
            extKey = URLDecoder.decode(linkId.substring(ndx+1), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("External Key is malformed").build();
        }

        List<LinkModel> links = new ArrayList<LinkModel>();
        List<ExternalXrefDTO> linkDTOs = getReadableService().getExtXrefByTypeAndKey(extCode, extKey);
        for (ExternalXrefDTO extXrefDto : linkDTOs) {
            if (extXrefDto.isPublished()) {
                links.add(linkMapper.createPlaceRepLinkFromDTO(extXrefDto, getPath(uriInfo)));
            }
        }
        root.setLinks(links);

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).tag(linkId).build();
    }

    /**
     * update the list of place-rep identifiers associated with an external
     * cross-reference.
     * 
     * @param uriInfo
     * @param linkId the composite identifier, which consists of an ext-xref "code" and
     *        an external-key, separated by a period (.).  The external-key must be
     *        URL-Encoded if there are special characters as part of the value.
     * @param inRoot RootModel containing the list of place-rep identifiers
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{linkId}")
    @PUT
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response createOrUpdatePlaceRepsByLinkId(
            @Context UriInfo uriInfo,
            @PathParam("linkId") String linkId,
            RootModel inRoot) throws PlaceDataException {

        RootModel  outRoot = new RootModel();
        long       startTime = System.currentTimeMillis();

        int ndx = linkId.indexOf('.');
        if (ndx < 0) {
            log(null, "/", "GET", "user_error", "Invalid link-id: " + linkId);
            return Response.status(Response.Status.BAD_REQUEST).entity("Link ID is malformed - must be of the form <code>.<external-key>").build();
        }

        LinkMapper linkMapper = new LinkMapper(getReadableService());
        String extCode = linkId.substring(0, ndx);
        String extKey;
        try {
            extKey = URLDecoder.decode(linkId.substring(ndx+1), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("External Key is malformed").build();
        }

        List<LinkModel> links = new ArrayList<LinkModel>();
        List<ExternalXrefDTO> linkDTOs = getWritableService().createOrUpdate(extCode, extKey, inRoot.getRepIds());
        for (ExternalXrefDTO extXrefDto : linkDTOs) {
            if (extXrefDto.isPublished()) {
                links.add(linkMapper.createPlaceRepLinkFromDTO(extXrefDto, getPath(uriInfo)));
            }
        }
        outRoot.setLinks(links);

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(outRoot).expires(this.getExpireDate()).tag(linkId).build();
    }
}
