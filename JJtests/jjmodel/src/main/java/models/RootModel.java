package models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import util.POJOMarshalUtil;

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
@XmlRootElement(name="metrics", namespace=RootModel.METRICS_NAMESPACE)
@JsonRootName(value="names")
public class RootModel {

    /** Custom content [mime] types for the application */
    public static final String    APPLICATION_XML_NAMES  = "application/standards-metrics-v2+xml";
    public static final String    APPLICATION_JSON_NAMES = "application/standards-metrics-v2+json";
    public static final String    METRICS_NAMESPACE        = "http://familysearch.org/standards/metrics/2.0";

    private TruthModel            truth;
    private List<TruthModel>      truths;


    private TruthSetModel         truthSet;
    private List<TruthSetModel>   truthSets;

    /**
     * Types represent the top-level retrieval of alltruthSets.
     * 
     * @return Returns the list of all types
     */
    @XmlElementWrapper(name="truths")
    @XmlElement(name="truths")
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

    @JsonProperty("truthSet")
    public void setTruth(TruthModel truth) {
        this.truth = truth;
    }

    @XmlElementWrapper(name="truthSets")
    @XmlElement(name="truthSets")
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
