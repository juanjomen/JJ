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

import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.SourceDTO;
import org.familysearch.standards.place.ws.mapping.SourceMapper;
import org.familysearch.standards.place.ws.model.RootModel;
import org.familysearch.standards.place.ws.model.SourceModel;


/**
 * The "places" web service provides endpoints to support the searching and retrieval of place and related
 * resources.  The "search" endpoint provides the ability to search for Place Representation entities by
 * using a variety of optional search parameters.  Additional endpoints provide direct access to Place
 * and Place Representation resources, as well as Place Types, Variant Name Types, and Type Groups.
 * 
 * @author dshellman
 */
@Path(WebServiceSource.URL_ROOT+"/" + SourceMapper.SOURCES_PATH)
public class WebServiceSource extends WebServiceBase {

	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServiceSource() { }


    /**
     * Retrieves the list of published sources
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
    public Response getSources( @Context UriInfo uriInfo,
                                  @QueryParam("accept-language") String acceptLanguage,
                                  @HeaderParam("Accept-Language") String headerAcceptLang ) throws PlaceDataException {
        RootModel          root = new RootModel();
        List<SourceModel>  sources = new ArrayList<SourceModel>();

        long startTime = System.currentTimeMillis();

        SourceMapper sourceMapper = new SourceMapper();
        Set<SourceDTO> sourceSet = getReadableService().getAllSources();
        for (SourceDTO source : sourceSet) {
            if (source.isPublished()) {
                sources.add(sourceMapper.createModelFromDTO(source, getPath(uriInfo)));
            }
        }
        root.setSources(sources);

        log( null, uriInfo.getPath(), "GET", "time", "" + ( System.currentTimeMillis() - startTime ) );

        return Response.ok( root ).expires( this.getExpireDate() ).build();
    }

    /**
     * Create a new source entry
     * 
     * @param uriInfo
     * @param inRoot RootModel containing the new source
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @POST
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response createSource(
            @Context UriInfo uriInfo,
            RootModel inRoot) throws PlaceDataException {

        RootModel  outRoot = new RootModel();
        long       startTime = System.currentTimeMillis();

        SourceMapper srcMapper = new SourceMapper();
        SourceModel  srcModel = inRoot.getSource();
        SourceDTO    sourceDto = srcMapper.createDTOFromModel(srcModel);

        sourceDto = getWritableService().create(sourceDto, "system"); 
        outRoot.setSource(srcMapper.createModelFromDTO(sourceDto, getPath(uriInfo)));
        
        log(null, uriInfo.getPath(), "POST", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.created(URI.create(getPath(uriInfo) + SourceMapper.SOURCES_PATH + sourceDto.getId())).entity(outRoot).tag(getEntityTag(sourceDto.getId(), 0)).build();
    }

    /**
     * Retrieve details about a source instance
     * 
     * @param uriInfo
     * @param sourceId the source identifier
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}")
    @GET
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    public Response getSourceById( @Context UriInfo uriInfo,
                                   @PathParam("id") int sourceId ) throws PlaceDataException {
        RootModel  root = new RootModel();
        long       startTime = System.currentTimeMillis();

        SourceMapper sourceMapper = new SourceMapper();
        SourceDTO    sourceDto = getReadableService().getSourceById(sourceId);
        root.setSource(sourceMapper.createModelFromDTO(sourceDto, getPath(uriInfo)));

        log( null, uriInfo.getPath(), "GET", "time", "" + ( System.currentTimeMillis() - startTime ) );

        return Response.ok( root ).expires( this.getExpireDate() ).tag( getEntityTag( sourceDto.getId(), 0 ) ).build();
    }


    /**
     * update an existing source instance
     * 
     * @param uriInfo
     * @param inRoot RootModel containing the existing source
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    @Path("{id}")
    @PUT
    @Produces({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Consumes({RootModel.APPLICATION_XML_PLACES, RootModel.APPLICATION_JSON_PLACES})
    public Response updateSource(
            @Context UriInfo uriInfo,
            RootModel inRoot) throws PlaceDataException {

        RootModel  outRoot = new RootModel();
        long       startTime = System.currentTimeMillis();

        SourceMapper sourceMapper = new SourceMapper();
        SourceModel  sourceModel = inRoot.getSource();
        SourceDTO    sourceDto = sourceMapper.createDTOFromModel(sourceModel);

        sourceDto = getWritableService().update(sourceDto, "system");
        outRoot.setSource(sourceMapper.createModelFromDTO(sourceDto, getPath(uriInfo)));
        
        log(null, uriInfo.getPath(), "PUT", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(outRoot).tag(getEntityTag(sourceDto.getId(), 0)).build();
    }
}
