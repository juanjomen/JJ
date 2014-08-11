package org.familysearch.standards.place.ws.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@XmlType(propOrder={ "localizedName", "types", "subGroups", "selfLink" })
public class PlaceTypeGroupModel {

	private int                            id;
    private Boolean                        isPublished;
    private LinkModel                      selfLink;
	private List<LocalizedNameDescModel>   nameDescList = new ArrayList<LocalizedNameDescModel>();
	private List<TypeModel>			       types;
	private List<PlaceTypeGroupModel>      subGroups;

	public PlaceTypeGroupModel() {}


	@XmlAttribute(name="id")
	@JsonProperty("id")
	public int getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId( int theId ) {
		id = theId;
	}

    @XmlElementWrapper(name="localized-names")
    @XmlElement(name="localized-name", type=LocalizedNameDescModel.class)
    @JsonProperty("localizedName")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<LocalizedNameDescModel> getLocalizedName() {
        return nameDescList;
    }

    @JsonProperty("localizedName")
    public void setName( List<LocalizedNameDescModel> theNameDesc ) {
        nameDescList = theNameDesc;
    }

    @XmlAttribute(name="published")
    @JsonProperty("published")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Boolean isPublished() {
        return isPublished;
    }

    @JsonProperty("published")
    public void setIsPublished( Boolean publishedFlag ) {
        isPublished = publishedFlag;
    }

	@XmlElementWrapper(name="place-types")
	@XmlElement(name="place-type")
	@JsonProperty("placeTypes")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public List<TypeModel> getTypes() {
		return types;
	}

	@JsonProperty("placeTypes")
	public void setTypes( List<TypeModel> theTypes ) {
		types = theTypes;
	}

	@XmlElementWrapper(name="sub-groups")
	@XmlElement(name="sub-group")
	@JsonProperty("subGroups")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public List<PlaceTypeGroupModel> getSubGroups() {
		return subGroups;
	}

	@JsonProperty("subGroups")
	public void setSubGroups( List<PlaceTypeGroupModel> theGroups ) {
		subGroups = theGroups;
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
