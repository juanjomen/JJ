package org.familysearch.standards.place.ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;


/**
 * The one -- and only -- marshal-able class for the web-service, representing both the
 * HTTP request and HTTP response body contents.  It's a wrapper for all possible input
 * or output values.
 * 
 * @author dshellman, wjohnson000
 *
 */
@XmlRootElement(name="places", namespace=RootModel.PLACES_NAMESPACE)
@JsonRootName(value="places")
public class RootModel {

    /** Custom content [mime] types for the application */
    public static final String    APPLICATION_XML_PLACES = "application/standards-places-v2+xml";
    public static final String    APPLICATION_JSON_PLACES = "application/standards-places-v2+json";
    public static final String    PLACES_NAMESPACE = "http://familysearch.org/standards/places/2.0";

    /** Body objects, only one of which should be populate at any time */
    private List<PlaceSearchResultsModel>  searchResults;
    private List<PlaceSearchRequestModel>  requests;
    private PlaceModel                     place;
    private PlaceRepresentationModel       placeRep;
    private List<TypeModel>                theTypes;
    private TypeModel                      theType;
    private List<PlaceTypeGroupModel>      typeGroups;
    private PlaceTypeGroupModel            typeGroup;
    private List<PlaceRepGroupModel>       placeRepGroups;
    private PlaceRepGroupModel             placeRepGroup;
    private List<SourceModel>              sources;
    private SourceModel                    source;
    private List<AttributeModel>           attributes;
    private AttributeModel                 attribute;
    private List<CitationModel>            citations;
    private CitationModel                  citation;
    private HealthCheckModel               healthCheck;
    private List<LinkModel>                links;
    private List<Integer>                  repIds;


    /**
     * Default constructor
     */
    public RootModel() { }

    /**
     * Search results represent the top-level retrieval of multiple
     * results for multiple requests.  In other words, there may be multiple
     * search requests (each has its own id), and therefore, has a separate
     * set of results for each request (a list of a list).
     * 
     * @return Returns the set of search results containing results.
     */
    @XmlElement(name="search-results")
    @JsonProperty("searchResults")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<PlaceSearchResultsModel> getSearchResults() {
        return searchResults;
    }

    @JsonProperty("searchResults")
    public void setSearchResults(List<PlaceSearchResultsModel> theSearchResults) {
        searchResults = theSearchResults;
    }

    /**
     * Requests represent the top-level receipt of multiple search
     * requests.  This is to handle bulk search requests.
     * 
     * @return Returns the list of requests.
     */
    @XmlElement(name="request")
    @JsonProperty("request")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<PlaceSearchRequestModel> getRequests() {
        return requests;
    }

    @JsonProperty("request")
    public void setRequests(List<PlaceSearchRequestModel> theRequests) {
        requests = theRequests;
    }

    /**
     * Place represents the top-level retrieval of a specific place resource.
     * A place resource has data about the place as well as all of the
     * place representations that it contains/owns.
     * 
     * @return Returns the place.
     */
    @XmlElement(name="place")
    @JsonProperty("place")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public PlaceModel getPlace() {
        return place;
    }

    @JsonProperty("place")
    public void setPlace(PlaceModel thePlace) {
        place = thePlace;
    }

    /**
     * Place Representation represents the top-level retrieval of a specific
     * place representation resource.
     * 
     * @return Returns the place representation.
     */
    @XmlElement(name="rep")
    @JsonProperty("rep")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public PlaceRepresentationModel getPlaceRepresentation() {
        return placeRep;
    }

    @JsonProperty("rep")
    public void setPlaceRepresentation(PlaceRepresentationModel theRep) {
        placeRep = theRep;
    }

    /**
     * Types represent the top-level retrieval of all types (place, name, attribute,
     * citation, ext-xref, resolution, etc).
     * 
     * @return Returns the list of all published types of a particular category.
     */
    @XmlElementWrapper(name="types")
    @XmlElement(name="type")
    @JsonProperty("types")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<TypeModel> getTypes() {
        return theTypes;
    }

    @JsonProperty("types")
    public void setTypes(List<TypeModel> theTypes) {
        this.theTypes = theTypes;
    }

    @XmlElement(name="type")
    @JsonProperty("type")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public TypeModel getType() {
        return theType;
    }

    @JsonProperty("type")
    public void setType(TypeModel theType) {
        this.theType = theType;
    }

    /**
     * Place Type Groups represent the top-level retrieval of all place type groups.
     * 
     * @return Returns the list of all published place type groups.
     */
    @XmlElementWrapper(name="place-type-groups")
    @XmlElement(name="place-type-group")
    @JsonProperty("placeTypeGroups")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<PlaceTypeGroupModel> getPlaceTypeGroups() {
        return typeGroups;
    }

