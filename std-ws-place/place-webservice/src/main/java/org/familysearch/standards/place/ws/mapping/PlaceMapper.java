package org.familysearch.standards.place.ws.mapping;

import java.util.ArrayList;
import java.util.List;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.core.logging.Logger;
import org.familysearch.standards.place.PlaceName;
import org.familysearch.standards.place.data.InvalidPlaceDataException;
import org.familysearch.standards.place.data.PlaceDTO;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.PlaceNameDTO;
import org.familysearch.standards.place.data.PlaceRepresentationDTO;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.TypeCategory;
import org.familysearch.standards.place.ws.model.LocalizedNameDescModel;
import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.model.PlaceModel;
import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.familysearch.standards.place.ws.model.VariantModel;


/**
 * Utility class to map between {@link PlaceDTO} and {@link PlaceModel} types.
 * 
 * @author dshellman
 *
 */
public class PlaceMapper {

    private static final Logger             logger = new Logger( PlaceMapper.class );

    public static final String				PLACE_PATH = "";

	private ReadablePlaceDataService		dataService;
	private PlaceRepresentationMapper		repMapper;
	private TypeMapper						typeMapper;

	public PlaceMapper( ReadablePlaceDataService theService ) {
		dataService = theService;
		repMapper = new PlaceRepresentationMapper( dataService );
		typeMapper = new TypeMapper( dataService );
	}

    /**
     * Transform a {@link org.familysearch.standards.place.data.PlaceDTO} into a
     * {@link org.familysearch.standards.place.ws.model.PlaceModel} instance, which is
     * a marshal-able value required by a web service.
     * 
     * @param place {@link org.familysearch.standards.place.data.PlaceDTO} instance
     * @param locale locale associated with the data
     * @param path URL path used in creating links
     * 
     * @return {@link org.familysearch.standards.place.ws.model.PlaceModel} instance
     */
	public PlaceModel createModelFromDTO( PlaceDTO placeDto, StdLocale locale, String path ) {
	    PlaceModel                      model = new PlaceModel();
	    List<PlaceRepresentationDTO>    reps;
	    List<PlaceRepresentationModel>  repModels = new ArrayList<PlaceRepresentationModel>();
	    List<VariantModel>              variants = new ArrayList<VariantModel>();

	    model.setId( placeDto.getId() );
	    if ( placeDto.getStartYear() == null  ||  placeDto.getStartYear() != Integer.MIN_VALUE ) {
	        //This ensures that if there really isn't a min year, it'll be null in the model.
	        model.setFromYear( placeDto.getStartYear() );
	    }
	    if ( placeDto.getEndYear() == null  ||  placeDto.getEndYear() != Integer.MAX_VALUE ) {
	        //This ensures that if there really isn't a max year, it'll be null in the model.
	        model.setToYear( placeDto.getEndYear() );
	    }
	    for ( PlaceNameDTO nameDto : placeDto.getVariants() ) {
	        VariantModel                    variant = new VariantModel();
	        NameModel                       nameModel = new NameModel();
	        TypeModel                       typeModel = new TypeModel();
	        LocalizedNameDescModel          nameDesc = new LocalizedNameDescModel();
	        List<LocalizedNameDescModel>    nameList = new ArrayList<LocalizedNameDescModel>();
	        PlaceName name = new PlaceName( nameDto, dataService );

	        nameModel.setName( name.getName().get() );
	        nameModel.setLocale( name.getName().getLocale().toString() );
	        typeModel.setCode( name.getType().getCode() );
	        typeModel.setId( name.getType().getId() );
	        nameDesc.setLocale( locale.toString() );
	        nameDesc.setName( name.getType().getName( locale ).get() );
	        nameDesc.setDescription( name.getType().getDescription( locale ).get() );
	        nameList.add( nameDesc );
	        typeModel.setName( nameList );
	        variant.setId( name.getId() );
	        variant.setName( nameModel );
	        variant.setType( typeModel );

	        variants.add( variant );
	    }

	    model.setVariants( variants );

	    // Add the place's reps, including the unpublished ones
	    try {
            reps = dataService.getPlaceRepresentationsByPlaceId(placeDto.getId(), placeDto.getCreatedUsingVersion(), false);
            for (PlaceRepresentationDTO rep : reps) {
                repModels.add(repMapper.createModelFromDTO(rep, locale, path, false, true));
            }
        } catch (PlaceDataException e) {
            logger.warn("Unable to retrieve place's reps ...", e);
        }

	    model.setReps( repModels );

	    return model;
	}

    /**
     * Transform a {@link org.familysearch.standards.place.ws.model.PlaceModel} into a
     * {@link org.familysearch.standards.place.data.PlaceDTO} instance, which is
     * recognizable by all of the back-end services.
     * 
     * @param model {@link org.familysearch.standards.place.ws.model.PlaceModel} instance
     * 
     * @return {@link org.familysearch.standards.place.data.PlaceDTO} instance
     */
	public PlaceDTO createDTOFromModel( PlaceModel model ) throws PlaceDataException {
		PlaceDTO				dto;
		List<PlaceNameDTO>		names;
		List<VariantModel>		modelNames;
		Integer					id;

		modelNames = model.getVariants();
		names = createNameDTOFromVariantModel( modelNames );
		id = model.getId();
		if ( id == null ) {
			id = -1;
		}

		dto = new PlaceDTO( id, names, model.getFromYear(), model.getToYear(), -1, null );

		return dto;
	}

    /**
     * Transform a list of {@link org.familysearch.standards.place.ws.model.VariantModel}
     * into a list of {@link org.familysearch.standards.place.data.PlaceNameDTO} instances,
     * which are recognizable by all of the back-end services.
     * 
     * @param modelNames {@link org.familysearch.standards.place.ws.model.VariantModel} list
     * 
     * @return {@link org.familysearch.standards.place.data.PlaceNameDTO} list
     */
	public List<PlaceNameDTO> createNameDTOFromVariantModel( List<VariantModel> modelNames ) throws PlaceDataException {
		List<PlaceNameDTO>		names = new ArrayList<PlaceNameDTO>( modelNames.size() );

		for ( VariantModel model : modelNames ) {
			PlaceNameDTO		name;
			Integer				typeId;
			Integer				id;
			NameModel			nameModel;
			String				nameStr;
			String				localeStr = null;

			typeId = typeMapper.getTypeDTOFromModel( model.getType(), TypeCategory.NAME ).getId();
			id = model.getId();
			if ( id == null ) {
				id = -1;
			}
			nameModel = model.getName();
			if ( nameModel != null ) {
				nameStr = nameModel.getName();
				localeStr = nameModel.getLocale();
			}
			else {
				if ( id == -1 ) {
					throw new InvalidPlaceDataException( "No variant name specified." );
				}
				else {
					nameStr = ""; //Don't care, since they specified the id, it can be pulled from the backend.
				}
			}
			if ( localeStr == null ) {
				localeStr = "en"; //TODO:  fix this
			}
			name = new PlaceNameDTO( id, nameStr, localeStr, typeId );
			names.add( name );
		}

		return names;
	}
}
