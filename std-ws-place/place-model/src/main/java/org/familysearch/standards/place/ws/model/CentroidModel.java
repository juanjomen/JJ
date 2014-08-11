package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
public class CentroidModel {

	private Double			latitude;
	private Double			longitude;

	public CentroidModel() {}

	@XmlElement(name="latitude")
	@JsonProperty("latitude")
	public Double getLatitude() {
		return latitude;
	}

	@JsonProperty("latitude")
	public void setLatitude( Double theLatitude ) {
		latitude = theLatitude;
	}

	@XmlElement(name="longitude")
	@JsonProperty("longitude")
	public Double getLongitude() {
		return longitude;
	}

	@JsonProperty("longitude")
	public void setLongitude( Double theLongitude ) {
		longitude = theLongitude;
	}
}
