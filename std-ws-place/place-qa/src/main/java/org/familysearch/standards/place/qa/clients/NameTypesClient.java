package org.familysearch.standards.place.qa.clients;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.familysearch.standards.place.qa.jersey.MasterClient;
import org.familysearch.standards.place.ws.model.PlaceModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 4/1/14
 * Time: 3:44 PM
 *
 * @copyright 4/1/14 Intellectual Reserve, Inc. All rights reserved.
 */
public class NameTypesClient extends MasterClient {

  private URI ourResourceEndpoint;

  public NameTypesClient() {
    super();
    ourResourceEndpoint = UriBuilder.fromUri(PLACE_URI).path("/places/name-types").build();
  }

  public WebResource getEndpointResource() {
    return super.getEndpointResource(ourResourceEndpoint);
  }

  public ClientResponse get() {
    UriBuilder builder = UriBuilder.fromUri(ourResourceEndpoint);
    WebResource web = client.resource(builder.build());
    return web.get(ClientResponse.class);
  }

  public PlaceModel get(String theId) {
    ClientResponse rsp = getResponse(theId, new HashMap<String,Object>());
    if (ClientResponse.Status.OK == rsp.getClientResponseStatus()) {
      return rsp.getEntity(PlaceModel.class);
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

  public ClientResponse post(PlaceModel theNameType) {
    UriBuilder builder = UriBuilder.fromUri(ourResourceEndpoint);
    WebResource web = client.resource(builder.build());
    MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();
    return web.queryParams(parameters).entity(theNameType).post(ClientResponse.class);
  }

  public ClientResponse put(PlaceModel theNameType) {
    UriBuilder builder = UriBuilder.fromUri(ourResourceEndpoint).path(theNameType.getId().toString());
    WebResource web = client.resource(builder.build());
    MultivaluedMap<String, String> parameters = new MultivaluedMapImpl();
    return web.queryParams(parameters).entity(theNameType).put(ClientResponse.class);
  }

  public ClientResponse delete(String theId) {
    UriBuilder builder = UriBuilder.fromUri(ourResourceEndpoint).path(theId);
    WebResource web = client.resource(builder.build());
    return web.delete(ClientResponse.class);
  }

}

