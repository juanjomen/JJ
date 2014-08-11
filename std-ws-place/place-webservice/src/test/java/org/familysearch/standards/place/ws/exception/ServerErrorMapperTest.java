package org.familysearch.standards.place.ws.exception;

import static org.testng.Assert.*;

import javax.ws.rs.core.Response;

import org.familysearch.standards.place.data.PlaceDataException;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class ServerErrorMapperTest extends BaseMapperUtil {

    ServerErrorMapper mapper;

    @BeforeMethod
    public void beforeMethod() {
        mapper = new ServerErrorMapper();
        setRequest(mapper);
    }

    @Test
    public void toResponse() {
        Response response = mapper.toResponse(new PlaceDataException("Dummy"));

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertNotNull(response.getEntity());
    }
}
