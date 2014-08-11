package org.familysearch.standards.place.ws.mapping;

import java.util.ArrayList;
import java.util.List;

import org.familysearch.standards.place.data.CitationDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.CitationModel;
import org.familysearch.standards.place.ws.model.LinkModel;


/**
 * Utility class to map between {@link CitationDTO} and {@link CitationModel} types.
 * 
 * @author wjohnson000
 *
 */
public class CitationMapper {

    /** URL paths for various entity types */
	public static final String CITATIONS_PATH = "/citations/";

    /** Data service for doing additional operations for place-type-groups */
    private ReadablePlaceDataService dataService;
    private TypeMapper typeMapper;


	/**
	 * Simple constructor
	 * 
	 * @param theService a "ReadablePlaceDataService"
	 */
	public CitationMapper(ReadablePlaceDataService theService) {
	    this.dataService = theService;
        this.typeMapper  = new TypeMapper(theService);
	}

	/**
     * Convert a {@link CitationDTO} instance into a {@link CitationModel} instance,
     * suitable for returning in a web response.
     * 
     * @param dto {@link CitationDTO} instance
     * @param path base URL path
     * @return {@link CitationModel} instance
     * 
	 * @throws PlaceDataException 
	 */
	public CitationModel createModelFromDTO(CitationDTO dto, String path) throws PlaceDataException {
        List<LinkModel> links = new ArrayList<LinkModel>();
        links.add(getSelfLink(dto, path));
        links.add(getRepLink(dto, path));
        links.add(getTypeLink(dto, path));
        links.add(getSourceLink(dto, path));

        CitationModel model = new CitationModel();
        TypeDTO typeDto = dataService.getTypeById(TypeCategory.CITATION, dto.getTypeId());

	    model.setId(dto.getId());
	    model.setRepId(dto.getRepId());
	    model.setType(typeMapper.createModelFromTypeDTO(typeDto, null, path, TypeCategory.CITATION));
	    model.setSourceId(dto.getSourceId());
	    model.setDescription(dto.getDescription());
	    model.setSourceRef(dto.getSourceRef());
	    model.setCitDate(dto.getCitationDate());
	    model.setLinks(links);

	    return model;
	}

    /**
     * Convert a {@link CitationModel} instance into a {@link CitationDTO} instance.
     * 
     * @param dto {@link CitationModel} instance
     * @return {@link CitationDTO} instance
     * 
     * @throws PlaceDataException 
     */
	public CitationDTO createDTOFromModel(CitationModel model) throws PlaceDataException {
	    int modelId = (model.getId() == null) ? 0 : model.getId();
	    int typeId = (model.getType() == null  ||  model.getType().getId() == null) ? 0 : model.getType().getId();
	    CitationDTO dto = new CitationDTO(
	            modelId, model.getSourceId(), model.getRepId(), typeId,
	            model.getCitDate(), model.getDescription(), model.getSourceRef(), 0);
	    return dto;
	}

    /**
     * Create a link to "self"
     * @param dto citation DTO
     * @param path base URL path
     * @return LinkModel to "self"
     */
    private LinkModel getSelfLink(CitationDTO dto, String path) {
        LinkModel link = new LinkModel();
        link.setRel("self");
        link.setHref(path + PlaceRepresentationMapper.REPS_PATH + dto.getRepId() + CITATIONS_PATH + dto.getId());

        return link;
    }

    /**
	 * Create a link to the place-rep associated with this citation 
	 * @param dto citation DTO
     * @param path base URL path
	 * @return LinkModel to place-rep
	 */
	private LinkModel getRepLink(CitationDTO dto, String path) {
	    LinkModel link = new LinkModel();
	    link.setRel("via");
	    link.setHref(path + PlaceRepresentationMapper.REPS_PATH + dto.getRepId());

	    return link;
	}

    /**
     * Create a link to the [citation] type associated with this citation 
     * @param dto citation DTO
     * @param path base URL path
     * @return LinkModel to citation type
     */
    private LinkModel getTypeLink(CitationDTO dto, String path) {
        LinkModel link = new LinkModel();
        link.setRel("type");
        link.setHref(path + TypeMapper.CITATION_TYPE_PATH + dto.getTypeId());

        return link;
    }

    /**
     * Create a link to the source associated with this citation 
     * @param dto citation DTO
     * @param path base URL path
     * @return LinkModel to source
     */
    private LinkModel getSourceLink(CitationDTO dto, String path) {
        LinkModel link = new LinkModel();
        link.setRel("describes");
        link.setHref(path + SourceMapper.SOURCES_PATH + dto.getSourceId());

        return link;
    }
}
