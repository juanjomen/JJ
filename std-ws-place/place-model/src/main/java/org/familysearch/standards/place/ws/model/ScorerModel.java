package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
public class ScorerModel {

	private Long			time;
	private Integer			score;
	private String			name;
	private String			reason;

	public ScorerModel() {}

	@XmlAttribute(name="time")
	@JsonProperty("time")
	public Long getTime() {
		return time;
	}

	@JsonProperty("time")
	public void setTime( Long theTime ) {
		time = theTime;
	}

	@XmlAttribute(name="score")
	@JsonProperty("score")
	public Integer getScore() {
		return score;
	}

	@JsonProperty("score")
	public void setScore( Integer theScore ) {
		score = theScore;
	}

	@XmlAttribute(name="name")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName( String theName ) {
		name = theName;
	}

	@XmlValue()
	@JsonProperty("reason")
	public String getReason() {
		return reason;
	}

	@JsonProperty("reason")
	public void setReason( String theReason ) {
		reason = theReason;
	}
}
