package org.familysearch.standards.place.ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@XmlType(propOrder={ "results", "metrics" })
public class PlaceSearchResultsModel {

	private int									id;
	private int									count;
	private List<PlaceSearchResultModel>		results;
	private MetricsModel						metrics;

	public PlaceSearchResultsModel() {}

	@XmlAttribute(name="ref-id")
	@JsonProperty("refId")
	public int getRefId() {
		return id;
	}

	@JsonProperty("refId")
	public void setRefId( int theId ) {
		id = theId;
	}

	@XmlAttribute(name="count")
	@JsonProperty("count")
	public int getCount() {
		return count;
	}

	@JsonProperty("count")
	public void setCount( int theCount ) {
		count = theCount;
	}

	@XmlElement(name="result")
	@JsonProperty("result")
	public List<PlaceSearchResultModel> getResults() {
		return results;
	}

	@JsonProperty("result")
	public void setResults( List<PlaceSearchResultModel> theResults ) {
		results = theResults;
	}

	@XmlElement(name="metrics")
	@JsonProperty("metrics")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public MetricsModel getMetrics() {
		return metrics;
	}

	@JsonProperty("metrics")
	public void setMetrics( MetricsModel theMetrics ) {
		metrics = theMetrics;
	}
}
