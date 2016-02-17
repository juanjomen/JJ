import Db.DbTruthSet;
import exception.NameSystemException;
import models.RootModel;
import models.TruthSetModel;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;


@Path(WebServiceBase.URL_ROOT + "/" + "truthSets")
public class WebServiceTruthSet extends WebServiceBase {

  /**
   * Get a list of endpoints available
   *
   * @param uriInfo the request URI
   *
   * @return Returns a root model with available end point links
   * @throws NameSystemException
   */

  @GET
  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

  public Response getTrutSets(
      @Context UriInfo uriInfo)
      throws NameSystemException {
    RootModel               root = new RootModel();

    return Response.ok(root).expires(getExpireDate()).build();
  }

  /**
   * Create a new NAME entry
   *
   * @param uriInfo the request URI
   * @param headers the HTTP headers
   * @param request the request text
   * @param inRoot RootModel containing the new name
   * @param userId user performing the action
   *
   * @return Returns a root model with a name model of the newly-created name if successful. Otherwise, returns an exception message with corresponding status code.
   * @throws NameSystemException
   */
  @POST
  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Consumes({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES})
  public Response createTruthSet(
      @Context UriInfo uriInfo,
      @Context HttpHeaders headers,
      @Context HttpServletRequest request,
      RootModel inRoot,
      @DefaultValue(WebConstants.MSG_SYSTEM) @HeaderParam(WebConstants.HEADER_USER_ID) String userId)
      throws NameSystemException {

    RootModel  outRoot = new RootModel();
    long       startTime = System.currentTimeMillis();

    TruthSetModel truthSetModel = inRoot.getTruthSet();
    if (truthSetModel == null) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No 'truthModel' found in request body.", WebConstants.MSG_USER_ID, userId);
      return Response.status(Response.Status.BAD_REQUEST).entity("No 'TruthSetModel' found in request body.").build();
    }
    if (truthSetModel.getType() == null || truthSetModel.getType().equals("")) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No type text found in request body.", WebConstants.MSG_USER_ID, userId);
      return Response.status(Response.Status.BAD_REQUEST).entity("No Truth set type  found in request body.").build();
    }
    if (truthSetModel.getName() == null || truthSetModel.getName().equals("")) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No name text found in request body.", WebConstants.MSG_USER_ID, userId);
      return Response.status(Response.Status.BAD_REQUEST).entity("No Truth set name  found in request body.").build();
    }
    if (truthSetModel.getVersion() == null || truthSetModel.getVersion().equals("")) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No version text found in request body.", WebConstants.MSG_USER_ID, userId);
      return Response.status(Response.Status.BAD_REQUEST).entity("No Truth set version  found in request body.").build();
    }

    DbTruthSet dbTruthSet = getWritableService().createTruthSet(truthSetModel.getType(), truthSetModel.getName(), truthSetModel.getVersion());
    TruthSetModel newTruthSet = getTruthSetMapper().mapFrom(dbTruthSet, getPath(uriInfo));
    outRoot.setTruthSet(newTruthSet);

    log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.CREATED.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime), WebConstants.MSG_USER_ID, userId);

    return Response
        .created(URI.create(getPath(uriInfo) + "/" + newTruthSet.getId()))
        .entity(outRoot)
        .tag(getEntityTag(newTruthSet.getId(), 0))
        .build();
  }

  /**
   * Update a NAME entry
   *
   * @param uriInfo
   * @param inRoot RootModel containing the new name
   * @param userId user performing the action
   *
   * @return Returns a root model with a name model of the updated name ID if successful. Otherwise, returns an exception message with corresponding status code.
   * @throws NameSystemException
   */
  @Path("/" + WebConstants.PATH_ID)
  @PUT
  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Consumes({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES})
  public Response updateTruthSet(
      @Context UriInfo uriInfo,
      @Context HttpHeaders headers,
      @Context HttpServletRequest request,
      RootModel inRoot,
      @DefaultValue(WebConstants.MSG_SYSTEM) @HeaderParam(WebConstants.HEADER_USER_ID) String userId,
      @PathParam(WebConstants.PARAM_ID) Long TruthSetId)
      throws NameSystemException {

    RootModel  outRoot = new RootModel();
    long       startTime = System.currentTimeMillis();

    TruthSetModel truthSetModel = inRoot.getTruthSet();
    if (truthSetModel == null) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No 'truthModel' found in request body.", WebConstants.MSG_USER_ID, userId);
      return Response.status(Response.Status.BAD_REQUEST).entity("No 'TruthSetModel' found in request body.").build();
    }
    if (truthSetModel.getType() == null || truthSetModel.getType().equals("")) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No type text found in request body.", WebConstants.MSG_USER_ID, userId);
      return Response.status(Response.Status.BAD_REQUEST).entity("No Truth set type  found in request body.").build();
    }
    if (truthSetModel.getName() == null || truthSetModel.getName().equals("")) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No name text found in request body.", WebConstants.MSG_USER_ID, userId);
      return Response.status(Response.Status.BAD_REQUEST).entity("No Truth set name  found in request body.").build();
    }
    if (truthSetModel.getVersion() == null || truthSetModel.getVersion().equals("")) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No version text found in request body.", WebConstants.MSG_USER_ID, userId);
      return Response.status(Response.Status.BAD_REQUEST).entity("No Truth set version  found in request body.").build();
    }

    DbTruthSet dbTruthSet = getWritableService().createTruthSet(truthSetModel.getType(), truthSetModel.getName(), truthSetModel.getVersion());
    TruthSetModel newTruthSet = getTruthSetMapper().mapFrom(dbTruthSet, getPath(uriInfo));
    outRoot.setTruthSet(newTruthSet);

    log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.CREATED.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime), WebConstants.MSG_USER_ID, userId);

    return Response
        .ok(URI.create(getPath(uriInfo) + "/" + newTruthSet.getId()))
        .entity(outRoot)
        .tag(getEntityTag(newTruthSet.getId(), 0))
        .build();
  }

  /**
   * Retrieve details about a {@link TruthSetModel} instance
   *
   * @param uriInfo
   * @param truthId the name identifier
   *
   * @return Returns a root model with a name model of the requested name ID if successful. Otherwise, returns an exception message with corresponding status code.
   * @throws NameSystemException
   */
  @Path("/" + WebConstants.PATH_ID)
  @GET
  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response getNameById(
      @Context UriInfo uriInfo,
      @Context HttpHeaders headers,
      @PathParam(WebConstants.PARAM_ID) Long truthId)
      throws NameSystemException {

    long       startTime = System.currentTimeMillis();
    RootModel  root = new RootModel();

    DbTruthSet dbTruthSet = this.getReadableService().getTruthSet(truthId);
    if (dbTruthSet == null) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.NOT_FOUND.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, WebConstants.MSG_NOT_FOUND);
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.OK.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime));
    return Response.ok(root).expires(getExpireDate()).tag(getEntityTag(dbTruthSet.getId(), 0)).build();
  }

  /**
   * Retrieve details about a {@link TruthSetModel} instance
   *
   * @param uriInfo
   * @param nameText the name being searched for
   *
   * @return Returns a root model with a name model of the requested name if successful. Otherwise, returns an exception message with corresponding status code.
   * @throws NameSystemException
   */
  @Path("/" + WebConstants.PATH_NAME)
  @GET
  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response getNameByName(
      @Context UriInfo uriInfo,
      @Context HttpHeaders headers,
      @PathParam(WebConstants.PARAM_TRUTH_SET) String nameText)
      throws NameSystemException {

    long       startTime = System.currentTimeMillis();

    RootModel  root = new RootModel();

    DbTruthSet dbTruthSet = getReadableService().getTruthSet(nameText);
    if (dbTruthSet == null) {
      log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.NOT_FOUND.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, WebConstants.MSG_NOT_FOUND);
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.OK.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime));
    return Response.ok(root).expires(getExpireDate()).tag(getEntityTag(dbTruthSet.getId(), 0)).build();
  }


  /**
   * Delete a {@link TruthSetModel} instance, which will fail unless the name has no
   * associated attributes.
   *
   * @param uriInfo
   * @param nameId the name identifier
   * @param userId the user who is performing this operation
   *
   * @return Returns a status code of 204 (NO_DATA) if successful. Otherwise returns an exception message with a corresponding status code.
   * @throws NameSystemException
   */
