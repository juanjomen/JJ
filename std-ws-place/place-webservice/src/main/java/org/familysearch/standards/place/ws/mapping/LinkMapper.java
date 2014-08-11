package org.familysearch.standards.place.ws.mapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.familysearch.standards.place.data.ExternalXrefDTO;
import org.familysearch.standards.place.data.InvalidPlaceDataException;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.data.TypeDTO;
import org.familysearch.standards.place.ws.model.LinkModel;


/**
 * Utility class to map between {@link ExtXrefDTO} and {@link LinkModel} types.
 * 
 * @author wjohnson000
 *
 */
public class LinkMapper {

    /** URL paths for various entity types */
	public static final String LINKS_PATH = "/xrefs/";

	/** Data service for doing additional operations for place-type-groups */
    private ReadablePlaceDataService dataService;


	/**
	 * Simple constructor
	 */
	public LinkMapper(ReadablePlaceDataService theService) {
	    this.dataService = theService;
	}

	/**
     * Convert a {@link ExtXrefDTO} instance into a {@link LinkModel} instance,
     * suitable for returning in a web response.
     * 
     * @param dto {@link ExtXrefDTO} instance
     * @param path base URL path
     * @return {@link LinkModel} instance
     * 
	 * @throws PlaceDataException 
	 */
	public LinkModel createExtXrefLinkFromDTO(ExternalXrefDTO dto, String path) throws PlaceDataException {
        LinkModel model = new LinkModel();

        try {
            TypeDTO typeDto = dataService.getTypeById(TypeCategory.EXT_XREF, dto.getTypeId());
            model.setRel("related");
            model.setHref(path + LINKS_PATH + typeDto.getCode() + "." + URLEncoder.encode(dto.getExternalKey(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new InvalidPlaceDataException("Unable to encode the key: " + dto.getExternalKey());
        }

	    return model;
	}

    /**
     * Convert a {@link ExtXrefDTO} instance into a {@link LinkModel} instance,
     * suitable for returning in a web response.
     * 
     * @param dto {@link ExtXrefDTO} instance
     * @param path base URL path
     * @return {@link LinkModel} instance
     * 
     * @throws PlaceDataException 
     */
    public LinkModel createPlaceRepLinkFromDTO(ExternalXrefDTO dto, String path) throws PlaceDataException {
        LinkModel model = new LinkModel();

        model.setRel("item");
        model.setHref(path + PlaceRepresentationMapper.REPS_PATH + dto.getRepId());

        return model;
    }

}
