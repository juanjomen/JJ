package org.familysearch.standards.place.ws.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.familysearch.standards.core.logging.Logger;


/**
 * Base class for the Exception Mappers. It provides a common logging experience.
 * 
 * @author wjohnson000
 */
public abstract class BaseMapper {
    private static final Logger  logger = new Logger(BaseMapper.class);

    public static final String      MODULE_NAME = "ws";

    /** Injected ServletRequest, with access to the HTTP method and URI */
    @Context HttpServletRequest request;


    /**
     * Output enough information to the LOG to make it useful for finding and fixing
     * problems.
     * 
     * @param errorType errorType
     * @param ex Exception that caused the issue, or possibly null ...
     */
    protected void logInfo(String errorType, Exception ex) {
        logger.info( ex, MODULE_NAME, ex.getMessage(), "type", errorType, "EndPoint", request.getRequestURI(), "Method", request.getMethod() );
    }

    /**
     * Output enough information to the LOG to make it useful for finding and fixing
     * problems.
     * 
     * @param errorType errorType
     * @param ex Exception that caused the issue, or possibly null ...
     */
    protected void logError(String errorType, Exception ex) {
        logger.error( ex, MODULE_NAME, ex.getMessage(), "type", errorType, "EndPoint", request.getRequestURI(), "Method", request.getMethod() );
    }
}
