package org.familysearch.standards.place.qa.jersey;

import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import org.apache.log4j.Logger;
import org.familysearch.standards.place.qa.util.PropertyUtil;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriBuilder;
import javax.xml.ws.WebServiceException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Fail.fail;

/**
 * @author : Gaylene Scholes
 * @date : 16 Dec 2013
 */
@SuppressWarnings({"UnusedDeclaration"})
public abstract class MasterClient {
  protected final static Logger LOG = Logger.getLogger(MasterClient.class.getName());

  //  protected final static String DEVELOPMENT_KEY = PropertyUtil.getDevelopmentKey();
  protected static final int CREATED = ClientResponse.Status.CREATED.getStatusCode();

  public final static URI PLACE_URI = PropertyUtil.getPlaceUri();

  protected static Client client;
  protected static Client noCookieClient;
  private static ThreadLocal<Map<String, NewCookie>> threadLocalCookies = new ThreadLocal<Map<String, NewCookie>>();

  static {
    checkTestProperties();
    createJerseyClients();
  }

  private static void checkTestProperties() {
    if (PLACE_URI == null) {
      fail("Test Configuration Error: One or more missing properties. Check place.uri");
    }
  }

  private static void createJerseyClients() {
    DefaultClientConfig cc = createDefaultClientConfig();
    client = createCookieClient(cc);
    noCookieClient = createBasicClient(cc);
  }

  private static DefaultClientConfig createDefaultClientConfig() {
    DefaultClientConfig cc = new DefaultClientConfig();
    cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    cc.getFeatures().put(ClientConfig.FEATURE_DISABLE_XML_SECURITY, true);
    LOG.debug("DISABLE_XML_SECURITY=" + cc.getFeature(ClientConfig.FEATURE_DISABLE_XML_SECURITY));
    cc.getProperties().put(ClientConfig.PROPERTY_BUFFER_RESPONSE_ENTITY_ON_EXCEPTION, true);
    LOG.debug("BUFFER_ENTITY_ON_EXCEPTION=" + cc.getPropertyAsFeature(ClientConfig.PROPERTY_BUFFER_RESPONSE_ENTITY_ON_EXCEPTION));
    LOG.debug("DISABLE_XML_SECURITY=" + cc.getPropertyAsFeature(ClientConfig.FEATURE_DISABLE_XML_SECURITY));
    return cc;
  }

  private static Client createCookieClient(DefaultClientConfig cc) {
    Client client = createBasicClient(cc);
    ClientFilter cookieFilter = buildCookieFilter();
    client.addFilter(cookieFilter);
    return client;
  }

  protected static Client createBasicClient(DefaultClientConfig cc) {
    Client client = Client.create(cc);
    client.setFollowRedirects(true);
    client.addFilter(new MasterClientRequestFilter(LOG));
    return client;
  }

  protected static ClientFilter buildCookieFilter() {
    return new ClientFilter() {
      @Override
      public ClientResponse handle(ClientRequest request) throws ClientHandlerException {
        Map<String, NewCookie> cookies = threadLocalCookies.get();
        if (cookies != null && !cookies.isEmpty()) {
          for (Map.Entry<String, NewCookie> each : cookies.entrySet()) {
            request.getHeaders().add("Cookie", each.getValue());
          }
        }

        ClientResponse response = getNext().handle(request);
        NewCookie sessionId = null;
        if (cookies != null) {
          sessionId = cookies.get("fssessionid");
        }
        saveCookies(response.getCookies());
        if (sessionId != null) {
          cookies = threadLocalCookies.get();
          cookies.put("fssessionid", sessionId);
        }
        return response;
      }
    };
  }

  public static void addDefaultCookie(NewCookie cookie) {
    Map<String, NewCookie> cookies = threadLocalCookies.get();
    cookies.put(cookie.getName(), cookie);
  }

  public void addClientLogging() {
    client.addFilter(new LoggingFilter());
    noCookieClient.addFilter(new LoggingFilter());
  }

  //this is for the benefit of getting tossCookies into the build.
  public void tossCookies() {
    Map<String, NewCookie> cookies = threadLocalCookies.get();
    if (cookies != null) {
      cookies.clear();
    }
  }

  private static void saveCookies(List<NewCookie> cookies) {
    threadLocalCookies.set(new HashMap<String, NewCookie>());
    for (NewCookie cookie : cookies) {
      addDefaultCookie(cookie);
    }
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public String getDefaultSessionId() {
    Map<String, NewCookie> cookies = threadLocalCookies.get();
    NewCookie cookie = cookies.get("fssessionid");
    return cookie == null ? null : cookie.getValue();
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public void removeDefaultSessionId() {
    Map<String, NewCookie> cookies = threadLocalCookies.get();
    if (cookies.containsKey("fssessionid")) {
      cookies.remove("fssessionid");
    }
  }

  /**
   * get the id from the client response
   *
   * @param clientResponse response containing the id
   * @return the relationship id from the response
   */
  public String getIdFromClientResponse(ClientResponse clientResponse) {
    return clientResponse.getHeaders().getFirst("X-Entity-ID");
  }

  /**
   * add an X-Reason header to the given resource
   *
   * @param resource the web request resource
   * @param content  the reason to add
   * @return a builder for a web resource that has the appropriate header
   */
  protected WebResource.Builder addReasonHeader(WebResource resource, String content) {
    try {
      String encodedContent = null;
      if (content != null) {
        encodedContent = URLEncoder.encode(content, "UTF-8");
      }
      return resource.header("X-Reason", encodedContent);
    }
    catch (UnsupportedEncodingException e) {
      throw new WebServiceException("The specified URL encoding is not supported for this request header.");
    }
  }

  protected WebResource getEndpointResource(URI theEndpoint) {
    return client.resource(UriBuilder.fromUri(theEndpoint).build());
  }
}
