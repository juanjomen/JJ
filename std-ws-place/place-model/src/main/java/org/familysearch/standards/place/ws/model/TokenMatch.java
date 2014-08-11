package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlType(propOrder={ "token", "nameMatch" })
public class TokenMatch {

	private String			token;
	private NameModel		nameMatch;
	private Integer			repId;

	public TokenMatch() {}

	@XmlElement(name="token")
	@JsonProperty("token")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getToken() {
		return token;
	}

	@JsonProperty("token")
	public void setToken( String theToken ) {
		token = theToken;
	}

	@XmlAttribute(name="id")
	@JsonProperty("id")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getId() {
		return repId;
	}

	@JsonProperty("id")
	public void setId( Integer theId ) {
		repId = theId;
	}

	@XmlElement(name="name")
	@JsonProperty("name")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public NameModel getNameMatch() {
		return nameMatch;
	}

	@JsonProperty("name")
	public void setNameMatch( NameModel name ) {
		nameMatch = name;
	}
}
