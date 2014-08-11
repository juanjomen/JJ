package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
public class JurisdictionModel {

	private int						id;
	private Integer					placeId;
	private String					name;
	private String					locale;
	private JurisdictionModel		parent;
	private LinkModel				selfLink;

	public JurisdictionModel() {}


	@XmlAttribute(name="id")
	@JsonProperty("id")
	public int getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId( int theId ) {
		id = theId;
	}

	@XmlAttribute(name="place")
	@JsonProperty("place")
	public Integer getPlace() {
		return placeId;
	}

	@JsonProperty("place")
	public void setPlace( Integer theId ) {
		placeId = theId;
	}

	@XmlAttribute(name="name")
	@JsonProperty("name")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName( String theName ) {
		name = theName;
	}

	@XmlAttribute(name="locale")
	@JsonProperty("locale")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getLocale() {
		return locale;
	}

	@JsonProperty("locale")
	public void setLocale( String theLocale ) {
		locale = theLocale;
	}

	@XmlElement(name="jurisdiction")
	@JsonProperty("jurisdiction")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public JurisdictionModel getParent() {
		return parent;
	}

	@JsonProperty("jurisdiction")
	public void setParent( JurisdictionModel theParent ) {
		parent = theParent;
	}

	@XmlElement(name="link", namespace=LinkModel.ATOM_NAMESPACE)
	@JsonProperty("link")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public LinkModel getSelfLink() {
		return selfLink;
	}

	@JsonProperty("link")
	public void setSelfLink( LinkModel link ) {
		selfLink = link;
	}
}
