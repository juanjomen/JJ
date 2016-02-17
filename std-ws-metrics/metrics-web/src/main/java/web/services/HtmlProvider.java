package web.services;


import web.model.RootModel;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


/**
 * Very simple marshaler that can handle plain text or plain html.  The response body will
 * simply be the JSON representation of the model.
 * 
 * @author wjohnson000
 *
 */
@Provider
@Produces({ MediaType.TEXT_PLAIN})
public class HtmlProvider implements MessageBodyWriter<RootModel> {

    /* (non-Javadoc)
     * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
     */
    @Override
    public long getSize(RootModel rootModel, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return rootModelToHMTL(rootModel).length;
    }

    /* (non-Javadoc)
     * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
     */
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == RootModel.class;
    }

    /* (non-Javadoc)
     * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)
     */
    @Override
    public void writeTo(RootModel rootModel, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {

        entityStream.write(rootModelToHMTL(rootModel));
    }

    /**
     * Return the bytes for the JSON representation of the model
     * 
     * @param rootModel incoming {@link RootModel} instance
     * @return
     */
    private byte[] rootModelToHMTL(RootModel rootModel) {
        return rootModel.toJSON().getBytes();
    }
}