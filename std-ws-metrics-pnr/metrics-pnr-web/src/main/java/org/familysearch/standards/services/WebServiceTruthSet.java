package org.familysearch.standards.services;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.model.*;
import org.familysearch.standards.model.exception.PnR_SystemException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("metrics-pnr")
public class WebServiceTruthSet {

  private BasicDataSource dataSource;
  ReadableMetricsService rs;
  final String dbUser = System.getenv("NAME_DB_USERNAME");
  final String dbPassW = System.getenv("NAME_DB_PASSWORD");
  final String dbAddress = System.getenv("NAME_DB_ADDRESS");
  final String dbName = System.getenv("PNR_DB_DATABASENAME");
  final String dbPort = System.getenv("NAME_DB_PORT");

  public void getDataSources() throws PnR_SystemException {

    dataSource = new BasicDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl("jdbc:postgresql://"+dbAddress+":"+dbPort+"/"+ dbName);
    dataSource.setUsername(dbUser);
    dataSource.setPassword(dbPassW);

    rs = new ReadableMetricsService(dataSource);

  }

  /**
   * Method handling HTTP GET requests. The returned object will be sent
   * to the client as "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */

  @Path("/truthset/" + WebConstants.PATH_ID)
  @GET
  @Produces({RootModel.APPLICATION_XML_NAMES, RootModel.APPLICATION_JSON_NAMES, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response getIt(@PathParam(WebConstants.PARAM_ID) Long truthSetId)throws PnR_SystemException {
    if(null==dataSource){
      getDataSources();
    }

    RootModel root = new RootModel();

    DbTruthSet dbTruthSet = rs.getTruthSet(truthSetId);

    TruthSetModel tsModel = new TruthSetModel();
    tsModel.setId(dbTruthSet.getId());
    tsModel.setType(dbTruthSet.getType());
    tsModel.setName(dbTruthSet.getName());
    tsModel.setVersion(dbTruthSet.getVersion());

    List<TruthModel> truths= new ArrayList<>();
    for(DbTruth dbTruth: rs.getTruthsBySet(truthSetId)){
      TruthModel truthModel =new TruthModel();
      truthModel.setId(dbTruth.getId());
      truthModel.setContextValue(dbTruth.getContextValue());
      truthModel.setValue(dbTruth.getValue());
      truths.add(truthModel);
    }

    tsModel.setTruths(truths);

    root.setTruthSet(tsModel);
    return Response.ok(root).build();
  }

}
