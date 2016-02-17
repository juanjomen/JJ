package web.services;

import api.model.DbTruthSet;
import api.model.exception.NameSystemException;
import web.model.RootModel;
import web.model.TruthSetModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;


@Path(WebServiceBase.URL_ROOT )
public class WebServiceTruthSet extends WebServiceBase {


  /**
   * Retrieve details about a {@link TruthSetModel} instance
   *
   * @param uriInfo aqui esta
   * @param name the name being searched for
   *
   * @return Returns a root model with a name model of the requested name if successful. Otherwise, returns an exception message with corresponding status code.
   */
  @Path("/" + WebConstants.PATH_NAME)
  @GET
  @Produces({MediaType.APPLICATION_XML})
  public Response getNameByName(
      @Context UriInfo uriInfo,
      @Context HttpHeaders headers,
      @PathParam(WebConstants.PARAM_TRUTH_SET) String name) throws NameSystemException {

    long       startTime = System.currentTimeMillis();

    RootModel  root = new RootModel();

    DbTruthSet dbTruthSet = getReadableService().getTruthSet(name);
    if (dbTruthSet == null) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.NOT_FOUND.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, WebConstants.MSG_NOT_FOUND);
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.OK.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime));
    return Response.ok(root).expires(getExpireDate()).tag(getEntityTag(dbTruthSet.getId(), 0)).build();
  }

}