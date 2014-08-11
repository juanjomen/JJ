package org.familysearch.standards.place.ws.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
import org.familysearch.standards.place.data.AttributeDTO;
import org.familysearch.standards.place.data.CitationDTO;
import org.familysearch.standards.place.data.ExternalXrefDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.PlaceRepresentationDTO;
import org.familysearch.standards.place.ws.mapping.AttributeMapper;
import org.familysearch.standards.place.ws.mapping.CitationMapper;
import org.familysearch.standards.place.ws.mapping.LinkMapper;
import org.familysearch.standards.place.ws.mapping.PlaceRepresentationMapper;
import org.familysearch.standards.place.ws.model.AttributeModel;
import org.familysearch.standards.place.ws.model.CitationModel;
import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.model.RootModel;


/**
 * The "places" web service provides endpoints to support the searching and retrieval of place and related
 * resources.  The "search" endpoint provides the ability to search for Place Representation entities by
 * using a variety of optional search parameters.  Additional endpoints provide direct access to Place
 * and Place Representation resources, as well as Place Types, Variant Name Types, and Type Groups.
 * 
 * @author dshellman
 */
@Path(WebServicePlaceRep.URL_ROOT + "/" + PlaceRepresentationMapper.REPS_PATH)
public class WebServicePlaceRep extends WebServiceBase{

	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServicePlaceRep() { }


