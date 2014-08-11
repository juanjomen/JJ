package org.familysearch.standards.place.ws.mapping;

import org.familysearch.standards.place.data.SourceDTO;
import org.familysearch.standards.place.ws.model.SourceModel;


/**
 * Utility class to map between {@link SourceDTO} and {@link SourceModel} types.
 * 
 * @author wjohnson000
 *
 */
public class SourceMapper {

    /** URL paths for various entity types */
	public static final String SOURCES_PATH = "sources/";


	/**
	 * Simple default constructor
	 * 
	 * @param theService a "ReadablePlaceDataService"
	 */
	public SourceMapper() { }


	/**
     * Convert a {@link SourceDTO} instance into a {@link SourceModel} instance,
     * suitable for returning in a web response.
     * 
     * @param dto {@link SourceDTO} instance
     * @param path base URL path
     * @return {@link SourceModel} instance
	 */
	public SourceModel createModelFromDTO(SourceDTO dto, String path) {
        SourceModel model = new SourceModel();

	    model.setId(dto.getId());
	    model.setTitle(dto.getTitle());
	    model.setDescription(dto.getDescription());
	    model.setIsPublished(dto.isPublished());

	    return model;
	}

	/**
	 * Convert a {@link SourceModel} instance into a {@link SourceDTO} instance.
	 * 
	 * @param model {@link SourceModel} instance
	 * @return {@link SourceDTO} instance
	 */
	public SourceDTO createDTOFromModel(SourceModel model) {
	    int id = (model.getId() == null) ? 0 : model.getId();
	    return new SourceDTO(id, model.getTitle(), model.getDescription(), model.isPublished());
	}
}
