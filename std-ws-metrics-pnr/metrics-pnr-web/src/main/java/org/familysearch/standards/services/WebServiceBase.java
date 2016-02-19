package org.familysearch.standards.services;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by juanjomen on 2/2/2016.
 */
public class WebServiceBase {

  //private static final Logger     LOGGER          = new Logger(WebServiceBase.class);
  protected static final String   MODULE_NAME     = "ws";
  private boolean                 isConfigured    = false;
  private ReadableMetricsService  readService;
  private WriteableMetricsService  writeService;
  private @Context ServletContext  context;
  /**
   * Retrieve the default 'ReadableNameService' instance
   * @return the default 'ReadableNameService' instance
   */
  protected ReadableMetricsService getReadableService() {
    checkConfiguration();
    return readService;
  }

  /**
   * Determine if the system has been configured.  If not, synchronously configure
   * it, ensuring that only one configuration takes place.  If two threads call this
   * simultaneously, they'll both see "isConfigured" as FALSE, but only one will
   * get past the "synchronized(this)" and initiate the configuration before
   * proceeding.  Once past the other thread will re-check the "isConfigured" flag
   * and skip the configuration.  All subsequent calls will not get past the very
   * first "isConfigured" check.
   */
  protected void checkConfiguration() {
    synchronized (this) {
      if (!isConfigured) {
        configureSystem();
        isConfigured = true;
      }
    }
  }

  /**
   * Configure the system, setting up the Readable data services which talks to the
   * underlying database service.  This method relies on the prior creation of the
   * data-source and data-service.
   */
  protected void configureSystem() {
    readService = (ReadableMetricsService)getContext().getAttribute("readableService");
//    LOGGER.info(null, MODULE_NAME, "Name-Readable-Service?", String.valueOf((readService != null)));
//
    writeService = (WriteableMetricsService)getContext().getAttribute("writeableService");
//    LOGGER.info(null, MODULE_NAME, "Name-Writable-Service?", String.valueOf((writeService != null)));
  }
  /**
   * Utility method to log a message in a standard format
   *
   * @param e exception, or null
   * @param endpoint URI endpoint
   * @param method HTTP method
   * @param status HTTP return status if available, else null
   * @param headers HTTP headers passed in to webservice call
   * @param params optional values to log, usually in pairs
   */
  protected void log(Exception e, String endpoint, String method, Integer status, HttpHeaders headers, String... params) {
    StringBuilder                    buf = new StringBuilder();
    Set<Map.Entry<String,List<String>>> headerEntries;

    buf.append("endpoint=");
    buf.append(endpoint);
    buf.append(" method=");
    buf.append(method);
    buf.append(" status=");
    buf.append(status);

    //Grab the header values:
    if ( headers != null && headers.getRequestHeaders() != null ) {
      headerEntries = headers.getRequestHeaders().entrySet();
      for ( Map.Entry<String,List<String>> entry : headerEntries ) {
        buf.append(' ');
        buf.append( entry.getKey() );
        buf.append( '=' );
        for ( String str : entry.getValue() ) {
          buf.append( str );
          buf.append( ',' );
        }
        //Strip the trailing comma
        if ( buf.charAt( buf.length() - 1 ) == ',' ) {
          buf.setLength( buf.length() - 1 );
        }
      }
    }

    for (int i = 0; i < params.length; i++) {
      buf.append(' ');
      buf.append(params[ i ]);
      buf.append("=\"");
      if (i + 1 < params.length) {
        buf.append(params[ i + 1 ]);
        buf.append( '\"' );
        i++;
      }
    }

    if (e != null) {
   //   LOGGER.error( e, MODULE_NAME, "call log", buf.toString() );
    } else {
  //    LOGGER.info( null, MODULE_NAME, "call log", buf.toString() );
    }
  }
  /**
   * @return the servlet context
   */
  protected ServletContext getContext() {
    return context;
  }
}
