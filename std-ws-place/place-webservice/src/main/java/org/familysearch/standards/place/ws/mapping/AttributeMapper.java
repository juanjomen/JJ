package org.familysearch.standards.place.ws.mapping;

import java.util.ArrayList;
import java.util.List;

import org.familysearch.standards.place.data.AttributeDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.AttributeModel;
import org.familysearch.standards.place.ws.model.LinkModel;


/**
 * Utility class to map between {@link AttributeDTO} and {@link AttributeModel} types.
 * 
 * @author wjohnson000
 *
 */
public class AttributeMapper {

    /** URL paths for various entity types */
	public static final String ATTRIBUTES_PATH = "/attributes/";

    /** Data service for doing additional operations for place-type-groups */
    private ReadablePlaceDataService dataService;
    private TypeMapper typeMapper;


    /**
	 * Simple constructor
	 * 
	 * @param theService a "ReadablePlaceDataService"
	 */
	public AttributeMapper(ReadablePlaceDataService theService) {
	    this.dataService = theService;
	    this.typeMapper  = new TypeMapper(theService);
	}


	/**
     * Convert a {@link AttributeDTO} instance into a {@link AttributeModel} instance,
     * suitable for returning in a web response.
     * 
     * @param dto {@link AttributeDTO} instance
     * @param path base URL path
     * @return {@link AttributeModel} instance
     * 
     * @throws PlaceDataException 
	 */
	public AttributeModel createModelFromDTO(AttributeDTO dto, String path) throws PlaceDataException {
        List<LinkModel> links = new ArrayList<LinkModel>();
        links.add(getSelfLink(dto, path));
        links.add(getRepLink(dto, path));
        links.add(getTypeLink(dto, path));

        AttributeModel model = new AttributeModel();
        TypeDTO typeDto = dataService.getTypeById(TypeCategory.ATTRIBUTE, dto.getTypeId());

	    model.setId(dto.getId());
	    model.setRepId(dto.getRepId());
	    model.setType(typeMapper.createModelFromTypeDTO(typeDto, null, path, TypeCategory.ATTRIBUTE));
	    model.setYear(dto.getYear());
	    model.setValue(dto.getValue());
	    model.setLinks(links);

	    return model;
	}

    /**
     * Convert a {@link AttributeModel} instance into a {@link AttributeDTO} instance.
     * 
     * @param dto {@link AttributeModel} instance
     * @return {@link AttributeDTO} instance
     * 
     * @throws PlaceDataException 
     */
    public AttributeDTO createDTOFromModel(AttributeModel model) throws PlaceDataException {
        int modelId = (model.getId() == null) ? 0 : model.getId();
        int typeId = (model.getType() == null  ||  model.getType().getId() == null) ? 0 : model.getType().getId();
        AttributeDTO dto = new AttributeDTO(
                modelId, model.getRepId(), typeId, model.getYear(), model.getValue(), 0);
        return dto;
    }

    /**
     * Create a link to "self"
     * @param dto attribute DTO
     * @param path base URL path
     * @return LinkModel to "self"
     */
    private LinkModel getSelfLink(AttributeDTO dto, String path) {
        LinkModel link = new LinkModel();
        link.setRel("self");
        link.setHref(path + PlaceRepresentationMapper.REPS_PATH + dto.getRepId() + ATTRIBUTES_PATH + dto.getId());

        return link;
    }

    /**
	 * Create a link to the place-rep associated with this attribute 
	 * @param dto attribute DTO
     * @param path base URL path
	 * @return LinkModel to place-rep
	 */
	private LinkModel getRepLink(AttributeDTO dto, String path) {
	    LinkModel link = new LinkModel();
	    link.setRel("via");
	    link.setHref(path + PlaceRepresentationMapper.REPS_PATH + dto.getRepId());

	    return link;
	}

    /**
     * Create a link to the [attribute] type associated with this attribute 
     * @param dto attribute DTO
     * @param path base URL path
     * @return LinkModel to attribute type
     */
    private LinkModel getTypeLink(AttributeDTO dto, String path) {
        LinkModel link = new LinkModel();
        link.setRel("type");
        link.setHref(path + TypeMapper.ATTRIBUTE_TYPE_PATH + dto.getTypeId());

        return link;
    }
}
