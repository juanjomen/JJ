package org.familysearch.standards.place.qa.clients;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.familysearch.standards.place.qa.jersey.MasterClient;
import org.familysearch.standards.place.ws.model.CitationModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 3/13/14
 * Time: 2:45 PM
 *
 * @copyright 3/13/14 Intellectual Reserve, Inc. All rights reserved.
 */
public class CitationTypesClient extends MasterClient {

  private URI ourResourceEndpoint;

  public CitationTypesClient() {
    super();
    ourResourceEndpoint = UriBuilder.fromUri(PLACE_URI).path("/places/citation-types").build();
  }

  public WebResource getEndpointResource() {
    return super.getEndpointResource(ourResourceEndpoint);
  }

  public ClientResponse get() {
    UriBuilder builder = UriBuilder.fromUri(ourResourceEndpoint);
    WebResource web = client.resource(builder.build());
    return web.get(ClientResponse.class);
  }

  public CitationModel get(String theId) {
    ClientResponse rsp = getResponse(theId, new HashMap<String,Object>());
    if (ClientResponse.Status.OK == rsp.getClientResponseStatus()) {
      return rsp.getEntity(CitationModel.class);
    }
    return null;
  }

  public ClientResponse getResponse(String theId, Map<String, Object> headerParameters) {
    UriBuilder uriBuilder = UriBuilder.fromUri(ourResourceEndpoint).path(theId);
    WebResource web = client.resource(uriBuilder.build());
    MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();
    WebResource.Builder builder = web.queryParams(parameters).getRequestBuilder();
    for (Map.Entry<String, Object> entry : headerParameters.entrySet()) {
      builder = builder.header(entry.getKey(), entry.getValue());
    }
    return builder.get(ClientResponse.class);
  }

  public ClientResponse post(CitationModel theCitation) {
    UriBuilder builder = UriBuilder.fromUri(ourResourceEndpoint);
    WebResource web = client.resource(builder.build());
    MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();
    return web.queryParams(parameters).entity(theCitation).post(ClientResponse.class);
  }

  public ClientResponse put(CitationModel theCitation) {
    UriBuilder builder = UriBuilder.fromUri(ourResourceEndpoint).path(theCitation.getId().toString());
    WebResource web = client.resource(builder.build());
    MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();
    return web.queryParams(parameters).entity(theCitation).put(ClientResponse.class);
  }

  public ClientResponse delete(String theId) {
    UriBuilder builder = UriBuilder.fromUri(ourResourceEndpoint).path(theId);
    WebResource web = client.resource(builder.build());
    return web.delete(ClientResponse.class);
  }

}