//  @Path("/" + WebConstants.PATH_ID)
//  @DELETE
//  public Response deleteName(
//      @Context UriInfo uriInfo,
//      @Context HttpHeaders headers,
//      @Context HttpServletRequest request,
//      @PathParam(WebConstants.PARAM_ID) Long nameId,
//      @DefaultValue(WebConstants.MSG_SYSTEM) @HeaderParam(WebConstants.HEADER_USER_ID) String userId)
//      throws NameSystemException {
//
//    long startTime = System.currentTimeMillis();
//
//    // Ensure that the name exists
//    DbName dbName = getReadableService().getName(nameId);
//    if (dbName == null) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No 'Name' found for nameId=" + nameId, WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("No 'Name' found for nameId=" + nameId).build();
//    }
//
//    getWritableService().deleteName(nameId);
//    log(null, uriInfo.getPath(), WebConstants.METHOD_DELETE, Response.Status.NO_CONTENT.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime), WebConstants.MSG_USER_ID, userId);
//
//    return Response.noContent().build();
//  }
//
//  /**
//   * Retrieve details about a {@link NameModel} instance
//   *
//   * @param uriInfo
//   * @param nameId the name identifier
//   *
//   * @return Returns a root model with a name and attribue model for the requested name ID and its attributes if successful. Otherwise returns and exception message with a coresponding status code.
//   * @throws NameSystemException
//   */
//  @Path("/" + WebConstants.PATH_ID + "/" + "attributes")
//  @GET
//  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//  public Response getNameAttributes(
//      @Context UriInfo uriInfo,
//      @Context HttpHeaders headers,
//      @PathParam(WebConstants.PARAM_ID) Long nameId)
//      throws NameSystemException {
//
//    long       startTime = System.currentTimeMillis();
//    RootModel  root = new RootModel();
//
//    List<AttributeModel> allAttrs = new ArrayList<>();
//    List<DbAttribute> dbAttrs = getReadableService().getAttributesByName(nameId);
//    for (DbAttribute dbAttr : dbAttrs) {
//      allAttrs.add(getAttributeMapper().mapFrom(dbAttr, getPath(uriInfo)));
//    }
//    root.setAttributes(allAttrs);
//
//    log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.OK.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime));
//    return Response.ok(root).expires(getExpireDate()).build();
//  }
//
//  /**
//   * Create a new {@link AttributeModel} instance
//   *
//   * @param uriInfo
//   * @param inRoot RootModel containing the new name and it atribute set(s).
//   * @param userId user performing the action
//   *
//   * @return Returns a root model with a name and attribute model for the newly-created name with attributes if successful. Otherwise returns an exception message with a corresponding status code.
//   * @throws NameSystemException
//   */
//  @Path("/" + WebConstants.PATH_ID + "/" + "attributes")
//  @POST
//  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//  @Consumes({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES})
//  public Response createAttribute(
//      @Context UriInfo uriInfo,
//      @Context HttpHeaders headers,
//      @Context HttpServletRequest request,
//      RootModel inRoot,
//      @PathParam(WebConstants.PARAM_ID) Long nameId,
//      @DefaultValue(WebConstants.MSG_SYSTEM) @HeaderParam(WebConstants.HEADER_USER_ID) String userId)
//      throws NameSystemException {
//
//    long       startTime = System.currentTimeMillis();
//    RootModel  outRoot = new RootModel();
//    int thisYear = Calendar.getInstance().get(Calendar.YEAR);
//
//
//    // Validate some of the input data
//    AttributeModel attrModel = inRoot.getAttribute();
//    if (attrModel == null) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No 'AttributeModel' found in request body.", WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("No 'AttributeModel' found in request body.").build();
//    } else if (attrModel.getNameId() == null  ||  attrModel.getNameId() == 0L) {
//      attrModel.setNameId(nameId);
//    } else if (!(attrModel.getNameId().equals(nameId))) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "NameId Mismatch: QueryParam=" + nameId + " but ModelId=" + attrModel.getNameId(), WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("Name identifier in URL query parameter doesn't match identifier in request body").build();
//    }
//    if (attrModel.getFromYear() != null && attrModel.getFromYear() > thisYear) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "From Year is in the future. From Year = " + attrModel.getFromYear() + " but ModelId=" + attrModel.getNameId(), WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("From Year is in the future. From Year = " + attrModel.getFromYear()).build();
//    }
//    if (attrModel.getFromYear() != null && attrModel.getToYear() > thisYear) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "To Year is in the future. To Year = " + attrModel.getToYear() + " but ModelId=" + attrModel.getNameId(), WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("To Year is in the future. To Year = " + attrModel.getToYear()).build();
//    }
//
//    DbAttribute dbAttr = getWritableService().createAttribute(
//        attrModel.getNameId(),
//        attrModel.getTypeId(),
//        attrModel.getRepGroupId(),
//        attrModel.getIsMale(),
//        attrModel.getIsFemale(),
//        attrModel.getFromYear(),
//        attrModel.getToYear(),
//        attrModel.getFrequency(),
//        attrModel.getWeight());
//    AttributeModel newAttr = getAttributeMapper().mapFrom(dbAttr, getPath(uriInfo));
//    outRoot.setAttribute(newAttr);
//
//    log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.CREATED.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime), WebConstants.MSG_USER_ID, userId);
//
//    return Response
//        .created(URI.create(getPath(uriInfo) + "/" + newAttr.getId()))
//        .entity(outRoot)
//        .tag(getEntityTag(newAttr.getId(), 0))
//        .build();
//  }
//
//  /**
//   * Retrieve details about a {@link AttributeModel} instance
//   *
//   * @param uriInfo
//   * @param nameId the name identifier
//   *
//   * @return Returns a root model with an attribute model for the requested attribute ID if successful. Otherwise returns an exception message with a corresponding status code.
//   * @throws NameSystemException
//   */
//  @Path("/" + WebConstants.PATH_ID + "/" + "attributes" + "/" + WebConstants.PATH_ATTR_ID)
//  @GET
//  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//  public Response getNameAttribute(
//      @Context UriInfo uriInfo,
//      @Context HttpHeaders headers,
//      @PathParam(WebConstants.PARAM_ID) Long nameId,
//      @PathParam(WebConstants.PARAM_ATTR_ID) Long attrId)
//      throws NameSystemException {
//
//    long       startTime = System.currentTimeMillis();
//    RootModel  root = new RootModel();
//
//    DbAttribute dbAttr = getReadableService().getAttribute(attrId);
//    if (dbAttr == null  ||  dbAttr.getNameId() == null  ||  !(dbAttr.getNameId().equals(nameId))) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.NOT_FOUND.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, WebConstants.MSG_NOT_FOUND);
//      return Response.status(Response.Status.NOT_FOUND).build();
//    }
//    root.setAttribute(getAttributeMapper().mapFrom(dbAttr, getPath(uriInfo)));
//
//    log(null, uriInfo.getPath(), WebConstants.METHOD_GET, Response.Status.OK.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime));
//    return Response.ok(root).expires(getExpireDate()).build();
//  }
//  /**
//   * Update an existing {@link AttributeModel} instance
//   *
//   * @param uriInfo
//   * @param inRoot RootModel containing the new name
//   * @param userId user performing the action
//   *
//   * @return Returns a root model with an attribute model for the updated attribute ID if successful. Otherwise returns an exception message with a corresponding status code.
//   * @throws NameSystemException
//   */
//  @Path("/" + WebConstants.PATH_ID + "/" + "attributes" + "/" + WebConstants.PATH_ATTR_ID)
//  @PUT
//  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//  @Consumes({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES})
//  public Response updateAttribute(
//      @Context UriInfo uriInfo,
//      @Context HttpHeaders headers,
//      @Context HttpServletRequest request,
//      RootModel inRoot,
//      @PathParam(WebConstants.PARAM_ID) Long nameId,
//      @PathParam(WebConstants.PARAM_ATTR_ID) Long attrId,
//      @DefaultValue(WebConstants.MSG_SYSTEM) @HeaderParam(WebConstants.HEADER_USER_ID) String userId)
//      throws NameSystemException {
//
//    long       startTime = System.currentTimeMillis();
//    RootModel  outRoot = new RootModel();
//    int thisYear = Calendar.getInstance().get(Calendar.YEAR);
//
//    // Validate some of the input data
//    AttributeModel attrModel = inRoot.getAttribute();
//    if (attrModel == null) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_PUT, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No data found in request body.", WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("No data found in request body.").build();
//    } else if (attrModel.getNameId() == null  ||  attrModel.getNameId() == 0L) {
//      attrModel.setNameId(nameId);
//    } else if (!(attrModel.getNameId().equals(nameId))) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_PUT, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "AttrId Mismatch: QueryParam=" + nameId + " but ModelId=" + attrModel.getNameId(), WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("Attr identifier in URL query parameter doesn't match identifier in request body").build();
//    } else if (attrModel.getId() == null  ||  attrModel.getId() == 0L) {
//      attrModel.setId(attrId);
//    } else if (!(attrModel.getId().equals(attrId))) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_PUT, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "AttributeId Mismatch: PathParam=" + attrId + " but ModelId=" + attrModel.getId(), WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("Attribute identifier in URL query parameter doesn't match identifier in request body").build();
//    }
//    if (attrModel.getFromYear() != null && attrModel.getFromYear() > thisYear) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_PUT, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "From Year is in the future. From Year = " + attrModel.getFromYear() + " but ModelId=" + attrModel.getNameId(), WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("From Year is in the future. From Year = " + attrModel.getFromYear()).build();
//    }
//    if (attrModel.getToYear() != null && attrModel.getToYear() > thisYear) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_PUT, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "To Year is in the future. To Year = " + attrModel.getToYear() + " but ModelId=" + attrModel.getNameId(), WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("To Year is in the future. To Year = " + attrModel.getToYear()).build();
//    }
//
//    // Ensure that the attribute exists and is tied to the given name
//    DbAttribute dbAttr = getReadableService().getAttribute(attrId);
//    if (dbAttr == null) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No 'Attribute' found for attrId=" + attrId, WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("No 'Attribute' found for attrId=" + attrId).build();
//    } else if (dbAttr.getNameId() == null  ||  !(dbAttr.getNameId().equals(nameId))) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "Attribute [attrId=" + attrId + "] not tied to Name [nameId=" + nameId + "].", WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("Attribute [attrId=" + attrId + "] not tied to Name [nameId=" + nameId + "].").build();
//    }
//
//    dbAttr = getWritableService().updateAttribute(
//        attrModel.getId(),
//        attrModel.getNameId(),
//        attrModel.getTypeId(),
//        attrModel.getRepGroupId(),
//        attrModel.getIsMale(),
//        attrModel.getIsFemale(),
//        attrModel.getFromYear(),
//        attrModel.getToYear(),
//        attrModel.getFrequency(),
//        attrModel.getWeight());
//    AttributeModel newAttr = getAttributeMapper().mapFrom(dbAttr, getPath(uriInfo));
//    outRoot.setAttribute(newAttr);
//
//    log(null, uriInfo.getPath(), WebConstants.METHOD_PUT, Response.Status.OK.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime), WebConstants.MSG_USER_ID, userId);
//
//    return Response
//        .ok(outRoot)
//        .tag(getEntityTag(newAttr.getId(), 0))
//        .build();
//  }
//
//
//  /**
//   * Delete an {@link AttributeModel} instance
//   *
//   * @param uriInfo
//   * @param attrId the attribute identifier
//   * @param userId the user who is performing this action
//   *
//   * @return Returns a status code 204 (NO_DATA) for the deleted attribute ID if successful. Otherwise returns an exception message with a corresponding status code.
//   * @throws NameSystemException
//   */
//  @Path("/" + WebConstants.PATH_ID + "/" + "attributes" + "/" + WebConstants.PATH_ATTR_ID)
//  @DELETE
//  public Response deleteAttribute(
//      @Context UriInfo uriInfo,
//      @Context HttpHeaders headers,
//      @Context HttpServletRequest request,
//      @PathParam(WebConstants.PARAM_ID) Long nameId,
//      @PathParam(WebConstants.PARAM_ATTR_ID) Long attrId,
//      @DefaultValue(WebConstants.MSG_SYSTEM) @HeaderParam(WebConstants.HEADER_USER_ID) String userId)
//      throws NameSystemException {
//
//    long startTime = System.currentTimeMillis();
//
//    // Ensure that the attribute exists and is tied to the given name
//    DbAttribute dbAttr = getReadableService().getAttribute(attrId);
//    if (dbAttr == null) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "No 'Attribute' found for attrId=" + attrId, WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("No 'Attribute' found for attrId=" + attrId).build();
//    } else if (dbAttr.getNameId() == null  ||  !(dbAttr.getNameId().equals(nameId))) {
//      log(null, uriInfo.getPath(), WebConstants.METHOD_POST, Response.Status.BAD_REQUEST.getStatusCode(), headers, WebConstants.MSG_USER_ERROR, "Attribute [attrId=" + attrId + "] not tied to Name [nameId=" + nameId + "].", WebConstants.MSG_USER_ID, userId);
//      return Response.status(Response.Status.BAD_REQUEST).entity("Attribute [attrId=" + attrId + "] not tied to Name [nameId=" + nameId + "].").build();
//    }
//
//    // Delete the attribute
//    getWritableService().deleteAttribute(attrId);
//    log(null, uriInfo.getPath(), WebConstants.METHOD_DELETE, Response.Status.NO_CONTENT.getStatusCode(), headers, WebConstants.MSG_TIME, "" + (System.currentTimeMillis() - startTime), WebConstants.MSG_USER_ID, userId);
//
//    return Response.noContent().build();
//  }
}