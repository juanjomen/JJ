package org.familysearch.standards.place.ws.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.AttributeType;
import org.familysearch.standards.place.CitationType;
import org.familysearch.standards.place.ExtXrefType;
import org.familysearch.standards.place.GenericType;
import org.familysearch.standards.place.PlaceNameType;
import org.familysearch.standards.place.PlaceType;
import org.familysearch.standards.place.ResolutionType;
import org.familysearch.standards.place.data.InvalidPlaceDataException;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.GroupDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.LinkModel;
import org.familysearch.standards.place.ws.model.LocalizedNameDescModel;
import org.familysearch.standards.place.ws.model.TypeModel;


/**
 * Utility class to map between:
 * <br/>
 * {@link TypeDTO} and {@link TypeModel} types.<br/>
 * {@link PlaceType} and {@link TypeModel} types.<br/>
 * {@link PLaceNameType} and {@link TypeModel} types.<br/>
 * {@link AttributeType} and {@link TypeModel} types.<br/>
 * {@link CitationType} and {@link TypeModel} types.<br/>
 * {@link ResolutionType} and {@link TypeModel} types.<br/>
 * {@link ExtXrefType} and {@link TypeModel} types.<br/>
 *  * 
 * @author dshaellman, wjohnson000
 *
 */
public class TypeMapper {

    /** URL paths for various entity types */
	public static final String PLACE_TYPE_PATH = "place-types/";
	public static final String NAME_TYPE_PATH = "name-types/";
    public static final String ATTRIBUTE_TYPE_PATH = "attribute-types/";
    public static final String CITATION_TYPE_PATH = "citation-types/";
    public static final String EXT_XREF_TYPE_PATH = "xref-types/";
    public static final String RESOLUTION_TYPE_PATH = "resolution-types/";

	/** Data service for doing additional operations for place-type-groups */
	private ReadablePlaceDataService dataService;


	/**
	 * Constructor requires a "ReadablePlaceDataService" for some of the place-type-group
	 * operations.
	 * 
	 * @param theService a "ReadablePlaceDataService"
	 */
	public TypeMapper(ReadablePlaceDataService theService) {
		dataService = theService;
	}

    /**
     * Convert a {@link TypeDTO} instance into a {@link TypeModel} instance, suitable for
     * returning in a web response.  Note: the given input type MUST have a type of PLACE,
     * but that fact isn't verifiable from this method.
     * <p/>
     * NOTE: The returned type will have the name and description for ALL locales. 
     * 
     * @param type a {@link TypeDTO} instance
     * @param path base URL path
     * @return a {@link TypeModel} instance
     */
    public TypeModel createModelFromPlaceTypeDTO(TypeDTO type, Set<GroupDTO> groups, String path) {
        TypeModel model = this.createModelFromTypeDTO(type, null, path, TypeCategory.PLACE);

        if ( groups != null && groups.size() > 0 ) {
            List<LinkModel> groupLinks = new ArrayList<>();
            for ( GroupDTO dto : groups ) {
                LinkModel link = new LinkModel();
                link.setTitle( dto.getName("en") );
                link.setRel( "group" );
                link.setHref( path + GroupMapper.GROUP_TYPE_PATH + dto.getId() );

                groupLinks.add( link );
            }
            model.setGroups( groupLinks );
        }

        return model;
    }

    /**
     * This is the main conversion method to get a "TypeDTO" from a "GenericType".
     * <p/>
     * NOTE: The returned type will have a single name and description, the one that
     * matches or is closest to the given StdLocale.  If the StdLocale is null, then
     * return all names and descriptions.
     * 
     * @param typeDTO a TypeDTO
     * @param locale locale value
     * @param path path for the SELF link
     * @param typeCat type category of this
     * @return a {@link TypeModel} instance
     */
    public TypeModel createModelFromTypeDTO(TypeDTO typeDTO, StdLocale locale, String path, TypeCategory typeCat) {
        // Generate the correct TYPE to work on ...
        GenericType type = null;
        switch(typeCat) {
            case ATTRIBUTE:
                type = new AttributeType(typeDTO); break;
            case CITATION:
                type = new CitationType(typeDTO); break;
            case EXT_XREF:
                type = new ExtXrefType(typeDTO); break;
            case NAME:
                type = new PlaceNameType(typeDTO); break;
            case PLACE:
                type = new PlaceType(typeDTO); break;
            case RESOLUTION:
                type = new ResolutionType(typeDTO); break;
        }

        if ( type == null ) {
        	throw new RuntimeException( "Invalid/unknown type category specified during type mapping: " + typeCat );
        }

        return createModelFromType(type, locale, path, typeCat);
    }

