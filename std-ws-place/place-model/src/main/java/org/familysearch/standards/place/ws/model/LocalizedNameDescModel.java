package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@XmlType(propOrder={ "name", "description" })
public class LocalizedNameDescModel {

	private String				locale;
	private String				name;
	private String				desc;

	public LocalizedNameDescModel() {}


	@XmlAttribute(name="locale")
	@JsonProperty("locale")
	public String getLocale() {
		return locale;
	}

	@JsonProperty("locale")
	public void setLocale( String theLocale ) {
		locale = theLocale;
	}

	@XmlElement(name="name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName( String theName ) {
		name = theName;
	}

	@XmlElement(name="desc")
	@JsonProperty("desc")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getDescription() {
		return desc;
	}

	@JsonProperty("desc")
	public void setDescription( String theDesc ) {
		desc = theDesc;
	}
}
