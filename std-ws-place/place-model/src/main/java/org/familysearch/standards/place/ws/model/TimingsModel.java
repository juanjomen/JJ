package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
public class TimingsModel {

	private Long			parseTime;
	private Long			identifyCandidatesTime;
	private Long			identifyCandidatesLookupTime;
	private Long			identifyCandidatesTailMatchTime;
	private Long			identifyCandidatesMaxHitFilterTime;
	private Long			scoringTime;
	private Long			assemblyTime;
	private Long			totalTime;

	public TimingsModel() {}


	@XmlElement(name="parse-time")
	@JsonProperty("parseTime")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Long getParseTime() {
		return parseTime;
	}

	@JsonProperty("parseTime")
	public void setParseTime( Long time ) {
		parseTime = time;
	}

	@XmlElement(name="identify-candidates-time")
	@JsonProperty("identifyCandidatesTime")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Long getIdentifyCandidatesTime() {
		return identifyCandidatesTime;
	}

	@JsonProperty("identifyCandidatesTime")
	public void setIdentifyCandidatesTime( Long time ) {
		identifyCandidatesTime = time;
	}

	@XmlElement(name="identify-candidates-lookup-time")
	@JsonProperty("identifyCandidatesLookupTime")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Long getIdentifyCandidatesLookupTime() {
		return identifyCandidatesLookupTime;
	}

	@JsonProperty("identifyCandidatesLookupTime")
	public void setIdentifyCandidatesLookupTime( Long time ) {
		identifyCandidatesLookupTime = time;
	}

	@XmlElement(name="identify-candidates-tail-match-time")
	@JsonProperty("identifyCandidatesTailMatchTime")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Long getIdentifyCandidatesTailMatchTime() {
		return identifyCandidatesTailMatchTime;
	}

	@JsonProperty("identifyCandidatesTailMatchTime")
	public void setIdentifyCandidatesTailMatchTime( Long time ) {
		identifyCandidatesTailMatchTime = time;
	}

	@XmlElement(name="identify-candidates-max-hit-filter-time")
	@JsonProperty("identifyCandidatesMaxHitFilterTime")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Long getIdentifyCandidatesMaxHitFilterTime() {
		return identifyCandidatesMaxHitFilterTime;
	}

	@JsonProperty("identifyCandidatesMaxHitFilterTime")
	public void setIdentifyCandidatesMaxHitFilterTime( Long time ) {
		identifyCandidatesMaxHitFilterTime = time;
	}

	@XmlElement(name="scoring-time")
	@JsonProperty("scoringTime")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Long getScoringTime() {
		return scoringTime;
	}

	@JsonProperty("scoringTime")
	public void setScoringTime( Long time ) {
		scoringTime = time;
	}

	@XmlElement(name="assembly-time")
	@JsonProperty("assemblyTime")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Long getAssemblyTime() {
		return assemblyTime;
	}

	@JsonProperty("assemblyTime")
	public void setAssemblyTime( Long time ) {
		assemblyTime = time;
	}

	@XmlElement(name="total-time")
	@JsonProperty("totalTime")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Long getTotalTime() {
		return totalTime;
	}

	@JsonProperty("totalTime")
	public void setTotalTime( Long time ) {
		totalTime = time;
	}
}
