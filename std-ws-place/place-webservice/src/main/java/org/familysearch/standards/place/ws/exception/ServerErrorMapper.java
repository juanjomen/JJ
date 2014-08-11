package org.familysearch.standards.place.ws.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.familysearch.standards.place.data.PlaceDataException;


/**
 * Custom mapper to turn the "{@link PlaceDataException}" into an appropriate HTTP
 * response, and log the error.
 * 
 * @author wjohnson000
 *
 */
@Provider
public class ServerErrorMapper extends BaseMapper implements ExceptionMapper<PlaceDataException> {

    @Override
    public Response toResponse(PlaceDataException ex) {
        this.logError("server_error", ex);
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ex.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
