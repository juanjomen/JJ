package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
@XmlType(propOrder={ "text", "filter", "limit", "threshold", "partial", "fuzzy", "publishedType", "validatedType", "acceptLanguage", "language", "optionalParents", "requiredParents", "requiredDirParents", "filterParents", "optionalTypes", "requiredTypes", "filterTypes", "priorityTypes", "reqTypeGroups", "filterTypeGroups", "optionalYears", "requiredYears", "latitude", "longitude", "distance", "profile", "version", "optDirParentIds", "points", "includeMetrics", "useWildcards" })
public class PlaceSearchRequestModel {

	private int               id;
	private String            text;
	private String            reqParentIds;
	private String            reqDirParentIds;
	private String            optParentIds;
	private String            filterParentIds;
	private String            requiredTypes;
	private String            optionalTypes;
	private String            priorityTypes;
	private String            filterTypes;
	private String            reqTypeGroups;
	private String            filterTypeGroups;
	private String  		  reqYears;
	private String			  optYears;
	private boolean           filter;
	private int            	  limit = 0;
	private String            fuzzy;
	private int               threshold;
	private String            profile;
	private String            acceptLang;
	private String            lang;
	private boolean           partial;
	private Double            lat;
	private Double            lng;
	private String            distance;
	private String            published;
	private String            validated;
	private String            version;
	private String            optDirParentIds;
	private String            points;
	private boolean           includeMetrics;
	private boolean           useWildcards;

	public PlaceSearchRequestModel() {}


	@XmlAttribute(name="id")
	@JsonProperty("id")
	public int getId() {
		return id;
	}

    @JsonProperty("id")
	public void setId( int theId ) {
		id = theId;
	}

	@XmlElement(name="text")
	@JsonProperty("text")
	public String getText() {
		return text;
	}

    @JsonProperty("text")
	public void setText( String theText ) {
		text = theText;
	}

	@XmlElement(name="filter")
	@JsonProperty("filter")
	public boolean getFilter() {
		return filter;
	}

    @JsonProperty("filter")
	public void setFilter( boolean filterFlag ) {
		filter = filterFlag;
	}

	@XmlElement(name="pub-type")
	@JsonProperty("pubType")
    public String getPublishedType() {
    	return published;
    }

	@JsonProperty("pubType")
    public void setPublishedType( String type ) {
    	published = type;
    }

	@XmlElement(name="val-type")
	@JsonProperty("valType")
    public String getValidatedType() {
    	return validated;
    }

	@JsonProperty("valType")
    public void setValidatedType( String type ) {
    	validated = type;
    }

	@XmlElement(name="partial")
	@JsonProperty("partial")
	public boolean getPartial() {
		return partial;
	}

    @JsonProperty("partial")
	public void setPartial( boolean partialFlag ) {
		partial = partialFlag;
	}

	@XmlElement(name="limit")
	@JsonProperty("limit")
	public int getLimit() {
		return limit;
	}

    @JsonProperty("limit")
	public void setLimit( int theLimit ) {
		limit = theLimit;
	}

    @JsonProperty("fuzzy")
	public void setFuzzy( String fuzzyType ) {
		fuzzy = fuzzyType;
	}

	@XmlElement(name="fuzzy")
	@JsonProperty("fuzzy")
	public String getFuzzy() {
		return fuzzy;
	}

    @JsonProperty("threshold")
	public void setThreshold( int theThreshold ) {
		threshold = theThreshold;
	}

	@XmlElement(name="threshold")
	@JsonProperty("threshold")
	public int getThreshold() {
		return threshold;
	}

    @JsonProperty("profile")
	public void setProfile( String theProfile ) {
		profile = theProfile;
	}

	@XmlElement(name="profile")
	@JsonProperty("profile")
	public String getProfile() {
		return profile;
	}

    @JsonProperty("acceptLanguage")
	public void setAcceptLanguage( String theLang ) {
		acceptLang = theLang;
	}

	@XmlElement(name="accept-language")
	@JsonProperty("acceptLanguage")
	public String getAcceptLanguage() {
		return acceptLang;
	}

    @JsonProperty("language")
	public void setLanguage( String theLang ) {
		lang = theLang;
	}

	@XmlElement(name="language")
	@JsonProperty("language")
	public String getLanguage() {
		return lang;
	}

    @JsonProperty("reqTypes")
	public void setRequiredTypes( String typeIds ) {
		requiredTypes = typeIds;
	}

	@XmlElement(name="req-types")
	@JsonProperty("reqTypes")
	public String getRequiredTypes() {
		return requiredTypes;
	}

    @JsonProperty("filterTypes")
	public void setFilterTypes( String typeIds ) {
		filterTypes = typeIds;
	}

