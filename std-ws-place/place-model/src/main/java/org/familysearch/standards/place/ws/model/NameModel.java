package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
public class NameModel {

	private String			name;
	private String			locale;

	public NameModel() {}

	@XmlValue()
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName( String theName ) {
		name = theName;
	}

	@XmlAttribute(name="locale")
	@JsonProperty("locale")
	public String getLocale() {
		return locale;
	}

	@JsonProperty("locale")
	public void setLocale( String theLocale ) {
		locale = theLocale;
	}
}
