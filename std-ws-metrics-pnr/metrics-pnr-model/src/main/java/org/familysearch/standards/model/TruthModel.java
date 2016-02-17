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
public class TruthModel {
  private Long       id;
  private Long       set_Id;
  private String     value;
  private String     contextValue;

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

  @XmlElement(name="set-Id")
  @JsonProperty("set-Id")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public Long getSet_Id() {
    return set_Id;
  }

  @JsonProperty("set-Id")
  public void setSet_Id(Long set_Id) {
    this.set_Id = set_Id;
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
