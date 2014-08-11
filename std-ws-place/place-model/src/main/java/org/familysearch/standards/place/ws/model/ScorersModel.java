package org.familysearch.standards.place.ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
public class ScorersModel {

	private List<ScorerModel>			scorers;

	public ScorersModel() {}


	@XmlElementWrapper(name="scorers")
	@XmlElement(name="scorer")
	@JsonProperty("scorers")
	public List<ScorerModel> getScorers() {
		return scorers;
	}

	@JsonProperty("scorers")
	public void setScorers( List<ScorerModel> theScorers ) {
		scorers = theScorers;
	}
}
