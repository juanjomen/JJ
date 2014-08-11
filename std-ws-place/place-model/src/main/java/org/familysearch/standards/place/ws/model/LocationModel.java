package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
public class LocationModel {

	private CentroidModel		centroid;

	public LocationModel() {}

	@XmlElement(name="centroid")
	@JsonProperty("centroid")
	public CentroidModel getCentroid() {
		return centroid;
	}

	@JsonProperty("centroid")
	public void setCentroid( CentroidModel theCentroid ) {
		centroid = theCentroid;
	}
}