    /**
     * This is the resource end point for getting a single Place Representation by its id.  By
     * passing the "children" parameter with a value of true, all of the direct children of
     * the place representation will be returned (that is, those place representations that
     * exist within the requested place representation's jurisdiction).
     * 
     * @param placeRepId
     * @param versionTag
     * @param acceptLanguage
     * @param headerAcceptLang
     * @param includeChildren
     * 
     * @return Returns the details for the place representation.
     * @throws PlaceDataException 
     */
    @Path("{id}")
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getPlaceRep(@Context UriInfo uriInfo,
                                  @PathParam("id") int placeRepId,
                                  @QueryParam("accept-language") String acceptLanguage,
                                  @HeaderParam("Accept-Language") String headerAcceptLang,
                                  @QueryParam("vTag") String versionTag,
                                  @DefaultValue("false") @QueryParam("children") boolean includeChildren,
                                  @DefaultValue("true") @QueryParam("pubonly") boolean pubOnly) throws PlaceDataException {
        RootModel                   root = new RootModel();
        PlaceRepresentationDTO      repDto;
        PlaceRepresentationMapper   mapper;

        long startTime = System.currentTimeMillis();

        //Figure out what language they want the info back in.
        StdLocale outLocale = getLocale(acceptLanguage, headerAcceptLang);

        mapper = new PlaceRepresentationMapper(getReadableService());
        repDto = getReadableService().getPlaceRepresentationById(placeRepId, versionTag);
        if (repDto == null) {
            //This means that the place rep doesn't exist.
            log(null, uriInfo.getPath(), "GET", "user_error", "not found", "version", versionTag);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        root.setPlaceRepresentation(mapper.createModelFromDTO(repDto, outLocale, getPath(uriInfo), includeChildren, pubOnly));

        log(null, uriInfo.getPath(), "GET", "children", "" + includeChildren, "version", versionTag, "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).tag(getEntityTag(repDto.getId(), repDto.getRevision())).build();
    }


    /**
     * Updates an existing PlaceRepresentation.  This is done by a PUT to /places/reps/<placeRepId>.
     * 
     * @param inPlaces Contains the data for the place representation
     * 
     * @return Returns the status of the update call.
     * @throws PlaceDataException 
     */
    @Path("{id}")
    @PUT
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response putPlaceRep(@Context UriInfo uriInfo,
                                  @PathParam("id") int placeRepId,
                                  RootModel inRoot,
                                  @QueryParam("accept-language") String acceptLanguage,
                                  @HeaderParam("Accept-Language") String headerAcceptLang) throws PlaceDataException {
        RootModel                   outRoot = new RootModel();
        PlaceRepresentationDTO      repDto;
        PlaceRepresentationMapper   repMapper;
        long                        startTime = System.currentTimeMillis();

        repMapper = new PlaceRepresentationMapper(getReadableService());
        repDto = repMapper.createDTOFromModel(inRoot.getPlaceRepresentation(), placeRepId);
        repDto = getWritableService().update(repDto, "system");

        outRoot.setPlaceRepresentation(repMapper.createModelFromDTO(repDto, getLocale(acceptLanguage, headerAcceptLang), getPath(uriInfo), false, true));

        log(null, uriInfo.getPath(), "PUT", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(outRoot).tag(getEntityTag(repDto.getId(), repDto.getRevision())).build();
    }


    /**
     * Deletes a place representation.  Note that the "newRepId" parameter is required and
     * specifies the replacement place representation for the deleted one.
     * 
     * @param uriInfo
     * @param newRepId
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}")
    @DELETE
    public Response deletePlaceRep(@Context UriInfo uriInfo,
                                    @PathParam("id") Integer placeRepId,
                                    @QueryParam("newRepId") Integer newRepId) throws PlaceDataException {
        PlaceRepresentationDTO  dto;
        long                    startTime = System.currentTimeMillis();

        dto = getReadableService().getPlaceRepresentationById(placeRepId, null);
        getWritableService().delete(dto, newRepId, "system");

        log(null, uriInfo.getPath(), "DELETE", "newRepId", "" + newRepId, "time", "" + (System.currentTimeMillis() - startTime));

        return Response.noContent().build();
    }
    
    /**
     * Retrieves the list of attributes associated with a place-rep
     * 
     * @param uriInfo
     * @param acceptLanguage
     * @param headerAcceptLang
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + AttributeMapper.ATTRIBUTES_PATH)
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getPlaceRepAttrs(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            @QueryParam("vTag") String versionTag) throws PlaceDataException {

        RootModel             root = new RootModel();
        List<AttributeModel>  attributes = new ArrayList<AttributeModel>();

        long startTime = System.currentTimeMillis();

        AttributeMapper attrMapper = new AttributeMapper(getReadableService());
        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, versionTag);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "GET", "user_error", "not found", "version", versionTag);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Set<AttributeDTO> attributeSet = getReadableService().getAttributesByRepId(placeRepId, versionTag);
        for (AttributeDTO attrDto : attributeSet) {
            attributes.add(attrMapper.createModelFromDTO(attrDto, getPath(uriInfo)));
        }
        root.setAttributes(attributes);

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).build();
    }

    /**
     * Create a new attribute, associated with a place-rep
     * 
     * @param uriInfo
     * @param placeRepId place-representation identifier
     * @param inRoot {@link RootModel} that contains the new attribute definition
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + AttributeMapper.ATTRIBUTES_PATH)
    @POST
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response createPlaceRepAttr(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            RootModel inRoot) throws PlaceDataException {

        AttributeDTO  attrDTO;
        RootModel    outRoot = new RootModel();
        long         startTime = System.currentTimeMillis();

        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, null);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "POST", "user_error", "not found", "version", null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        AttributeMapper attrMapper = new AttributeMapper(getReadableService());
        AttributeModel  attrModel  = inRoot.getAttribute();
        AttributeDTO    tempDTO    = attrMapper.createDTOFromModel(attrModel);

        attrDTO = getWritableService().create(tempDTO, "system");
        outRoot.setAttribute(attrMapper.createModelFromDTO(tempDTO, getPath(uriInfo)));

        log(null, uriInfo.getPath(), "POST", "time", "" + (System.currentTimeMillis() - startTime));

        return Response
            .created(URI.create(getPath(uriInfo) + PlaceRepresentationMapper.REPS_PATH + placeRepId + AttributeMapper.ATTRIBUTES_PATH + attrDTO.getId()))
            .entity(outRoot)
            .tag(getEntityTag(attrDTO.getId(), 0))
            .build();
    }

    /**
     * Retrieve all a single attribute associated with a place-representation.
     * 
     * @param uriInfo URI
     * @param placeRepId place-rep identifier
     * @param attrId attribute identifier
     * @param versionTag version identifier
     * @return
     * @throws PlaceDataException 
     */
    @Path("{id}" + AttributeMapper.ATTRIBUTES_PATH + "{attrId}")
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getPlaceRepAttrById(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            @PathParam("attrId") int attrId,
            @QueryParam("vTag") String versionTag) throws PlaceDataException {
        RootModel       root = new RootModel();
        AttributeModel  attribute = null;
        long startTime = System.currentTimeMillis();

        AttributeMapper attrMapper = new AttributeMapper(getReadableService());
        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, versionTag);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "GET", "user_error", "not found", "version", versionTag);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Set<AttributeDTO> attributeSet = getReadableService().getAttributesByRepId(placeRepId, versionTag);
        for (AttributeDTO attrDto : attributeSet) {
            if (attrDto.getId() == attrId) {
                attribute = attrMapper.createModelFromDTO(attrDto, getPath(uriInfo));
                break;
            }
        }

        // The attribute isn't associated with the given place-rep
        if (attribute == null) {
            log(null, uriInfo.getPath(), "GET", "user_error", "not found", "version", versionTag);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        root.setAttribute(attribute);

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).build();
    }

    /**
     * Update an existing attribute, associated with a place-rep
     * 
     * @param uriInfo
     * @param placeRepId place-representation identifier
     * @param inRoot {@link RootModel} that contatins the new attribute definition
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + AttributeMapper.ATTRIBUTES_PATH + "{attributeId}")
    @PUT
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response updatePlaceRepAttr(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            @PathParam("attributeId") int attributeId,
            RootModel inRoot) throws PlaceDataException {

        AttributeDTO  attrDTO;
        RootModel     outRoot = new RootModel();
        long          startTime = System.currentTimeMillis();

        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, null);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "PUT", "user_error", "place-representation not found", String.valueOf(placeRepId), null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Ensure that the attribute exists ...
        boolean found = false;
        Set<AttributeDTO> attributes = getReadableService().getAttributesByRepId(placeRepId, null);
        for (AttributeDTO attribute : attributes) {
            if (attribute.getId() == attributeId) {
                found = true;
            }
        }

        if (! found) {
            log(null, uriInfo.getPath(), "PUT", "user_error", "attribute not found", String.valueOf(attributeId), null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        AttributeMapper attrMapper = new AttributeMapper(getReadableService());
        AttributeModel  attrModel  = inRoot.getAttribute();
        AttributeDTO    tempDTO   = attrMapper.createDTOFromModel(attrModel);

        attrDTO = getWritableService().update(tempDTO, "system");
        outRoot.setAttribute(attrMapper.createModelFromDTO(tempDTO, getPath(uriInfo)));

        log(null, uriInfo.getPath(), "PUT", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(outRoot).tag(getEntityTag(attrDTO.getId(), attrDTO.getRevision())).build();
    }

    /**
     * Delete an existing attribute, associated with a place-rep
     * 
     * @param uriInfo
     * @param placeRepId place-representation identifier
     * @param inRoot {@link RootModel} that contatins the new attribute definition
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + AttributeMapper.ATTRIBUTES_PATH + "{attributeId}")
    @DELETE
    public Response deletePlaceRepAttr(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            @PathParam("attributeId") int attributeId) throws PlaceDataException {

        long startTime = System.currentTimeMillis();

        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, null);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "DELETE", "user_error", "place-representation not found", String.valueOf(placeRepId), null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Ensure that the attribute exists ...
        boolean found = false;
        Set<AttributeDTO> attributes = getReadableService().getAttributesByRepId(placeRepId, null);
        for (AttributeDTO attribute : attributes) {
            if (attribute.getId() == attributeId) {
                found = true;
            }
        }

        if (! found) {
            log(null, uriInfo.getPath(), "DELETE", "user_error", "attribute not found", String.valueOf(attributeId), null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Create a dummy attribute DTO with the attribute-id and place-rep-id
        AttributeDTO tempDTO = new AttributeDTO(attributeId, placeRepId, 0, 0, null, 0);
        getWritableService().delete(tempDTO, "system");

        log(null, uriInfo.getPath(), "DELETE", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.noContent().build();
    }

    /**
     * Retrieves the list of citations associated with a place-rep
     * 
     * @param uriInfo
     * @param acceptLanguage
     * @param headerAcceptLang
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + CitationMapper.CITATIONS_PATH)
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getPlaceRepCitations(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            @QueryParam("vTag") String versionTag) throws PlaceDataException {

        RootModel             root = new RootModel();
        List<CitationModel>   citations = new ArrayList<CitationModel>();
        long startTime = System.currentTimeMillis();

        CitationMapper citationMapper = new CitationMapper(getReadableService());
        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, versionTag);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "GET", "user_error", "not found", "version", versionTag);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Set<CitationDTO> citationSet = getReadableService().getCitationsByRepId(placeRepId, versionTag);
        for (CitationDTO citationDto : citationSet) {
            citations.add(citationMapper.createModelFromDTO(citationDto, getPath(uriInfo)));
        }
        root.setCitations(citations);

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).build();
    }

    /**
     * Create a new citation, associated with a place-rep
     * 
     * @param uriInfo
     * @param placeRepId place-representation identifier
     * @param inRoot {@link RootModel} that contains the new citation definition
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + CitationMapper.CITATIONS_PATH)
    @POST
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response createPlaceRepCitation(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            RootModel inRoot) throws PlaceDataException {

        CitationDTO  citDTO;
        RootModel    outRoot = new RootModel();
        long         startTime = System.currentTimeMillis();

        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, null);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "POST", "user_error", "not found", "version", null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        CitationMapper citMapper = new CitationMapper(getReadableService());
        CitationModel  citModel  = inRoot.getCitation();
        CitationDTO    tempDTO   = citMapper.createDTOFromModel(citModel);

        citDTO = getWritableService().create(tempDTO, "system");
        outRoot.setCitation(citMapper.createModelFromDTO(tempDTO, getPath(uriInfo)));

        log(null, uriInfo.getPath(), "POST", "time", "" + (System.currentTimeMillis() - startTime));

        return Response
            .created(URI.create(getPath(uriInfo) + PlaceRepresentationMapper.REPS_PATH + placeRepId + CitationMapper.CITATIONS_PATH + citDTO.getId()))
            .entity(outRoot)
            .tag(getEntityTag(citDTO.getId(), 0))
            .build();
    }

    /**
     * Retrieve a specific citation associated with a place-rep
     * 
     * @param uriInfo
     * @param placeRepId place-representation identifier
     * @param citationId citation identifier
     * @param versionTag version tag
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + CitationMapper.CITATIONS_PATH + "{citationId}")
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getPlaceRepCitationById(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            @PathParam("citationId") int citationId,
            @QueryParam("vTag") String versionTag) throws PlaceDataException {
        RootModel       root = new RootModel();
        CitationModel   citation = null;
        long startTime = System.currentTimeMillis();

        CitationMapper citationMapper = new CitationMapper(getReadableService());
        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, versionTag);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "GET", "user_error", "not found", "version", versionTag);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Set<CitationDTO> citationSet = getReadableService().getCitationsByRepId(placeRepId, versionTag);
        for (CitationDTO citationDto : citationSet) {
            if (citationDto.getId() == citationId) {
                citation = citationMapper.createModelFromDTO(citationDto, getPath(uriInfo));
                break;
            }
        }

        // The citation isn't associated with the given place-rep
        if (citation == null) {
            log(null, uriInfo.getPath(), "GET", "user_error", "not found", "version", versionTag);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        root.setCitation(citation);

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).build();
    }

    /**
     * Update an existing citation, associated with a place-rep
     * 
     * @param uriInfo
     * @param placeRepId place-representation identifier
     * @param inRoot {@link RootModel} that contatins the new citation definition
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + CitationMapper.CITATIONS_PATH + "{citationId}")
    @PUT
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response updatePlaceRepCitation(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            @PathParam("citationId") int citationId,
            RootModel inRoot) throws PlaceDataException {

        CitationDTO  citDTO;
        RootModel    outRoot = new RootModel();
        long         startTime = System.currentTimeMillis();

        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, null);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "PUT", "user_error", "place-representation not found", String.valueOf(placeRepId), null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Ensure that the citation exists ...
        boolean found = false;
        Set<CitationDTO> citations = getReadableService().getCitationsByRepId(placeRepId, null);
        for (CitationDTO citation : citations) {
            if (citation.getId() == citationId) {
                found = true;
            }
        }

        if (! found) {
            log(null, uriInfo.getPath(), "PUT", "user_error", "citation not found", String.valueOf(citationId), null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        CitationMapper citMapper = new CitationMapper(getReadableService());
        CitationModel  citModel  = inRoot.getCitation();
        CitationDTO    tempDTO   = citMapper.createDTOFromModel(citModel);

        citDTO = getWritableService().update(tempDTO, "system");
        outRoot.setCitation(citMapper.createModelFromDTO(tempDTO, getPath(uriInfo)));

        log(null, uriInfo.getPath(), "PUT", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(outRoot).tag(getEntityTag(citDTO.getId(), citDTO.getRevision())).build();
    }

    /**
     * Delete an existing citation, associated with a place-rep
     * 
     * @param uriInfo
     * @param placeRepId place-representation identifier
     * @param inRoot {@link RootModel} that contatins the new citation definition
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + CitationMapper.CITATIONS_PATH + "{citationId}")
    @DELETE
    public Response deletePlaceRepCitation(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId,
            @PathParam("citationId") int citationId) throws PlaceDataException {

        long startTime = System.currentTimeMillis();

        // Ensure that the place-rep exists ...
        PlaceRepresentationDTO repDto = getReadableService().getPlaceRepresentationById(placeRepId, null);
        if (repDto == null) {
            log(null, uriInfo.getPath(), "DELETE", "user_error", "place-representation not found", String.valueOf(placeRepId), null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Ensure that the citation exists ...
        boolean found = false;
        Set<CitationDTO> citations = getReadableService().getCitationsByRepId(placeRepId, null);
        for (CitationDTO citation : citations) {
            if (citation.getId() == citationId) {
                found = true;
            }
        }

        if (! found) {
            log(null, uriInfo.getPath(), "DELETE", "user_error", "citation not found", String.valueOf(citationId), null);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Create a dummy citation DTO with the citation-id and place-rep-id
        CitationDTO tempDTO = new CitationDTO(citationId, 0, placeRepId, 0, null, null, null, 0);
        getWritableService().delete(tempDTO, "system");

        log(null, uriInfo.getPath(), "DELETE", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.noContent().build();
    }

    /**
     * Retrieves the list of external-xref links associated with a place-rep
     * 
     * @param uriInfo
     * @param acceptLanguage
     * @param headerAcceptLang
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}" + LinkMapper.LINKS_PATH)
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getPlaceRepExtXref(
            @Context UriInfo uriInfo,
            @PathParam("id") int placeRepId) throws PlaceDataException {

        RootModel    root = new RootModel();
        List<LinkModel>   links = new ArrayList<LinkModel>();
        long startTime = System.currentTimeMillis();

        LinkMapper linkMapper = new LinkMapper(getReadableService());

        List<ExternalXrefDTO> extLinks = getReadableService().getExtXrefByRepId(placeRepId);
        for (ExternalXrefDTO extLink : extLinks) {
            links.add(linkMapper.createExtXrefLinkFromDTO(extLink, getPath(uriInfo)));
        }
        root.setLinks(links);

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).build();
    }
}
