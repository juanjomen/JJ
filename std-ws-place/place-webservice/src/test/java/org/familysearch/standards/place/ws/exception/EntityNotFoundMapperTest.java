package org.familysearch.standards.place.ws.exception;

import static org.testng.Assert.*;

import javax.ws.rs.core.Response;

import org.familysearch.standards.place.data.NotFoundPlaceDataException;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class EntityNotFoundMapperTest extends BaseMapperUtil {

    EntityNotFoundMapper mapper;

    @BeforeMethod
    public void beforeMethod() {
        mapper = new EntityNotFoundMapper();
        setRequest(mapper);
    }

    @Test
    public void toResponse() {
        Response response = mapper.toResponse(new NotFoundPlaceDataException("Dummy"));

        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertNotNull(response.getEntity());
    }
}
