package org.familysearch.standards.place.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@XmlType(propOrder={ "counts", "timings", "scorers" })
public class MetricsModel {

	private TimingsModel		timings;
	private CountsModel			counts;
	private ScorersModel		scorers;

	public MetricsModel() {}


	@XmlElement(name="timings")
	@JsonProperty("timings")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public TimingsModel getTimings() {
		return timings;
	}

	@JsonProperty("timings")
	public void setTimings( TimingsModel theTimings ) {
		timings = theTimings;
	}

	@XmlElement(name="counts")
	@JsonProperty("counts")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public CountsModel getCounts() {
		return counts;
	}

	@JsonProperty("counts")
	public void setCounts( CountsModel theCounts ) {
		counts = theCounts;
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
