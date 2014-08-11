package org.familysearch.standards.qa.it.thin;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.familysearch.standards.place.qa.clients.AttributeTypesClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 4/1/14
 * Time: 4:04 PM
 *
 * @copyright 4/1/14 Intellectual Reserve, Inc. All rights reserved.
 */

@Test
public class ITestHttpMethodsAttributeTypes {

  private WebResource webResource;
  private AttributeTypesClient client;

  @BeforeClass
  public void setUp() {
    client = new AttributeTypesClient();
    webResource = client.getEndpointResource();
  }

  @Test
  public void testOptionsVerb() {
    ClientResponse response = webResource.options(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.NO_CONTENT);
  }

  @Test
  public void testGetVerb() {
    ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.OK);
    response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.OK);
  }

  @Test
  public void testHeadVerb() {
    ClientResponse response = webResource.head();
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.OK);
  }

  @Test
  public void testPostVerb() {
    ClientResponse response = webResource.post(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.UNSUPPORTED_MEDIA_TYPE);
  }

  @Test
  public void testPutVerb() {
    ClientResponse response = webResource.put(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.METHOD_NOT_ALLOWED);
  }

  @Test
  public void testDeleteVerb() {
    ClientResponse response = webResource.delete(ClientResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.getClientResponseStatus()).isEqualTo(ClientResponse.Status.METHOD_NOT_ALLOWED);
  }

}

