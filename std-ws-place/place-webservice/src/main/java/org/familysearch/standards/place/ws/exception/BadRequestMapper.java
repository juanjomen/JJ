package org.familysearch.standards.place.ws.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.familysearch.standards.place.data.InvalidPlaceDataException;


/**
 * Custom mapper to turn the "{@link InvalidPlaceDataException}" into an appropriate
 * HTTP response, and log the error.
 * 
 * @author wjohnson000
 *
 */
@Provider
public class BadRequestMapper extends BaseMapper implements ExceptionMapper<InvalidPlaceDataException> {

    @Override
    public Response toResponse(InvalidPlaceDataException ex) {
        this.logError("user_error", ex);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ex.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
