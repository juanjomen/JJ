package org.familysearch.standards.qa.it.thin;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.familysearch.standards.place.qa.clients.PlaceRepClient;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 4/2/14
 * Time: 3:50 PM
 *
 * @copyright 4/2/14 Intellectual Reserve, Inc. All rights reserved.
 */

@Test
public class ITestHttpMethodsPlaceRepClient {

  @Test
  public void testOptionsVerb() {
    PlaceRepClient client = new PlaceRepClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.options(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.NO_CONTENT);
  }

  @Test
  public void testGetVerb() {
    PlaceRepClient client = new PlaceRepClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.METHOD_NOT_ALLOWED);
    response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.METHOD_NOT_ALLOWED);
  }

  @Test
  public void testHeadVerb() {
    PlaceRepClient client = new PlaceRepClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.head();
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.METHOD_NOT_ALLOWED);
  }

  @Test
  public void testPostVerb() {
    PlaceRepClient client = new PlaceRepClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.post(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.METHOD_NOT_ALLOWED);
  }

  @Test
  public void testPutVerb() {
    PlaceRepClient client = new PlaceRepClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.put(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.METHOD_NOT_ALLOWED);
  }

  @Test
  public void testDeleteVerb() {
    PlaceRepClient client = new PlaceRepClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.delete(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.METHOD_NOT_ALLOWED);
  }

}