    /**
     * This is the main conversion method to get a "TypeModel" from a "GenericType".
     * <p/>
     * NOTE: The returned type will have a single name and description, the one that
     * matches or is closest to the given StdLocale.  If the StdLocale is null, then
     * return all names and descriptions.
     * 
     * @param type a subclass of the "GenericType"
     * @param locale locale value
     * @param path path for the SELF link
     * @param typeCat type category of this
     * @return a {@link TypeModel} instance
     */
    public TypeModel createModelFromType(GenericType type, StdLocale locale, String path, TypeCategory typeCat) {
        TypeModel                       model = new TypeModel();
        List<LocalizedNameDescModel>    nameList = new ArrayList<>();
        LocalizedNameDescModel          nameDesc = new LocalizedNameDescModel();
        LinkModel                       selfLink = new LinkModel();

        // Null locale --> return values for ALL locales
        if (locale == null) {
            Set<String> allLocales = type.getLocales();
            for (String aLocale : allLocales) {
                nameDesc = new LocalizedNameDescModel();
                nameDesc.setLocale(aLocale);
                nameDesc.setName(type.getName(new StdLocale(aLocale)).get());
                nameDesc.setDescription(type.getDescription(new StdLocale(aLocale)).get());
                nameList.add(nameDesc);
            }
        } else {
            nameDesc.setLocale(type.getName(locale).getLocale().toString());
            nameDesc.setName(type.getName(locale).get());
            nameDesc.setDescription(type.getDescription(locale).get());
            nameList.add(nameDesc);
        }

        selfLink.setRel("self");
        selfLink.setHref(path + getRelativePath(typeCat) + type.getId());

        model.setId(type.getId());
        model.setCode(type.getCode());
        model.setName(nameList);
        model.setIsPublished(type.isPublished());
        model.setSelfLink(selfLink);

        return model;
    }

    /**
     * Convert a {@link TypeModel} instance into a {@link TypeDTO} instance.  This is
     * applicable for any type.  It'll perform a look-up by both ID and code.
     * 
     * @param model a {@link TypeModel} instance
     * @param typeCat the appropriate type-category definition
     * @return a {@link TypeDTO} instance
     */
    public TypeDTO getTypeDTOFromModel(TypeModel model, TypeCategory typeCat) throws PlaceDataException {
        TypeDTO  typeDto = null;
        String   errorMsg = "No id or code.";

        if (model == null) {
            throw new InvalidPlaceDataException("No " + typeCat + " type specified.");
        }

        if (model.getId() != null) {
            typeDto = dataService.getTypeById(typeCat, model.getId());
            errorMsg = "" + model.getId();
        } else {
            errorMsg = "No id.";
        }

        if (typeDto == null  &&  model.getCode() != null) {
            typeDto = dataService.getTypeByCode(typeCat, model.getCode());
            errorMsg = model.getCode();
        }

        if (typeDto == null) {
            throw new InvalidPlaceDataException("Invalid " + typeCat + " type id or code specified: " + errorMsg);
        }

        return typeDto;
    }

    /**
     * Create a {@link TypeDTO} from a {@link TypeModel) instance.  This is applicable
     * for any type.
     * 
     * @param model {@link TypeModel} instance
     * @param typeCat {@link TypeCategory} value
     * 
     * @return a {@link TypeDTO} instance
     */
    public TypeDTO createTypeDTOFromModel(TypeModel model, TypeCategory typeCat) {
        Map<String,String> names = new HashMap<>();
        Map<String,String> descr = new HashMap<>();

        for (LocalizedNameDescModel nameDesc : model.getLocalizedName()) {
            names.put(nameDesc.getLocale(), nameDesc.getName());
            descr.put(nameDesc.getLocale(), nameDesc.getDescription());
        }

        return new TypeDTO((model.getId()==null) ? 0 : model.getId(), model.getCode(), names, descr, model.isPublished());
    }

    /**
     * Retrieve the relative path for the type base on the type category
     * 
     * @param typeCat {@link TypeCategory} value
     * @return relative path
     */
    public static String getRelativePath(TypeCategory typeCat) {
        String relPath = "";

        switch(typeCat) {
            case ATTRIBUTE:
                relPath += ATTRIBUTE_TYPE_PATH; break;
            case CITATION:
                relPath += CITATION_TYPE_PATH; break;
            case EXT_XREF:
                relPath += EXT_XREF_TYPE_PATH; break;
            case NAME:
                relPath += NAME_TYPE_PATH; break;
            case PLACE:
                relPath += PLACE_TYPE_PATH; break;
            case RESOLUTION:
                relPath += RESOLUTION_TYPE_PATH; break;
        }

        return relPath;
    }
}
