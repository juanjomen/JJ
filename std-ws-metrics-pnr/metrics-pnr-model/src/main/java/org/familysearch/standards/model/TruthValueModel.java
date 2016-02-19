package org.familysearch.standards.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.familysearch.standards.util.POJOMarshalUtil;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by juanjomen on 2/8/2016.
 */
@XmlRootElement
public class TruthValueModel {

  private Long       id;
  private Long       truth_Id;
  private String     value;
  private String     contextValue;
  private Long       score;
  private Boolean    isPositive;

  @XmlAttribute(name="id")
  @JsonProperty("id")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public Long getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Long id) {
    this.id = id;
  }

  @XmlElement(name="truth-Id")
  @JsonProperty("truth-Id")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public Long getTruth_Id() {
    return truth_Id;
  }

  @JsonProperty("truth-Id")
  public void setTruth_Id(Long truth_Id) {
    this.truth_Id = truth_Id;
  }

  @XmlElement(name="contextValue")
  @JsonProperty("contextValue")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public String getContextValue() {
    return contextValue;
  }

  @JsonProperty("contextValue")
  public void setContextValue(String contextValue) {
    this.contextValue = contextValue;
  }

  @XmlElement(name="value")
  @JsonProperty("value")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public String getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(String value) {
    this.value = value;
  }

  @XmlElement(name="score")
  @JsonProperty("score")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public Long getScore() {
    return score;
  }

  @JsonProperty("score")
  public void setScore(Long score) {
    this.score = score;
  }


  @XmlElement(name="isPositive")
  @JsonProperty("isPositive")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public Boolean getIsPositive() {
    return isPositive;
  }

  @JsonProperty("isPOsitive")
  public void setIsPositive(Boolean isPositive) {
    this.isPositive = isPositive;
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
