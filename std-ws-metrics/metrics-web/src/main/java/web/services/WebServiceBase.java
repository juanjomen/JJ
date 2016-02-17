package web.services;

import api.services.ReadableMetricsService;
import api.services.WriteableMetricsService;
import org.familysearch.standards.core.logging.Logger;
import web.mapper.TruthMapper;
import web.mapper.TruthSetMapper;
import web.model.RootModel;
import web.util.POJOMarshalUtil;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import java.util.*;
import java.util.regex.Pattern;



/**
 * Base methods for all web endpoints.
 * 
 * @author wjohnson000
 *
 */
public class WebServiceBase {

    private static final Logger    LOGGER              = new Logger(WebServiceBase.class);
    private static final Pattern   PATTERN_REMOVE_CRLF = Pattern.compile("[\n\r]+");
    protected static final String  MODULE_NAME         = "ws";
    protected static final String  URL_ROOT            = "metrics";

    private @Context ServletContext context;

    private boolean                 isConfigured = false;
    private ReadableMetricsService  readService;
    private WriteableMetricsService writeService;

    /** Singletons for all your mapping needs */
    private TruthMapper             truthMapper = new TruthMapper();
    private TruthSetMapper          truthSetMapper = new TruthSetMapper();

    /**
     * Retrieve the default 'ReadableNameService' instance
     * @return the default 'ReadableNameService' instance
     */
    protected ReadableMetricsService getReadableService() {
        checkConfiguration();
        return readService;
    }

    /**
     * Retrieve the default 'WritableNameService' instance
     * @return the default 'WritableNameService' instance
     */
    protected WriteableMetricsService getWritableService() {
        checkConfiguration();
        return writeService;
    }

    /**
     * Methods to return the various mappers
     */
    protected TruthMapper getTruthMapper() {
        return truthMapper;
    }

    protected TruthSetMapper getTruthSetMapper() {
        return truthSetMapper;
    }



    /**
     * Pull the path from the URI-INFO
     * 
     * @param uriInfo uri-info
     * @return path
     */
    protected String getPath(UriInfo uriInfo) {
        StringBuilder buf = new StringBuilder();

        buf.append(uriInfo.getBaseUri().getPath());
        buf.append(URL_ROOT);
        buf.append("/");

        return buf.toString();
    }

    /**
     * Return a "{@link Date} instance one day in the future.
     * 
     * @return a date
     */
    protected Date getExpireDate() {
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.DAY_OF_YEAR, 1);
        return expires.getTime();
    }

    /**
     * Manufacture an entity-tag for a given object.
     * 
     * @param theId primary identifier
     * @param theSecondaryId secondary identifier, may be zero
     * @return
     */
    protected String getEntityTag(long theId, long theSecondaryId) {
        StringBuilder buf = new StringBuilder();

        buf.append(theId);
        buf.append("-");
        buf.append(theSecondaryId);

        return buf.toString();
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
        synchronized(this) {
            if (! isConfigured) {
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
        LOGGER.info(null, MODULE_NAME, "Name-Readable-Service?", String.valueOf((readService != null)));

        writeService = (WriteableMetricsService)getContext().getAttribute("writeableService");
        LOGGER.info(null, MODULE_NAME, "Name-Writable-Service?", String.valueOf((writeService != null)));
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
            LOGGER.error( e, MODULE_NAME, "call log", buf.toString() );
        } else {
            LOGGER.info( null, MODULE_NAME, "call log", buf.toString() );
        }
    }

  /*
   * Log the input to an endpoint, generally a PUT or POST, with the request payload.
   *
   * @param endpoint web end-point
   * @param method web method
   * @param model request contents
   * @param status HTTP return status
   * @param headers HTTP headers passed in to endpoint call
   */

    protected void log(String endpoint, String method, RootModel model, Integer status, HttpHeaders headers) {
        // Format the payload
        String modelJSON = "";
        if (model != null) {
            modelJSON = PATTERN_REMOVE_CRLF.matcher(POJOMarshalUtil.toJSONPlain(model)).replaceAll(" ");
        }

        log(null, endpoint, method, status, headers, "Request-Content-Model", modelJSON, "payload", "json");
    }

  /*
   * Log the input to an endpoint, generally a PUT or POST, with the request payload.
   *
   * @param endpoint web end-point
   * @param method web method
   * @param model request contents
   * @param status HTTP return status
   * @param headers HTTP headers passed in to endpoint call
   */

    protected void log(Exception e, String endpoint, String method, RootModel model, Integer status, HttpHeaders headers) {
        // Format the payload
        String modelJSON = "";
        if (model != null) {
            modelJSON = PATTERN_REMOVE_CRLF.matcher(POJOMarshalUtil.toJSONPlain(model)).replaceAll(" ");
        }

        log(e, endpoint, method, status, headers, "Request-Content-Model", modelJSON, "payload", "json");
    }

    /**
     * @return the servlet context
     */
    protected ServletContext getContext() {
        return context;
    }
}
