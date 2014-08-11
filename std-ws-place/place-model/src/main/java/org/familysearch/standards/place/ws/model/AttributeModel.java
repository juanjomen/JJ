package org.familysearch.standards.place.ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@XmlRootElement
@XmlType(propOrder={ "type", "year", "value", "links" })
public class AttributeModel {

	private Integer           id;
	private Integer           repId;
    private TypeModel         type;
	private Integer           year;
	private String            value;
    private List<LinkModel>   links;


	public AttributeModel() {}


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

    @XmlElement(name="year")
    @JsonProperty("year")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Integer getYear() {
        return year;
    }

    @JsonProperty("year")
    public void setYear(Integer year) {
        this.year = year;
    }

    @XmlElement(name="value")
	@JsonProperty("value")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(String value) {
		this.value = value;
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
