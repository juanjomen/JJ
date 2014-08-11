package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement(name="link", namespace=LinkModel.ATOM_NAMESPACE)
@JsonRootName(value="link")
public class LinkModel {
	public static final String				ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";

	private String			href;
	private String			rel;
	private String			type;
	private String			hreflang;
	private String			title;
	private String			length;

	public LinkModel() {}

	@XmlAttribute(name="href")
	@JsonProperty("href")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getHref() {
		return href;
	}

	@JsonProperty("href")
	public void setHref( String theHref ) {
		href = theHref;
	}

	@XmlAttribute(name="rel")
	@JsonProperty("rel")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getRel() {
		return rel;
	}

	@JsonProperty("rel")
	public void setRel( String theRel ) {
		rel = theRel;
	}

	@XmlAttribute(name="type")
	@JsonProperty("type")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType( String theType ) {
		type = theType;
	}

	@XmlAttribute(name="hreflang")
	@JsonProperty("hreflang")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getHrefLang() {
		return hreflang;
	}

	@JsonProperty("hreflang")
	public void setHrefLang( String theLang ) {
		hreflang = theLang;
	}

	@XmlAttribute(name="title")
	@JsonProperty("title")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle( String theTitle ) {
		title = theTitle;
	}

	@XmlAttribute(name="length")
	@JsonProperty("length")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getLength() {
		return length;
	}

	@JsonProperty("length")
	public void setLength( String theLength ) {
		length = theLength;
	}
}
