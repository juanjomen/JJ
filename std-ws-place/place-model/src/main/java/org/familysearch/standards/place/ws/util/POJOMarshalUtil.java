package org.familysearch.standards.place.ws.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.familysearch.standards.core.logging.Logger;


/**
 * A set of utilities to marshal and un-marshal using either XML or JSON notation.
 * <p/>
 * NOTE: From a little testing it appears that creating new JSON reader and writer
 * instances each time is faster than synchronizing on pre-created instances.
 * However the reverse is true for the XML marshaller/unmarshaller instances, so
 * they are created once, saved in a map keyed by Class type, and all operations
 * are synchronized.
 * <p/>
 * NOTE 2: If we migrate to the "faster jackson" (v2.0+) the object readers and
 * writer are thread-safe so we could use single instances of them.
 * 
 * @author wjohnson000
 *
 */
public class POJOMarshalUtil {

    /** LOGGER */
    private static final Logger logger = new Logger(POJOMarshalUtil.class);

    /** Prefix to indicate that an error occurred in the marshaling process */
    public static final String ERROR_PREFIX = "ERROR: ";

    /** JSON object mapper */
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    /** XML object mapper */
    private static Map<Class<?>,Marshaller>   marshallerMap   = new HashMap<Class<?>,Marshaller>();
    private static Map<Class<?>,Unmarshaller> unmarshallerMap = new HashMap<Class<?>,Unmarshaller>();


    /**
     * Private constructor since this is a utility class
     */
    private POJOMarshalUtil() { }

    /**
     * Marshal an object to JSON.
     * 
     * @param model object to marshal
     * @return JSON representation of the object
     */
    public static String toJSON(Object model) {
        try {
            ObjectWriter jsonWriterPP = jsonMapper.writerWithDefaultPrettyPrinter();
            return jsonWriterPP.writeValueAsString(model);
        } catch (JsonGenerationException e) {
            return ERROR_PREFIX + e.getLocalizedMessage();
        } catch (JsonMappingException e) {
            return ERROR_PREFIX + e.getLocalizedMessage();
        } catch (IOException e) {
            return ERROR_PREFIX + e.getLocalizedMessage();
        }
    }

    /**
     * Un-marshal an object from JSON.
     * 
     * @param jsonString JSON representation of the object
     * @return a model object
     */
    public static <T> T fromJSON(String jsonString, Class<T> clazz) {
        try {
            ObjectReader reader = jsonMapper.reader(clazz);
            return reader.readValue(jsonString);
        } catch (JsonGenerationException e) {
            logger.error("Unable to unmarshal JSON: " + e.getLocalizedMessage());
        } catch (JsonMappingException e) {
            logger.error("Unable to unmarshal JSON: " + e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error("Unable to unmarshal JSON: " + e.getLocalizedMessage());
        }

        return null;
    }

    /**
     * Marshal an object to XML.
     * 
     * @param model object to marshal
     * @return XML representation of the object
     */
    public static String toXML(Object model) {
        try {
            Marshaller marshaller = marshallerMap.get(model.getClass());
            if (marshaller == null) {
                JAXBContext context = JAXBContext.newInstance(model.getClass());
                marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshallerMap.put(model.getClass(), marshaller);
            }

            synchronized(marshaller) {
                StringWriter writer = new StringWriter();
                marshaller.marshal(model, writer);

                return writer.toString();
            }
        } catch (JAXBException e) {
            return ERROR_PREFIX + e.getLocalizedMessage();
        }
    }

    /**
     * Un-marshal an object from XML.
     * 
     * @param jsonString XML representation of the object
     * @return a model object
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXML(String xmlString, Class<T> clazz) {
        try {
            Unmarshaller unmarshaller = unmarshallerMap.get(clazz);
            if (unmarshaller == null) {
                JAXBContext context = JAXBContext.newInstance(clazz);
                unmarshaller = context.createUnmarshaller();
                unmarshallerMap.put(clazz, unmarshaller);
            }

            synchronized(unmarshaller) {
                StringReader reader = new StringReader(xmlString);
                T model = (T)unmarshaller.unmarshal(reader);

                return model;
            }
        } catch (JAXBException e) {
            logger.debug("Unable to marshal: " + e.getMessage());
            return null;
        }
    }
}
