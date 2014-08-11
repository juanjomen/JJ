package org.familysearch.standards.qa.it.thin;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.familysearch.standards.place.qa.clients.DocumentationClient;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 12/16/13
 * Time: 3:26 PM
 *
 * @copyright 12/16/13 Intellectual Reserve, Inc. All rights reserved.
 */
@Test
public class ITestHttpMethodsStdWsPlace {

  @Test
  public void httpGetMethodShouldReturnEnunciateDocumentation() {
    DocumentationClient client = new DocumentationClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.accept(MediaType.TEXT_HTML_TYPE).get(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getLength()).isGreaterThan(0);
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.OK);
  }

  @Test
  public void testHead() {
    DocumentationClient client = new DocumentationClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.head();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.OK);
  }

  @Test
  public void httpPostMethodShouldReturnEnunciateDocumentation() {
    DocumentationClient client = new DocumentationClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.accept(MediaType.TEXT_HTML_TYPE).post(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.OK);
  }

  @Test
  public void testPut() {
    DocumentationClient client = new DocumentationClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.put(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.FORBIDDEN);
  }

  @Test
  public void testDelete() {
    DocumentationClient client = new DocumentationClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.delete(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.FORBIDDEN);
  }

  @Test
  public void testOptions() {
    DocumentationClient client = new DocumentationClient();
    WebResource webResource = client.getEndpointResource();
    ClientResponse response = webResource.options(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.OK);
  }

}

