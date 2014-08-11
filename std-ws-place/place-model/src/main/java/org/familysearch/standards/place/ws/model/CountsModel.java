package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
public class CountsModel {

	private Integer			rawCandidateCount;
	private Integer			preScoringCandidateCount;
	private Integer			initialParsedInputTextCount;
	private Integer			finalParsedInputTextCount;

	public CountsModel() {}

	@XmlElement(name="raw-candidate-count")
	@JsonProperty("rawCandidateCount")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getRawCandidateCount() {
		return rawCandidateCount;
	}

	@JsonProperty("rawCandidateCount")
	public void setRawCandidateCount( Integer count ) {
		rawCandidateCount = count;
	}

	@XmlElement(name="pre-scoring-candidate-count")
	@JsonProperty("preScoringCandidateCount")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getPreScoringCandidateCount() {
		return preScoringCandidateCount;
	}

	@JsonProperty("preScoringCandidateCount")
	public void setPreScoringCandidateCount( Integer count ) {
		preScoringCandidateCount = count;
	}

	@XmlElement(name="initial-parsed-input-text-count")
	@JsonProperty("initialParsedInputTextCount")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getInitialParsedInputTextCount() {
		return initialParsedInputTextCount;
	}

	@JsonProperty("initialParsedInputTextCount")
	public void setInitialParsedInputTextCount( Integer count ) {
		initialParsedInputTextCount = count;
	}

	@XmlElement(name="final-parsed-input-text-count")
	@JsonProperty("finalParsedInputTextCount")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getFinalParsedInputTextCount() {
		return finalParsedInputTextCount;
	}

	@JsonProperty("finalParsedInputTextCount")
	public void setFinalParsedInputTextCount( Integer count ) {
		finalParsedInputTextCount = count;
	}
}
