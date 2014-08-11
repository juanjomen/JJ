package org.familysearch.standards.place.ws.exception;

import static org.testng.Assert.*;

import javax.ws.rs.core.Response;

import org.familysearch.standards.place.data.InvalidPlaceDataException;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class BadRequestMapperTest extends BaseMapperUtil {

    BadRequestMapper mapper;

    @BeforeMethod
    public void beforeMethod() {
        mapper = new BadRequestMapper();
        setRequest(mapper);
    }

    @Test
    public void toResponse() {
        Response response = mapper.toResponse(new InvalidPlaceDataException("Dummy"));

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        assertNotNull(response.getEntity());
    }
}
