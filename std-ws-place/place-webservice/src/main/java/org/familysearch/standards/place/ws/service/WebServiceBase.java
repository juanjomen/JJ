package org.familysearch.standards.place.ws.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.familysearch.standards.core.StdLocale;
import org.familysearch.standards.core.logging.Logger;
import org.familysearch.standards.place.PlaceRequest;
import org.familysearch.standards.place.PlaceService;
import org.familysearch.standards.place.access.PlaceDataServiceImpl;
import org.familysearch.standards.place.data.ReadablePlaceDataService;
import org.familysearch.standards.place.data.WritablePlaceDataService;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.search.DefaultPlaceRequestProfile;
import org.familysearch.standards.place.service.DbDataService;


/**
 * The "places" web service provides endpoints to support the searching and retrieval of place and related
 * resources.  The "search" endpoint provides the ability to search for Place Representation entities by
 * using a variety of optional search parameters.  Additional endpoints provide direct access to Place
 * and Place Representation resources, as well as Place Types, Variant Name Types, and Type Groups.
 * 
 * @author dshellman, wjohnson000
 */
public abstract class WebServiceBase {
	private static final Logger		logger = new Logger(WebServiceBase.class);

	protected static final String	MODULE_NAME = "ws";
	protected static final String   URL_ROOT = "places";
	protected static final String   SEARCH_ROOT = "request/";

	protected static final String   VERSION_PROPS = "/ws-version.properties";
	protected static final String   VERSION_PROP_NAME = "current.ws.version";

	protected static final Map<String,PlaceRequest.FuzzyType>		fuzzyLookup = new HashMap<String,PlaceRequest.FuzzyType>();
	protected static final Map<String,PlaceRequest.PublishedType> 	pubTypes = new HashMap<String,PlaceRequest.PublishedType>();
	protected static final Map<String,PlaceRequest.ValidatedType> 	validatedTypes = new HashMap<String,PlaceRequest.ValidatedType>();

	static {
		fuzzyLookup.put( "ed", PlaceRequest.FuzzyType.EDIT_DISTANCE);
		//TODO:  The fuzzy lookups below should be removed when all clients stop
		//using them, since they are deprecated.
		fuzzyLookup.put("ed_40", PlaceRequest.FuzzyType.EDIT_DISTANCE);
		fuzzyLookup.put("ed_50", PlaceRequest.FuzzyType.EDIT_DISTANCE);
		fuzzyLookup.put("ed_60", PlaceRequest.FuzzyType.EDIT_DISTANCE);
		fuzzyLookup.put("ed_70", PlaceRequest.FuzzyType.EDIT_DISTANCE);
		fuzzyLookup.put("ed_80", PlaceRequest.FuzzyType.EDIT_DISTANCE);
		fuzzyLookup.put("ed_90", PlaceRequest.FuzzyType.EDIT_DISTANCE);
		pubTypes.put("pub_only", PlaceRequest.PublishedType.PUB_ONLY);
		pubTypes.put("non_pub_only", PlaceRequest.PublishedType.NON_PUB_ONLY);
		pubTypes.put("pub_non_pub", PlaceRequest.PublishedType.PUB_AND_NON_PUB);
		validatedTypes.put("val_only", PlaceRequest.ValidatedType.VALIDATED_ONLY);
		validatedTypes.put("non_val_only", PlaceRequest.ValidatedType.NON_VALIDATED_ONLY);
		validatedTypes.put("val_non_val", PlaceRequest.ValidatedType.VALIDATED_AND_NON_VALIDATED);
	}

    protected @Context ServletContext	context;

	protected boolean					isConfigured = false;

	protected WritablePlaceDataService	writableService;
	protected ReadablePlaceDataService	readableService;
	protected SolrDataService			solrService;
	protected DbDataService				dbService;
	protected String					wsVersion;
	protected PlaceService				placeService;


	/**
	 * Constructor ... all set-up has been moved to the "configureSystem()" method, which
	 * is called lazily whenever any of the services are called for.
	 */
	public WebServiceBase() {
	    logger.info( null, MODULE_NAME, "Starting WS Endpoints.", "class", this.getClass().getSimpleName() );
	}

	/**
	 * Determines the locale based upon the given parameters.  The first parameter
	 * represents the "override" for the locale, while the headerLang represents
	 * the default.
	 * 
	 * @param lang The override parameter for the locale.
	 * @param headerLang The default parameter for the locale.
	 * 
	 * @return Returns an instance of StdLocale.  This won't be null.
	 */
	protected StdLocale getLocale(String lang, String headerLang) {
		StdLocale locale;

		if (lang != null  &&  lang.trim().length() == 0) {
		    locale = new StdLocale("en");
		} else if (lang != null) {
			locale = new StdLocale(lang);
		} else if (headerLang != null   &&  headerLang.trim().length() > 0) {
			Collection<StdLocale> locales = StdLocale.getLocales(headerLang);
			if (locales.size() > 0) {
				locale = locales.iterator().next();
			} else {
				locale = new StdLocale("en");
			}
		} else {
			locale = new StdLocale("en");
		}

		return locale;
	}

