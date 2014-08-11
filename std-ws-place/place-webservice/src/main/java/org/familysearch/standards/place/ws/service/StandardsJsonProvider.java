package org.familysearch.standards.place.ws.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import org.familysearch.standards.place.ws.model.RootModel;

/**
 * Jackson JSON requires a provider class for custom MIME types.
 * @author wjohnson000
 *
 */
@Provider
@Produces( RootModel.APPLICATION_JSON_PLACES )
@Consumes( RootModel.APPLICATION_JSON_PLACES )
public class StandardsJsonProvider extends JacksonJaxbJsonProvider {

}
