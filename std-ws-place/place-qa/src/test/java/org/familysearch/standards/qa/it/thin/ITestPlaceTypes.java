package org.familysearch.standards.qa.it.thin;

import com.sun.jersey.api.client.ClientResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.familysearch.standards.place.qa.clients.PlaceTypeClient;
import org.familysearch.standards.place.ws.model.TypeModel;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 1/7/14
 * Time: 12:34 PM
 *
 * @copyright 1/7/14 Intellectual Reserve, Inc. All rights reserved.
 */
@Test
public class ITestPlaceTypes {

  public void acquireListOfPlaceTypes() {
    PlaceTypeClient handler = new PlaceTypeClient();
    ClientResponse response = handler.get();
    assertNotNull(response);
    assertEquals(response.getClientResponseStatus(), ClientResponse.Status.OK);
    ObjectMapper mapper = new ObjectMapper();
    try {
      Map<String,TypeModel> placeTypes = mapper.readValue(response.getEntityInputStream(), new TypeReference<Map<String,TypeModel>>() {});
      assertNotNull(placeTypes);
    }
    catch (IOException e) {
      e.printStackTrace();

    }
  }

}
