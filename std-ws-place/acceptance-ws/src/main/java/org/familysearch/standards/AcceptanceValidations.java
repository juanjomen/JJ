package org.familysearch.standards;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.familysearch.qa.testcommons.http.JerseyAssertImpl;
import org.testng.annotations.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collection;
import java.util.Iterator;

import static org.familysearch.qa.LogInfo.*;
import static org.testng.Assert.*;

/**
 * Acceptance Test to validate the service - Validations
 * User:
 * Date:
 */
public class AcceptanceValidations {

  private static final Logger LOGGER = LoggerFactory.getLogger(AcceptanceValidations.class);

  private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_XML_TYPE;

  private String service = "";
  private JerseyAssertImpl jersey;

  /**
   * Config Methods ***
   */
  @BeforeClass(alwaysRun = true)
  @Parameters({"baseUrl"})
  public void init(String baseUrl) {
    if (baseUrl == null || baseUrl.isEmpty()) {
      throw new IllegalArgumentException("Expected a non-null and non-empty serivce URL");
    }
    this.service = baseUrl.trim();
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Setting service: [" + getService() + "]." + logInfo());
    }
  }

  @AfterClass(alwaysRun = true)
  public void cleanup() throws Exception {
    //Add Suite teardown here
  }

  @BeforeMethod
  public void setUp() throws Exception {

    //Add test setup here
  }

  @AfterMethod
  public void tearDown() throws Exception {
    //Add test tear down here
  }


  @Test(groups = {"smoke", "acceptance"}, timeOut = 300000)
  public void httpGet_HealthCheckVital() {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Executing httpGet_HealthCheckVital() to determine Health State. Using service: [" + getService() + "]." + logInfo());
    }
    JerseyAssertImpl myRequest = getJersey();
    myRequest.setPrintStatus(true);
    myRequest.setPrintClientRequest(true);
    myRequest.assertGet("healthcheck/heartbeat", MEDIA_TYPE); // ensures 200 only
  }



  /**
   * Tests the Key Acceptance Criteria.
   */
  @Test(groups = {"acceptance"}, timeOut = 300000, dependsOnMethods = {"httpGet_HealthCheckVital"})
  public void httpGet_BasicTest() {
      // Add additional acceptance tests.
  }


  /**
   * Tests more robust acceptance criteria.
   */
  @Test(groups = {"acceptance"}, timeOut = 300000, dependsOnMethods = {"httpGet_HealthCheckVital"})
  public void httpGet_BasicSearchAcceptance() {
     // Add additional acceptance tests.
	 assertTrue(false, "test");
  }

  String getService() {
    return this.service;
  }


  JerseyAssertImpl getJersey() {
    if (this.jersey == null) {
      jersey = new JerseyAssertImpl(getService());
    }
    return jersey;
  }

  protected void setJersey(JerseyAssertImpl jersey) {
    this.jersey = jersey;
  }

}

