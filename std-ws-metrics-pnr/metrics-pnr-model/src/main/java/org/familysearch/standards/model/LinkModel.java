package org.familysearch.standards.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by juanjomen on 2/8/2016.
 */
@XmlRootElement(name="link", namespace=LinkModel.ATOM_NAMESPACE)
@JsonRootName(value="link")
public class LinkModel {

  public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";

  private String  rel;    // Link Relations, required for a valid link
  private String  href;   // URI of the resource, required for a valid link
  private String  title;  // Human-readable title, optionsl

  @XmlAttribute(name="href")
  @JsonProperty("href")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public String getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(String theHref) {
    href = theHref;
  }

  @XmlAttribute(name="rel")
  @JsonProperty("rel")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public String getRel() {
    return rel;
  }

  @JsonProperty("rel")
  public void setRel(String theRel) {
    rel = theRel;
  }

  @XmlAttribute(name="title")
  @JsonProperty("title")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String theTitle) {
    title = theTitle;
  }
}
