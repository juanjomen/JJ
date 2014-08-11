package org.familysearch.standards.place.ws.mapping;

import java.util.ArrayList;
import java.util.List;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.place.PlaceRepresentation;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.ws.model.JurisdictionModel;
import org.familysearch.standards.place.ws.model.LinkModel;

/**
 * This mapper is useful to put common mapping methods that other mappers commonly use.
 * Generally speaking, if two or more mappers need to share the same method or mapping
 * capability, they should add that to this class.
 * 
 * @author dshellman
 */
public class CommonMapper {

    private ReadablePlaceDataService   dataService;

    public CommonMapper( ReadablePlaceDataService theService ) {
        dataService = theService;
    }

    public ReadablePlaceDataService getDataService() {
        return dataService;
    }

    /**
     * Convert a {@link org.familysearch.standards.place.ws.model.JurisdictionModel}
     * instance into an array of IDs reflecting the jurisdiction chain.
     * 
     * @param model {@link org.familysearch.standards.place.ws.model.JurisdictionModel} instance
     * 
     * @return integer array of identifiers
     */
    public int[] createJurisdictionChainFromJurisdictionModel( JurisdictionModel model ) {
        List<Integer>           chain = new ArrayList<Integer>();
        int[]                   chainArray = new int[0];
        JurisdictionModel       child;

        if (model != null) {
            chain.add(  model.getId() );
            child = model.getParent();
            while ( child != null ) {
                chain.add( child.getId() );
                child = child.getParent();
            }

            chainArray = new int[ chain.size() ];
            for ( int i = 0; i < chainArray.length; i++ ) {
                chainArray[ i ] = chain.get( i );
            }
        }

        return chainArray;
    }

    /**
     * Create a {@link org.familysearch.standards.place.ws.model.JurisdictionModel}
     * instance from a {@link org.familysearch.standards.place.PlaceRepresentation}
     * instance, which has a reference to its parent.
     * 
     * @param parent {@link org.familysearch.standards.place.PlaceRepresentation} instance
     * @param locale locale for results
     * @param path URL path for embedded links
     * 
     * @return {@link org.familysearch.standards.place.ws.model.JurisdictionModel} instance
     */
    public JurisdictionModel createJurisdictionModelFromJurisdictionChain( PlaceRepresentation parent, StdLocale locale, String path ) {
        JurisdictionModel           model = new JurisdictionModel();
        PlaceRepresentation[]       parents;
        LinkModel                   link = new LinkModel();

        link.setRel( "parent" );
        link.setHref( path + PlaceRepresentationMapper.REPS_PATH + parent.getId() );

        model.setId( parent.getId() );
        model.setPlace( parent.getPlaceId() );
        model.setName( parent.getDisplayName( locale ).get() );
        model.setLocale( parent.getDisplayName( locale ).getLocale().toString() );
        model.setSelfLink( link );
        parents = parent.getJurisdictionChain();
        if ( parents.length > 1 ) {
            model.setParent( createJurisdictionModelFromJurisdictionChain( parents[ 1 ], locale, path ) );
        }

        return model;
    }

}
