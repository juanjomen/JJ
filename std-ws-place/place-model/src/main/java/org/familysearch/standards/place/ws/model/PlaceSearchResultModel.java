package org.familysearch.standards.place.ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@XmlType(propOrder={ "rep", "rawScore", "relevanceScore", "distanceInKM", "distanceInMiles", "tokenMatches", "scorers" })
public class PlaceSearchResultModel {

	private PlaceRepresentationModel			rep;
	private Integer								rawScore;
	private Integer								relevanceScore;
	private Double								distanceInKM;
	private Double								distanceInMiles;
	private List<TokenMatch>					tokenMatches;
	private ScorersModel						scorers;

	public PlaceSearchResultModel() {}


	@XmlElement(name="rep")
	@JsonProperty("rep")
	public PlaceRepresentationModel getRep() {
		return rep;
	}

	@JsonProperty("rep")
	public void setRep( PlaceRepresentationModel theRep ) {
		rep = theRep;
	}

	@XmlElement(name="raw-score")
	@JsonProperty("rawScore")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getRawScore() {
		return rawScore;
	}

	@JsonProperty("rawScore")
	public void setRawScore( Integer theScore ) {
		rawScore = theScore;
	}

	@XmlElement(name="relevance-score")
	@JsonProperty("relevanceScore")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getRelevanceScore() {
		return relevanceScore;
	}

	@JsonProperty("relevanceScore")
	public void setRelevanceScore( Integer theScore ) {
		relevanceScore = theScore;
	}

	@XmlElement(name="distance-km")
	@JsonProperty("distanceInKM")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Double getDistanceInKM() {
		return distanceInKM;
	}

	@JsonProperty("distanceInKM")
	public void setDistanceInKM( Double theDistance ) {
		distanceInKM = theDistance;
	}

	@XmlElement(name="distance-miles")
	@JsonProperty("distanceInMiles")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Double getDistanceInMiles() {
		return distanceInMiles;
	}

	@JsonProperty("distanceInMiles")
	public void setDistanceInMiles( Double theDistance ) {
		distanceInMiles = theDistance;
	}

	@XmlElementWrapper(name="token-matches")
	@XmlElement(name="match")
	@JsonProperty("tokenMatches")
	public List<TokenMatch> getTokenMatches() {
		return tokenMatches;
	}

	@JsonProperty("tokenMatches")
	public void setTokenMatches( List<TokenMatch> theMatches ) {
		tokenMatches = theMatches;
	}

	@XmlElement(name="scorers")
	@JsonProperty("scorers")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public ScorersModel getScorers() {
		return scorers;
	}

	@JsonProperty("scorers")
	public void setScorers( ScorersModel theScorers ) {
		scorers = theScorers;
	}

}
