package org.familysearch.standards.place.ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;

@XmlRootElement
@XmlType(propOrder={ "UUID", "revision", "displayName", "fullDisplayName", "displayNames", "jurisdiction", "type", "group", "preferredLocale", "fromYear", "toYear", "location", "published", "validated", "links", "children" })
public class PlaceRepresentationModel {

	private Integer				id;
	private JurisdictionModel	jurisdiction;
	private TypeModel			type;
	private Integer				ownerId;
	private PlaceTypeGroupModel		group;
	private Integer				fromYear;
	private Integer				toYear;
	private String				preferredLocale;
	private boolean				published;
	private boolean				validated;
	private LocationModel		location;
	private String				uuid;
	private Integer				revision;
	private List<LinkModel>		links;

	private NameModel			displayName;
	private NameModel			fullDisplayName;
	private List<NameModel>		displayNames;
	private List<PlaceRepresentationModel>	children;


	public PlaceRepresentationModel() {}

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

	@XmlElement(name="jurisdiction")
	@JsonProperty("jurisdiction")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public JurisdictionModel getJurisdiction() {
		return jurisdiction;
	}

	@JsonProperty("jurisdiction")
	public void setJurisdiction( JurisdictionModel theJurisdiction ) {
		jurisdiction = theJurisdiction;
	}

	@XmlElement(name="type")
	@JsonProperty("type")
	public TypeModel getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType( TypeModel theType ) {
		type = theType;
	}

	@XmlAttribute(name="place")
	@JsonProperty("place")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getOwnerId() {
		return ownerId;
	}

	@JsonProperty("place")
	public void setOwnerId( Integer ownerId ) {
		this.ownerId = ownerId;
	}

	@XmlElement(name="type-group")
	@JsonProperty("typeGroup")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public PlaceTypeGroupModel getGroup() {
		return group;
	}

	@JsonProperty("typeGroup")
	public void setGroup( PlaceTypeGroupModel theGroup ) {
		group = theGroup;
	}

	@XmlElement(name="from-year")
	@JsonProperty("fromYear")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getFromYear() {
		return fromYear;
	}

	@JsonProperty("fromYear")
	public void setFromYear( Integer theFromYear ) {
		fromYear = theFromYear;
	}

	@XmlElement(name="to-year")
	@JsonProperty("toYear")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getToYear() {
		return toYear;
	}

	@JsonProperty("toYear")
	public void setToYear( Integer theToYear ) {
		toYear = theToYear;
	}

	@XmlElement(name="uuid")
	@JsonProperty("uuid")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public String getUUID() {
		return uuid;
	}

	@JsonProperty("uuid")
	public void setUUID( String theUUID ) {
		uuid = theUUID;
	}

	@XmlElement(name="revision")
	@JsonProperty("revision")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getRevision() {
		return revision;
	}

	@JsonProperty("revision")
	public void setRevision( Integer theRevision ) {
		revision = theRevision;
	}

	@XmlElement(name="preferred-locale")
	@JsonProperty("preferredLocale")
	public String getPreferredLocale() {
		return preferredLocale;
	}

	@JsonProperty("preferredLocale")
	public void setPreferredLocale( String thePreferredLocale ) {
		preferredLocale = thePreferredLocale;
	}

	@XmlElement(name="published")
	@JsonProperty("published")
	public boolean isPublished() {
		return published;
	}

	@JsonProperty("published")
	public void setPublished( boolean isPublished ) {
		published = isPublished;
	}

	@XmlElement(name="validated")
	@JsonProperty("validated")
	public boolean isValidated() {
		return validated;
	}

	@JsonProperty("validated")
	public void setValidated( boolean isValidated ) {
		validated = isValidated;
	}

	@XmlElement(name="location")
	@JsonProperty("location")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public LocationModel getLocation() {
		return location;
	}

	@JsonProperty("location")
	public void setLocation( LocationModel theLocation ) {
		location = theLocation;
	}

	@XmlElement(name="display")
	@JsonProperty("display")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public NameModel getDisplayName() {
		return displayName;
	}

	@JsonProperty("display")
	public void setDisplayName( NameModel theName ) {
		displayName = theName;
	}

	@XmlElement(name="full-display")
	@JsonProperty("fullDisplay")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public NameModel getFullDisplayName() {
		return fullDisplayName;
	}

    @JsonProperty("fullDisplay")
	public void setFullDisplayName( NameModel theName ) {
		fullDisplayName = theName;
	}

	@XmlElementWrapper(name="display-names")
	@XmlElement(name="name")
	@JsonProperty("displayNames")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public List<NameModel> getDisplayNames() {
		return displayNames;
	}

	@JsonProperty("displayNames")
	public void setDisplayNames( List<NameModel> theDisplayNames ) {
		displayNames = theDisplayNames;
	}

	@XmlElementWrapper(name="children")
	@XmlElement(name="rep")
	@JsonProperty("children")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public List<PlaceRepresentationModel> getChildren() {
		return children;
	}

	@JsonProperty("children")
	public void setChildren( List<PlaceRepresentationModel> theChildren ) {
		children = theChildren;
	}

	@XmlElement(name="link", namespace=LinkModel.ATOM_NAMESPACE)
	@JsonProperty("link")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public List<LinkModel> getLinks() {
		return links;
	}

	@JsonProperty("link")
	public void setLinks( List<LinkModel> theLinks ) {
		links = theLinks;
	}

	public String toXML() {
		return toString();
	}

    public String toJSON() {
        return POJOMarshalUtil.toJSON(this);
    }

    @Override
    public String toString() {
        return POJOMarshalUtil.toXML(this);
    }

}
