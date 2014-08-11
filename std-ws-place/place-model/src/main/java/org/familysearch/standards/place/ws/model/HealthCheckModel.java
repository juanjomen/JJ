package org.familysearch.standards.place.ws.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement
@XmlType(propOrder={ "APIVersion", "WSVersion", "currentRevision", "status", "links" })
public class HealthCheckModel {

    private String			apiVersion;
    private String			wsVersion;
    private String          status;
    private Integer			revisionNum;
    private List<LinkModel>	links = new ArrayList<LinkModel>();

    public HealthCheckModel() {}


    @JsonProperty("apiVersion")
    public void setAPIVersion( String version ) {
        apiVersion = version;
    }

    @XmlElement(name="api-version")
    @JsonProperty("apiVersion")
    public String getAPIVersion() {
        return apiVersion;
    }

    @JsonProperty("wsVersion")
    public void setWSVersion( String version ) {
        wsVersion = version;
    }

    @XmlElement(name="ws-version")
    @JsonProperty("wsVersion")
    public String getWSVersion() {
        return wsVersion;
    }

    @JsonProperty("status")
    public void setStatus( String status ) {
        this.status = status;
    }

    @XmlElement(name="status")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("currentRevision")
    public void setCurrentRevision( Integer rev ) {
        revisionNum = rev;
    }

    @XmlElement(name="current-revision")
    @JsonProperty("currentRevision")
    public Integer getCurrentRevision() {
        return revisionNum;
    }

    @XmlElementWrapper(name="links")
    @XmlElement(name="link")
    @JsonProperty("links")
    public List<LinkModel> getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks( List<LinkModel> theLinks ) {
        links = theLinks;
    }
}
