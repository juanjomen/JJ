package org.familysearch.standards.place.qa.clients;

import com.sun.jersey.api.client.WebResource;
import org.familysearch.standards.place.qa.jersey.MasterClient;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 3/31/14
 * Time: 2:04 PM
 *
 * @copyright 3/31/14 Intellectual Reserve, Inc. All rights reserved.
 */
public class DocumentationClient extends MasterClient {

  private URI ourResourceEndpoint;

  public DocumentationClient() {
    super();
    ourResourceEndpoint = UriBuilder.fromUri(PLACE_URI).build();
  }

  public WebResource getEndpointResource() {
    return super.getEndpointResource(ourResourceEndpoint);
  }

}

