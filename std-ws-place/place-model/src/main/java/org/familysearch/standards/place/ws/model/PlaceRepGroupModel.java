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
@XmlType(propOrder={ "localizedName", "subGroups", "repSummaries", "selfLink" })
public class PlaceRepGroupModel {

	private int                            id;
    private Boolean                        isPublished;
    private LinkModel                      selfLink;
	private List<LocalizedNameDescModel>   nameDescList = new ArrayList<>();
	private List<PlaceRepGroupModel>       subGroups;
	private List<PlaceRepSummaryModel>     repSummaries;

	public PlaceRepGroupModel() {}


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

    @XmlElementWrapper(name="sub-groups")
    @XmlElement(name="sub-group")
    @JsonProperty("subGroups")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<PlaceRepGroupModel> getSubGroups() {
        return subGroups;
    }

    @JsonProperty("subGroups")
    public void setSubGroups( List<PlaceRepGroupModel> theGroups ) {
        subGroups = theGroups;
    }

    @XmlElementWrapper(name="place-reps")
    @XmlElement(name="place-rep")
    @JsonProperty("placeReps")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<PlaceRepSummaryModel> getRepSummaries() {
        return repSummaries;
    }

    @JsonProperty("placeReps")
    public void setRepSummaries( List<PlaceRepSummaryModel> summaries ) {
        repSummaries = summaries;
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
