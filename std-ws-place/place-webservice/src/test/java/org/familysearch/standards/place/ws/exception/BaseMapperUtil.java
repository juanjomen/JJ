package org.familysearch.standards.place.ws.exception;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;


public class BaseMapperUtil {
    /**
     * Set the "HttpServletRequest" in the given mapper.  Create a mock for the HTTP
     * Servlet Request instance.
     * 
     * @param mapper mapper instance
     */
    protected static void setRequest(BaseMapper mapper) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/app/something");

        Class<?> clazz = mapper.getClass();
        while (clazz != null) {
            try {
                Field contextField = clazz.getDeclaredField("request");
                contextField.setAccessible(true);
                contextField.set(mapper, request);
            } catch(Exception ex) {
                ;  // Do nothing ...
            }
            clazz = clazz.getSuperclass();
        }
    }
}
