package org.familysearch.standards.place.ws.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@XmlRootElement
@XmlType(propOrder={ "type", "description", "sourceRef", "citDate", "links" })
public class CitationModel {

	private Integer           id;
	private Integer           repId;
    private Integer           sourceId;
	private TypeModel         type;
	private String            description;
    private String            sourceRef;
    private Date              citDate;
    private List<LinkModel>   links;


	public CitationModel() {}


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

    @XmlAttribute(name="rep-id")
    @JsonProperty("repId")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Integer getRepId() {
        return repId;
    }

    @JsonProperty("repId")
    public void setRepId(Integer repId) {
        this.repId = repId;
    }

    @XmlAttribute(name="source-id")
    @JsonProperty("sourceId")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Integer getSourceId() {
        return sourceId;
    }

    @JsonProperty("sourceId")
    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
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

    @XmlElement(name="description")
	@JsonProperty("description")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

    @XmlElement(name="source-ref")
    @JsonProperty("sourceRef")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public String getSourceRef() {
        return sourceRef;
    }

    @JsonProperty("sourceRef")
    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    @XmlElement(name="cit-date")
    @JsonProperty("citDate")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Date getCitDate() {
        return citDate;
    }

    @JsonProperty("citDate")
    public void setCitDate(Date citDate) {
        this.citDate = citDate;
    }

    @XmlElement(name="link", namespace=LinkModel.ATOM_NAMESPACE)
    @JsonProperty("link")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<LinkModel> getLinks() {
        return links;
    }

    @JsonProperty("link")
    public void setLinks(List<LinkModel> links) {
        this.links = links;
    }
}
