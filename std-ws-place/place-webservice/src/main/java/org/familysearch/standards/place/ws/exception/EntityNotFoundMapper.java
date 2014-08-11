package org.familysearch.standards.place.ws.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.familysearch.standards.place.data.NotFoundPlaceDataException;


/**
 * Custom mapper to turn the "{@link NotFoundPlaceDataException}" into an appropriate
 * HTTP response, and log the error.
 * 
 * @author wjohnson000
 *
 */
@Provider
public class EntityNotFoundMapper extends BaseMapper implements ExceptionMapper<NotFoundPlaceDataException> {

    /* (non-Javadoc)
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(NotFoundPlaceDataException ex) {
        this.logInfo("not_found_error", ex);
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(ex.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
