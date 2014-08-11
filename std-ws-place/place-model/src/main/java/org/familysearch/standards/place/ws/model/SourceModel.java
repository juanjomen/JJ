package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@XmlRootElement
@XmlType(propOrder={ "title", "description", "published" })
public class SourceModel {

	private Integer   id;
	private String    title;
	private String    description;
	private Boolean   isPublished;


	public SourceModel() {}


	@XmlAttribute(name="id")
	@JsonProperty("id")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement(name="title")
	@JsonProperty("title")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String theTitle) {
		title = theTitle;
	}

	@XmlElement(name="description")
    @JsonProperty("description")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String theDescription) {
        description = theDescription;
    }

    @XmlElement(name="published")
	@JsonProperty("published")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Boolean isPublished() {
		return isPublished;
	}

	@JsonProperty("published")
	public void setIsPublished(Boolean publishedFlag) {
		isPublished = publishedFlag;
	}
}