	@XmlElement(name="filter-types")
	@JsonProperty("filterTypes")
	public String getFilterTypes() {
		return filterTypes;
	}

    @JsonProperty("optTypes")
	public void setOptionalTypes( String typeIds ) {
		optionalTypes = typeIds;
	}

	@XmlElement(name="opt-types")
	@JsonProperty("optTypes")
	public String getOptionalTypes() {
		return optionalTypes;
	}

    @JsonProperty("reqTypeGroups")
	public void setReqTypeGroups( String groupIds ) {
		reqTypeGroups = groupIds;
	}

	@XmlElement(name="req-type-groups")
	@JsonProperty("reqTypeGroups")
	public String getReqTypeGroups() {
		return reqTypeGroups;
	}

    @JsonProperty("filterTypeGroups")
	public void setFilterTypeGroups( String groupIds ) {
		filterTypeGroups = groupIds;
	}

	@XmlElement(name="filter-type-groups")
	@JsonProperty("filterTypeGroups")
	public String getFilterTypeGroups() {
		return filterTypeGroups;
	}

	@XmlElement(name="priority-types")
	@JsonProperty("priorityTypes")
	public String getPriorityTypes() {
		return priorityTypes;
	}

    @JsonProperty("priorityTypes")
    public void setPriorityTypes( String types ) {
        priorityTypes = types;
    }

    @JsonProperty("reqYears")
	public void setRequiredYears( String years ) {
		reqYears = years;
	}

	@XmlElement(name="req-years")
	@JsonProperty("reqYears")
	public String getRequiredYears() {
		return reqYears;
	}

    @JsonProperty("optYears")
	public void setOptionalYears( String years ) {
		optYears = years;
	}

	@XmlElement(name="opt-years")
	@JsonProperty("optYears")
	public String getOptionalYears() {
		return optYears;
	}

    @JsonProperty("optParents")
	public void setOptionalParents( String parents ) {
		optParentIds = parents;
	}

	@XmlElement(name="opt-parents")
	@JsonProperty("optParents")
	public String getOptionalParents() {
		return optParentIds;
	}

    @JsonProperty("reqParents")
	public void setRequiredParents( String parents ) {
		reqParentIds = parents;
	}

	@XmlElement(name="req-parents")
	@JsonProperty("reqParents")
	public String getRequiredParents() {
		return reqParentIds;
	}

    @JsonProperty("reqDirParents")
	public void setRequiredDirParents( String parents ) {
		reqDirParentIds = parents;
	}

	@XmlElement(name="req-dir-parents")
	@JsonProperty("reqDirParents")
	public String getRequiredDirParents() {
		return reqDirParentIds;
	}

    @JsonProperty("filterParents")
	public void setFilterParents( String parents ) {
		filterParentIds = parents;
	}

	@XmlElement(name="filter-parents")
	@JsonProperty("filterParents")
	public String getFilterParents() {
		return filterParentIds;
	}

    @JsonProperty("lat")
	public void setLatitude( Double theLat ) {
		lat = theLat;
	}

	@XmlElement(name="lat")
	@JsonProperty("lat")
	public Double getLatitude() {
		return lat;
	}

    @JsonProperty("lng")
	public void setLongitude( Double theLong ) {
		lng = theLong;
	}

	@XmlElement(name="lng")
	@JsonProperty("lng")
	public Double getLongitude() {
		return lng;
	}

    @JsonProperty("distance")
	public void setDistance( String theDist ) {
		distance = theDist;
	}

	@XmlElement(name="distance")
	@JsonProperty("distance")
	public String getDistance() {
		return distance;
	}

    @JsonProperty("version")
    public void setVersion( String version ) {
        this.version = version;
    }

    @XmlElement(name="version")
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("optDirParents")
    public void setOptDirParentIds( String optDirParentIds ) {
        this.optDirParentIds = optDirParentIds;
    }

    @XmlElement(name="opt-dir-parents")
    @JsonProperty("optDirParents")
    public String getOptDirParentIds() {
        return optDirParentIds;
    }

    @JsonProperty("points")
    public void setPoints( String points ) {
        this.points = points;
    }

    @XmlElement(name="points")
    @JsonProperty("points")
    public String getPoints() {
        return points;
    }

    @XmlElement(name="metrics")
    @JsonProperty("metrics")
    public boolean getIncludeMetrics() {
        return includeMetrics;
    }

    @JsonProperty("metrics")
    public void setIncludeMetrics( boolean includeMetrics ) {
        this.includeMetrics = includeMetrics;
    }

    @XmlElement(name="wildcards")
    @JsonProperty("wildcards")
    public boolean getUseWildcards() {
        return useWildcards;
    }

    @JsonProperty("wildcards")
    public void setUseWildcars( boolean useWildcards ) {
        this.useWildcards = useWildcards;
    }

}
