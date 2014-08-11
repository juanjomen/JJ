package org.familysearch.standards.place.qa.clients;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.familysearch.standards.place.qa.jersey.MasterClient;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 1/7/14
 * Time: 11:31 AM
 *
 * @copyright 1/7/14 Intellectual Reserve, Inc. All rights reserved.
 */
public class PlaceTypeClient extends MasterClient {
  private URI ourResourceEndpoint;

  public WebResource getEndpointResource() {
    return super.getEndpointResource(ourResourceEndpoint);
  }

  public PlaceTypeClient() {
    super();
    ourResourceEndpoint = UriBuilder.fromUri(PLACE_URI).path("places").path("place-types").build();
  }

  public URI getBaseUri() {
    return ourResourceEndpoint;
  }

  private WebResource getWebResource(URI uri) {
    WebResource web = client.resource(uri);
    return web;
  }

  public ClientResponse get() {
    WebResource webResource = getWebResource(getBaseUri());
    final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
    return response;
  }

}
