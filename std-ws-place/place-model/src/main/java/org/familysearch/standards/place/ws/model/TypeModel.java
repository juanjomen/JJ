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
@XmlType(propOrder={ "localizedName", "selfLink", "groups" })
public class TypeModel implements Comparable<TypeModel> {

	private Integer							id;
	private String							code;
	private List<LocalizedNameDescModel>	nameDescList = new ArrayList<LocalizedNameDescModel>();
	private Boolean							isPublished;
	private LinkModel						selfLink;
	private List<LinkModel>					groups;


	public TypeModel() {}


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

	@XmlAttribute(name="code")
	@JsonProperty("code")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode( String theCode ) {
		code = theCode;
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

	@XmlElementWrapper(name="groups")
	@XmlElement(name="link", namespace=LinkModel.ATOM_NAMESPACE)
	@JsonProperty("groups")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public List<LinkModel> getGroups() {
		return groups;
	}

	@JsonProperty("groups")
	public void setGroups( List<LinkModel> theGroups ) {
		groups = theGroups;
	}

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(TypeModel that) {
        LocalizedNameDescModel thisName = getDefaultName(this.getLocalizedName());
        LocalizedNameDescModel thatName = getDefaultName(that.getLocalizedName());

        if (thisName == null  ||  thisName.getName() == null) {
            return -1;
        } else if (thatName == null  ||  thatName.getName() == null) {
            return 1;
        } else {
            return thisName.getName().compareToIgnoreCase(thatName.getName());
        }
    }

    @Override
    public boolean equals( Object obj ) {
    	if ( obj != null && obj instanceof TypeModel ) {
    		return getId().equals( ( ( TypeModel ) obj ).getId() );
    	}

    	return false;
    }

    @Override
    public int hashCode() {
    	return getId();
    }

    /**
     * Retrieve the "en" version of the name; if no "en" version exists, simply choose
     * the first entry.
     * 
     * @param names list of localized names
     * @return "en" version, or the first one encountered.
     */
    private LocalizedNameDescModel getDefaultName(List<LocalizedNameDescModel> names) {
        LocalizedNameDescModel name = null;

        if (names != null) {
            name = (names.size() == 0) ? null : names.get(0);
            for (LocalizedNameDescModel model : names) {
                if (model.getLocale() != null) {
                    String locale = model.getLocale().toLowerCase();
                    if (locale.equals("en")  ||  locale.startsWith("en")) {
                        name = model;
                        break;
                    }
                }
            }
        }

        return name;
    }

}
