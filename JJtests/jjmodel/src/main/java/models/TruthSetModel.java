package models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import util.POJOMarshalUtil;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * Created by juanjomen on 1/11/2016.
 */
public class TruthSetModel {

  private Long              id;
  private String            name;
  private String            version;
  private String            type;
  private TruthModel truth;
  private List<TruthModel> truths;

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

  @XmlElement(name="name")
  @JsonProperty("name")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @XmlElement(name="version")
  @JsonProperty("version")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(String version) {
    this.version = version;
  }

  @XmlElement(name="type")
  @JsonProperty("type")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public String getType() {
    return type;
  }

  @JsonProperty("version")
  public void setType(String type) {
    this.type = type;
  }

  @XmlElement(name="truth")
  @JsonProperty("truth")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public TruthModel getTruth() {
    return truth;
  }

  @JsonProperty("truth")
  public void setTruth(TruthModel truth) {
    this.truth = truth;
  }

  @XmlElementWrapper(name="truths")
  @XmlElement(name="truths")
  @JsonProperty("truths")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public List<TruthModel> getTruths() {
    return truths;
  }

  @JsonProperty("truths")
  public void setTruths( List<TruthModel> truths ) {
    this.truths = truths;
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
