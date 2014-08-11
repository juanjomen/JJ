package org.familysearch.standards.place.ws.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Catch-all exception mapper to properly handle all exceptions (note that this
 * won't catch errors).
 * 
 * @author dshellman
 */
@Provider
public class CatchAllMapper extends BaseMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse( Exception e ) {
        this.logError( "server_error", e );
        return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( "An unknown internal error occurred." ).type( MediaType.TEXT_PLAIN ).build();
    }
}
