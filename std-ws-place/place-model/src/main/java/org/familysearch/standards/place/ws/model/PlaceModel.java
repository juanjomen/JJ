package org.familysearch.standards.place.ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.familysearch.standards.place.ws.util.POJOMarshalUtil;

@XmlRootElement
@XmlType(propOrder={ "fromYear", "toYear", "variants", "reps" })
public class PlaceModel {

	private Integer							id;
	private Integer							fromYear;
	private Integer							toYear;
	private List<VariantModel>				variants;
	private List<PlaceRepresentationModel>	reps;

	public PlaceModel() {}

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

	@XmlElement(name="from-year")
	@JsonProperty("fromYear")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getFromYear() {
		return fromYear;
	}

	@JsonProperty("fromYear")
	public void setFromYear( Integer theYear ) {
		fromYear = theYear;
	}

	@XmlElement(name="to-year")
	@JsonProperty("toYear")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
	public Integer getToYear() {
		return toYear;
	}

	@JsonProperty("toYear")
	public void setToYear( Integer theYear ) {
		toYear = theYear;
	}

	@XmlElementWrapper(name="variant-names")
	@XmlElement(name="variant-name")
	@JsonProperty("variantNames")
	public List<VariantModel> getVariants() {
		return variants;
	}

	@JsonProperty("variantNames")
	public void setVariants( List<VariantModel> theVariants ) {
		variants = theVariants;
	}

	@XmlElementWrapper(name="reps")
	@XmlElement(name="rep")
	@JsonProperty("reps")
	public List<PlaceRepresentationModel> getReps() {
		return reps;
	}

	@JsonProperty("reps")
	public void setReps( List<PlaceRepresentationModel> theReps ) {
		reps = theReps;
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
