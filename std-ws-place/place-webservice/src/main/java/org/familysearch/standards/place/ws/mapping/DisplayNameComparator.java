package org.familysearch.standards.place.ws.mapping;

import java.util.Comparator;

import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;

public class DisplayNameComparator implements Comparator<PlaceRepresentationModel> {

	@Override
	public int compare( PlaceRepresentationModel o1, PlaceRepresentationModel o2 ) {
		return o1.getDisplayName().getName().compareToIgnoreCase( o2.getDisplayName().getName() );
	}

}
