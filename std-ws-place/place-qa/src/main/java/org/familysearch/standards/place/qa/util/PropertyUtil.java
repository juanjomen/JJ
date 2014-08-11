package org.familysearch.standards.place.qa.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 12/16/13
 * Time: 2:01 PM
 *
 * @copyright 12/16/13 Intellectual Reserve, Inc. All rights reserved.
 */
public class PropertyUtil {
  static Properties properties = new Properties();
  private static final Properties users = new Properties();

  static {
    try {
      properties.load(PropertyUtil.class.getResourceAsStream("/test.properties"));
    }
    catch (IOException e) {
      throw (new RuntimeException("Unable to load test.properties.", e));
    }
  }

  public static Properties getProperties() {
    return properties;
  }

  public static URI getProperty(String property) {
    String propertyValue = properties.getProperty(property);
    if (propertyValue == null) {
      return null;
    }
    return URI.create(propertyValue);
  }

  public static URI getPlaceUri() {
    return getProperty("place.uri");
  }

  public static URI getHostUri() {
    return getProperty("host.uri");
  }


  public static Properties getUsers() {
    URI path = getProperty("api.users");
    InputStream userPropertiesStream = PropertyUtil.class.getResourceAsStream(path.getPath());

    //users = new Properties();
    try {
      users.load(userPropertiesStream);
    }
    catch (IOException e) {
      throw new IllegalStateException(e);
    }

    if (users.isEmpty()) {
      throw new IllegalStateException("No users specified.");
    }

    return users;
  }

  public static String getDefaultUser() {
    return properties.getProperty("default.api.user");
  }

  public static String getDefaultPassword() {
    return ((String) users.get(getDefaultUser()));
  }
}