    @JsonProperty("placeTypeGroups")
    public void setPlaceTypeGroups(List<PlaceTypeGroupModel> theTypes) {
        typeGroups = theTypes;
    }

    @XmlElement(name="place-type-group")
    @JsonProperty("placeTypeGroup")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public PlaceTypeGroupModel getPlaceTypeGroup() {
        return typeGroup;
    }

    @JsonProperty("placeTypeGroup")
    public void setPlaceTypeGroup(PlaceTypeGroupModel theType) {
        typeGroup = theType;
    }

    /**
     * Place Rep Groups represent the top-level retrieval of all place rep groups.
     * 
     * @return Returns the list of all published place rep groups.
     */
    @XmlElementWrapper(name="place-rep-groups")
    @XmlElement(name="place-rep-group")
    @JsonProperty("placeRepGroups")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<PlaceRepGroupModel> getPlaceRepGroups() {
        return placeRepGroups;
    }

    @JsonProperty("placeRepGroups")
    public void setPlaceRepGroups(List<PlaceRepGroupModel> theReps) {
        placeRepGroups = theReps;
    }

    @XmlElement(name="place-rep-group")
    @JsonProperty("placeRepGroup")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public PlaceRepGroupModel getPlaceRepGroup() {
        return placeRepGroup;
    }

    @JsonProperty("placeRepGroup")
    public void setPlaceRepGroup(PlaceRepGroupModel theRep) {
        placeRepGroup = theRep;
    }

    /**
     * Sources represent the top-level retrieval of all sources.
     * 
     * @return Returns the list of all published sources.
     */
    @XmlElementWrapper(name="sources")
    @XmlElement(name="source")
    @JsonProperty("sources")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<SourceModel> getSources() {
        return sources;
    }

    @JsonProperty("sources")
    public void setSources(List<SourceModel> theSources) {
        sources = theSources;
    }

    @XmlElement(name="source")
    @JsonProperty("source")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public SourceModel getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(SourceModel theSource) {
        source = theSource;
    }

    /**
     * Attributes represent the retrieval of all attributes associated with a place-rep.
     * 
     * @return Returns the list of all of a place-rep's attributes.
     */
    @XmlElementWrapper(name="attributes")
    @XmlElement(name="attribute")
    @JsonProperty("attributes")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<AttributeModel> getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(List<AttributeModel> theAttributes) {
        attributes = theAttributes;
    }

    @XmlElement(name="attribute")
    @JsonProperty("attribute")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public AttributeModel getAttribute() {
        return attribute;
    }

    @JsonProperty("attribute")
    public void setAttribute(AttributeModel theAttribute) {
        attribute = theAttribute;
    }

    /**
     * Citations represent the retrieval of all citations associated with a place-rep.
     * 
     * @return Returns the list of all of a place-rep's citations.
     */
    @XmlElementWrapper(name="citations")
    @XmlElement(name="citation")
    @JsonProperty("citations")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<CitationModel> getCitations() {
        return citations;
    }

    @JsonProperty("citations")
    public void setCitations(List<CitationModel> theCitations) {
        citations = theCitations;
    }

    @XmlElement(name="citation")
    @JsonProperty("citation")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public CitationModel getCitation() {
        return citation;
    }

    @JsonProperty("citation")
    public void setCitation(CitationModel theCitation) {
        citation = theCitation;
    }

    /**
     * Health-Check returns some top-level links as well as general health of the system.
     * 
     * @return Returns basic health-check information
     */
    @XmlElement(name="health")
    @JsonProperty("health")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public HealthCheckModel getHealthCheck() {
        return healthCheck;
    }

    @JsonProperty("health")
    public void setHealthCheck(HealthCheckModel model) {
        healthCheck = model;
    }

    /**
     * Links represent the a list of Links
     * 
     * @return Returns the list of all links
     */
    @XmlElementWrapper(name="links")
    @XmlElement(name="link")
    @JsonProperty("links")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<LinkModel> getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(List<LinkModel> theLinks) {
        links = theLinks;
    }

    /**
     * repIds represent the a list of place-rep identifiers
     * 
     * @return Returns the list of all place-rep identifiers
     */
    @XmlElementWrapper(name="rep-ids")
    @XmlElement(name="rep-id")
    @JsonProperty("repIds")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<Integer> getRepIds() {
        return repIds;
    }

    @JsonProperty("repIds")
    public void setRepIds(List<Integer> repIds) {
        this.repIds = repIds;
    }

    /**
     * Return an XML string that represents the marshal-ed RootModel instance
     * @return XML string
     */
    public String toXML() {
        return toString();
    }

    /**
     * Return a JSON string that represents the marshal-ed RootModel instance
     * @return JSON string
     */
    public String toJSON() {
        return POJOMarshalUtil.toJSON(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return POJOMarshalUtil.toXML(this);
    }
}
