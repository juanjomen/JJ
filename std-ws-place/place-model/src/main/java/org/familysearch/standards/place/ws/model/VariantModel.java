package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
public class VariantModel {

	private Integer			id;
	private TypeModel		type;
	private NameModel		name;

	public VariantModel() {}

	@XmlAttribute(name="id")
	@JsonProperty("id")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId( Integer theId ) {
		id = theId;
	}

	@XmlElement(name="type")
	@JsonProperty("type")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public TypeModel getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType( TypeModel theType ) {
		type = theType;
	}

	@XmlElement(name="name")
	@JsonProperty("name")
	public NameModel getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName( NameModel theName ) {
		name = theName;
	}
}
