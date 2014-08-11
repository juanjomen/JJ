package org.familysearch.standards.place.qa.jersey;

import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.ReaderWriter;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.util.List;
import java.util.Map;


/**
 * A logging filter.
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class MasterClientRequestFilter extends ClientFilter {

  private static final Logger LOGGER = Logger.getLogger(MasterClientRequestFilter.class.getName());

  private static final String NOTIFICATION_PREFIX = "- ";

  private static final String REQUEST_PREFIX = " > ";

  private static final String RESPONSE_PREFIX = " < ";

  private final class Adapter extends AbstractClientRequestAdapter {

    private final StringBuilder b;

    Adapter(ClientRequestAdapter cra, StringBuilder b) {
      super(cra);
      this.b = b;
    }

    public OutputStream adapt(ClientRequest request, OutputStream out) throws IOException {
      return new LoggingOutputStream(getAdapter().adapt(request, out), b);
    }

  }

  private final class LoggingOutputStream extends OutputStream {

    private final OutputStream out;

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private final StringBuilder b;

    LoggingOutputStream(OutputStream out, StringBuilder b) {
      this.out = out;
      this.b = b;
    }

    @Override
    public void write(byte[] b) throws IOException {
      baos.write(b);
      out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      baos.write(b, off, len);
      out.write(b, off, len);
    }

    @Override
    public void write(int b) throws IOException {
      baos.write(b);
      out.write(b);
    }

    @Override
    public void close() throws IOException {
      printEntity(b, baos.toByteArray());
      log(b);
      out.close();
    }
  }

  private final PrintStream loggingStream;

  private final Logger logger;

  private long _id = 0;

  /**
   * Create a logging filter logging the request and response to a default JDK logger, named as the fully qualified class name of this class.
   */
  public MasterClientRequestFilter() {
    this(LOGGER);
  }

  /**
   * Create a logging filter logging the request and response to a JDK logger.
   *
   * @param logger the logger to log requests and responses.
   */
  public MasterClientRequestFilter(Logger logger) {
    this.loggingStream = null;
    this.logger = logger;
  }

  /**
   * Create a logging filter logging the request and response to print stream.
   *
   * @param loggingStream the print stream to log requests and responses.
   */
  public MasterClientRequestFilter(PrintStream loggingStream) {
    this.loggingStream = loggingStream;
    this.logger = null;
  }

  private void log(StringBuilder b) {
    if (logger != null) {
      logger.info(b.toString());
    }
    else {
      loggingStream.print(b);
    }
  }

  private StringBuilder prefixId(StringBuilder b, long id) {
    b.append(Long.toString(id)).append(" ");
    return b;
  }

  @Override
  public ClientResponse handle(ClientRequest request) throws ClientHandlerException {
    long id = ++this._id;

    logRequest(id, request);

    ClientResponse response = getNext().handle(request);

    logResponse(id, response);

    return response;
  }

  private void logRequest(long id, ClientRequest request) {
    StringBuilder b = new StringBuilder();

    printRequestLine(b, id, request);
    if (logger.isDebugEnabled()) {
      printRequestHeaders(b, id, request.getHeaders());
    }

    if (request.getEntity() != null) {
      request.setAdapter(new Adapter(request.getAdapter(), b));
    }
    else {
      log(b);
    }
  }

  private void printRequestLine(StringBuilder b, long id, ClientRequest request) {
    b.append("REQUEST ").append(id).append(": ").append(request.getMethod()).append(" ").
        append(request.getURI().toASCIIString());
  }

  private void printRequestHeaders(StringBuilder b, long id, MultivaluedMap<String, Object> headers) {
    for (Map.Entry<String, List<Object>> e : headers.entrySet()) {
      String header = e.getKey();
      for (Object value : e.getValue()) {
        prefixId(b, id).append(REQUEST_PREFIX).append(header).append(": ").
            append(ClientRequest.getHeaderValue(value)).append("\n");
      }
    }
//    prefixId(b, id).append(REQUEST_PREFIX).append("\n");
  }

  private void logResponse(long id, ClientResponse response) {
    StringBuilder b = new StringBuilder();

    printResponseLine(b, id, response);
    if (logger.isDebugEnabled()) {
      printResponseHeaders(b, id, response.getHeaders());
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    InputStream in = response.getEntityInputStream();
    try {
      ReaderWriter.writeTo(in, out);

      byte[] requestEntity = out.toByteArray();
      printEntity(b, requestEntity);
      response.setEntityInputStream(new ByteArrayInputStream(requestEntity));
    }
    catch (IOException ex) {
      throw new ClientHandlerException(ex);
    }
    log(b);
  }

  private void printResponseLine(StringBuilder b, long id, ClientResponse response) {
    b.append("RESPONSE ").append(id).append(": ").append(Integer.toString(response.getStatus()));
  }

  private void printResponseHeaders(StringBuilder b, long id, MultivaluedMap<String, String> headers) {
    b.append("\n");
    for (Map.Entry<String, List<String>> e : headers.entrySet()) {
      String header = e.getKey();
      for (String value : e.getValue()) {
        prefixId(b, id).append(RESPONSE_PREFIX).append(header).append(": ").
            append(value).append("\n");
      }
    }
//    prefixId(b, id).append(RESPONSE_PREFIX).append("\n");
  }

  private void printEntity(StringBuilder b, byte[] entity) throws IOException {
    if (entity.length == 0) {
      return;
    }

    b.append("\n ").append(new String(entity));
  }
}
