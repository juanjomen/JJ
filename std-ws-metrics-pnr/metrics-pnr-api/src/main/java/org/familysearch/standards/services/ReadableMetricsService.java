package org.familysearch.standards.services;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.model.DbTruth;
import org.familysearch.standards.model.DbTruthSet;
import org.familysearch.standards.model.DbTruthValue;
import org.familysearch.standards.model.exception.PnR_SystemException;

import java.util.List;

/**
 * Created by juanjomen on 2/2/2016.
 */
public class ReadableMetricsService extends BaseMetricsService {


  public ReadableMetricsService(BasicDataSource dataSource)throws PnR_SystemException{
    super(dataSource);
  }
  /**
   * Perform a clean shutdown of any services
   */
  public void shutdown() {
    // For now, do nothing
  }

  //
  // ====================================================================================
  // READ methods for TruthSet data
  // ====================================================================================
  //

  public DbTruthSet getTruthSet(long truthId) throws PnR_SystemException {
    return getTruthSetDAO().get(truthId);
  }

  public DbTruthSet getTruthSet(String truthName) throws PnR_SystemException {
    return getTruthSetDAO().get(truthName);
  }

  public List<DbTruthSet> getTruthSets() throws PnR_SystemException {
    return getTruthSetDAO().getTruthSets();
  }

  //
  // ====================================================================================
  // READ methods for Truth data
  // ====================================================================================
  //
  public DbTruth getTruth(long truthId) throws PnR_SystemException {
    return getTruthDAO().get(truthId);
  }

  public List<DbTruth> getTruthsBySet(long setId) throws PnR_SystemException {
    return getTruthDAO().getBySetId(setId);
  }

  //
  // ====================================================================================
  // READ methods for TYPE data
  // ====================================================================================
  //

  public DbTruthValue getTruthValue(long truthValueId) throws PnR_SystemException {
    return getTruthValueDAO().get(truthValueId);
  }

  public List<DbTruthValue> getTruthValuesByTruth(Long truthId) throws PnR_SystemException {
    return getTruthValueDAO().getByTruthId(truthId);
  }
}
