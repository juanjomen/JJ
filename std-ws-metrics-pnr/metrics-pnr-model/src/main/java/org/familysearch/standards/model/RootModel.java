package org.familysearch.standards.model;

/**
 * Created by juanjomen on 2/8/2016.
 */

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.familysearch.standards.util.POJOMarshalUtil;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * The one -- and only -- marshal-able class for the web-service, representing both the
 * HTTP request and HTTP response body contents.  It's a wrapper for all possible input
 * or output values.
 *
 * @author wjohnson000
 *
 */
@XmlRootElement(name="metrics")
@JsonRootName(value="metrics")
public class RootModel {

  /** Custom content [mime] types for the application */
  public static final String    APPLICATION_XML_NAMES  = "application/standards-names-v2+xml";
  public static final String    APPLICATION_JSON_NAMES = "application/standards-names-v2+json";
  public static final String    METRICS_NAMESPACE        = "http://familysearch.org/standards/metrics/2.0";

  public static final String    NAMES_NAMESPACE        = "http://familysearch.org/standards/names/2.0";

  private TruthSetModel         truthSet;
  private List<TruthSetModel>   truthSets;

  private TruthModel            truth;
  private List<TruthModel>      truths;

  private TruthValueModel       truthValue;
  private List<TruthValueModel> truthValues;

  //private SearchResultsModel    searchResults; this may be the one for running it

  private List<LinkModel>       links;

  /**
   * Truth set represent the top-level retrieval of all truths.
   *
   * @return Returns the list of all truths
   */
  @XmlElementWrapper(name="truths")
  @XmlElement(name="truth")
  @JsonProperty("truths")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public List<TruthModel> getTruths() {
    return truths;
  }

  @JsonProperty("truths")
  public void setTruths(List<TruthModel> truths) {
    this.truths = truths;
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

  /**
   * Truth set represent the top-level retrieval of all truths.
   *
   * @return Returns the list of all truths
   */
  @XmlElementWrapper(name="truthValues")
  @XmlElement(name="truthValue")
  @JsonProperty("truthValues")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public List<TruthValueModel> getTruthsValues() {
    return truthValues;
  }

  @JsonProperty("truthValues")
  public void setTruthValues(List<TruthValueModel> truthValues) {
    this.truthValues = truthValues;
  }

  @XmlElement(name="truthValue")
  @JsonProperty("truthValue")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public TruthValueModel getTruthValue() {
    return truthValue;
  }

  @JsonProperty("truthValue")
  public void setTruthValue(TruthValueModel truthValue) {
    this.truthValue = truthValue;
  }


  @XmlElementWrapper(name="truthSets")
  @XmlElement(name="truthSet")
  @JsonProperty("truthSets")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public List<TruthSetModel> getTruthSets() {
    return truthSets;
  }

  @JsonProperty("truthSets")
  public void setTruthSets(List<TruthSetModel> truthSets) {
    this.truthSets = truthSets;
  }

  @XmlElement(name="truthSet")
  @JsonProperty("truthSet")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public TruthSetModel getTruthSet() {
    return truthSet;
  }

  @JsonProperty("truthSet")
  public void setTruthSet(TruthSetModel truthSet) {
    this.truthSet = truthSet;
  }

  @XmlElementWrapper(name="links")
  @XmlElement(name="link")
  @JsonProperty("links")
  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  public List<LinkModel> getLinks() { return links; }

  @JsonProperty("links")
  public void setLinks(List<LinkModel> links) { this.links = links; }


//  @XmlElement(name="search-results")
//  @JsonProperty("searchResults")
//  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//  public SearchResultsModel getSearchResults() {
//    return searchResults;
//  }
//
//  @JsonProperty("searchResults")
//  public void setSearchResults(SearchResultsModel searchResults) {
//    this.searchResults = searchResults;
//  }

  public String toXML() {
    return this.toString();
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

