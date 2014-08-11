package org.familysearch.standards.place.ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;

@XmlRootElement
@XmlType(propOrder={ "displayName", "fullDisplayName", "jurisdiction", "type", "location", "links" })
public class PlaceRepSummaryModel {

    private Integer             id;
    private JurisdictionModel   jurisdiction;
    private TypeModel           type;
    private Integer             ownerId;
    private LocationModel       location;
    private NameModel           displayName;
    private NameModel           fullDisplayName;
    private List<LinkModel>     links;


    public PlaceRepSummaryModel() {}

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