	protected String getPath(UriInfo uriInfo) {
		StringBuilder buf = new StringBuilder();

		buf.append(uriInfo.getBaseUri().getPath());
		buf.append(URL_ROOT);
		buf.append("/");

		return buf.toString();
	}

	protected String getEntityTag(int theId, int theSecondaryId) {
		StringBuilder buf = new StringBuilder();

		buf.append(theId);
		buf.append("-");
		buf.append(theSecondaryId);

		return buf.toString();
	}

	//
	// =========================================================================================
	// These methods will be used for accessing the various services.  It adds a degree of
	// loose coupling while incurring no performance penalty.  They also allow for mocks to
	// be created and used during unit testing.  They are PROTECTED to allow unit tests to
	// gain access to them.  A minor trade-off.
    // =========================================================================================
	//

	/**
	 * Retrieve the 'WritablePlaceDataService' instance, lazily instantiated.
	 * @return the 'WritablePlaceDataService' instance
	 */
	protected WritablePlaceDataService getWritableService() {
	    checkConfiguration();
	    return writableService;
	}

    /**
     * Retrieve the 'ReadablePlaceDataService' instance, lazily instantiated.
     * @return the 'ReadablePlaceDataService' instance
     */
    protected ReadablePlaceDataService getReadableService() {
        checkConfiguration();
        return readableService;
    }

    /**
     * Retrieve the 'SolrDataService' instance, lazily instantiated.
     * @return the 'SolrDataService' instance
     */
    protected SolrDataService getSolrService() {
        checkConfiguration();
        return solrService;
    }

    /**
     * Retrieve the 'DbDataService' instance, lazily instantiated.
     * @return the 'DbDataService' instance
     */
    protected DbDataService getDbService() {
        checkConfiguration();
        return dbService;
    }

    /**
     * Retrieve the 'Version' (String) instance, lazily instantiated.
     * @return the 'Version' value
     */
    protected String getWsVersion() {
        checkConfiguration();
        return wsVersion;
    }

    /**
     * Retrieve the default 'PlaceService' instance
     * @return the default 'PlaceService' instance
     */
    protected PlaceService getPlaceService() {
    	checkConfiguration();
        return placeService;
    }

    /**
     * Retrieve a named 'PlaceService' instance
     * @return a named 'PlaceService' instance
     */
    protected PlaceService getPlaceService(String profName) {
    	checkConfiguration();
        return PlaceService.getInstance(profName);
    }

    /**
     * Return a "{@link java.util.Date} instance one day in the future.
     * 
     * @return a date
     */
    protected Date getExpireDate() {
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.DAY_OF_YEAR, 1);
        return expires.getTime();
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
	 * Configure the system, setting up the Readable- and Writable data services,
	 * the underlying database service and solr services.  This method relies on
	 * prior creation of the two basic services (DB and Solr).
	 */
	protected void configureSystem() {
	    PlaceDataServiceImpl  shimService;
	    Properties            props = new Properties();
	    InputStream           is = null;

	    solrService = (SolrDataService)context.getAttribute("solrService");
	    dbService   = (DbDataService)context.getAttribute("dbService");

	    shimService = new PlaceDataServiceImpl(dbService, solrService);
	    writableService = shimService;
	    readableService = shimService;
	    placeService = PlaceService.getInstance(new DefaultPlaceRequestProfile(readableService));

	    try {
	        is = getClass().getResourceAsStream(VERSION_PROPS);
	        if (is == null) {
	        	logger.error( null, MODULE_NAME, "Unable to find version properties file.", "file", VERSION_PROPS );
	            wsVersion = "Not Configured";
	        } else {
	            props.load(is);
	            wsVersion = props.getProperty(VERSION_PROP_NAME, "Not Configured");
	        }
	    } catch (IOException e) {
	    	logger.error( e, MODULE_NAME, "Error loading Web Service version properties file.", "file", VERSION_PROPS );
	        wsVersion = "Unknown";
	    } finally {
	        if (is != null) {
	            try {
	                is.close();
	            } catch(IOException ex) {
	            	logger.error( ex, MODULE_NAME, "Unable to close file.", "file", VERSION_PROPS );
	            }
	        }
	    }
	}

	protected void log(Exception e, String endpoint, String method, String... params) {
		StringBuilder buf = new StringBuilder();

		buf.append("endpoint=");
		buf.append(endpoint);
		buf.append(" method=");
		buf.append(method);

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
			logger.error( e, MODULE_NAME, "", buf.toString() );
		} else {
			logger.info( null, MODULE_NAME, "", buf.toString() );
		}
	}
}
