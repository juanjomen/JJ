package org.familysearch.standards.place.ws.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.mapping.TypeMapper;
import org.familysearch.standards.place.ws.model.RootModel;
import org.familysearch.standards.place.ws.model.TypeModel;


/**
 * The "Generic-Type" class defines the basic operations for all of the different
 * types (place, name, attribute, citation, ext-xref and resolution), and forms
 * the base class for those implementations.
 * 
 * @author dshellman, wjohnson000
 */
public abstract class WebServiceGenericType extends WebServiceBase {

	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServiceGenericType() { }

	/**
	 * Each class will specify the "TypeCategory" for its end-points.
	 * @return
	 */
	abstract TypeCategory getTypeCat();


    /**
     * Retrieves the list of published types
     * 
     * @param uriInfo
     * @param acceptLanguage
     * @param headerAcceptLang
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    public Response getAllTypes(UriInfo uriInfo, String acceptLanguage, String headerAcceptLang) throws PlaceDataException {
        RootModel        root = new RootModel();
        List<TypeModel>  outTypes = new ArrayList<TypeModel>();
        long startTime = System.currentTimeMillis();
        StdLocale locale = getLocale(acceptLanguage, headerAcceptLang);

        TypeMapper typeMapper = new TypeMapper(getReadableService());
        Set<TypeDTO> theTypes = getReadableService().getAllTypes(getTypeCat());
        for (TypeDTO type : theTypes) {
            if (type.isPublished()) {
                outTypes.add(typeMapper.createModelFromTypeDTO(type, locale, getPath(uriInfo), getTypeCat()));
            }
        }

        // Sort the list
        Collections.sort(outTypes);
        root.setTypes(outTypes);

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).build();
    }

    /**
     * Create a new type of the appropriate category.
     * 
     * @param uriInfo
     * @param inRoot RootModel containing the new type
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    public Response createType(UriInfo uriInfo, RootModel inRoot) throws PlaceDataException {

        RootModel  outRoot = new RootModel();
        long       startTime = System.currentTimeMillis();

        TypeMapper typeMapper = new TypeMapper(getReadableService());
        TypeModel  typeModel = inRoot.getType();
        TypeDTO    typeDto = typeMapper.createTypeDTOFromModel(typeModel, getTypeCat());

        typeDto = getWritableService().create(getTypeCat(), typeDto, "system");
        outRoot.setType(typeMapper.createModelFromTypeDTO(typeDto, null, getPath(uriInfo), getTypeCat()));
        
        log(null, uriInfo.getPath(), "POST", "time", "" + (System.currentTimeMillis() - startTime));

        String resourcePath = getPath(uriInfo) + TypeMapper.getRelativePath(getTypeCat()) + typeDto.getId();
        return Response.created(URI.create(resourcePath)).entity(outRoot).tag(getEntityTag(typeDto.getId(), 0)).build();
    }

    /**
     * Retrieve details about a single type
     * 
     * @param uriInfo
     * @param typeId the type identifier
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    public Response getTypeById(UriInfo uriInfo, int typeId) throws PlaceDataException {
        RootModel  root = new RootModel();
        long startTime = System.currentTimeMillis();

        TypeMapper typeMapper = new TypeMapper(getReadableService());
        TypeDTO typeDto = getReadableService().getTypeById(getTypeCat(), typeId);
        root.setType(typeMapper.createModelFromTypeDTO(typeDto, null, getPath(uriInfo), getTypeCat()));

        log(null, uriInfo.getPath(), "GET", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(root).expires(this.getExpireDate()).tag(getEntityTag(typeDto.getId(), 0)).build();
    }

    /**
     * update an existing type
     * 
     * @param uriInfo
     * @param inRoot RootModel containing the existing type
     * 
     * @return Returns the response.
     * @throws PlaceDataException 
     */
    public Response updateType(UriInfo uriInfo, RootModel inRoot) throws PlaceDataException {

        RootModel  outRoot = new RootModel();
        long       startTime = System.currentTimeMillis();

        TypeMapper typeMapper = new TypeMapper(getReadableService());
        TypeModel  typeModel = inRoot.getType();
        TypeDTO    typeDto = typeMapper.createTypeDTOFromModel(typeModel, getTypeCat());

        typeDto = getWritableService().update(getTypeCat(), typeDto, "system");
        outRoot.setType(typeMapper.createModelFromTypeDTO(typeDto, null, getPath(uriInfo), getTypeCat()));

        log(null, uriInfo.getPath(), "PUT", "time", "" + (System.currentTimeMillis() - startTime));

        return Response.ok(outRoot).tag(getEntityTag(typeDto.getId(), 0)).build();
    }
}
