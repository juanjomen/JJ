package org.familysearch.standards.services;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.model.DbTruth;
import org.familysearch.standards.model.DbTruthSet;
import org.familysearch.standards.model.exception.PnR_SystemException;

/**
 * Created by juanjomen on 2/2/2016.
 */
public class WriteableMetricsService extends BaseMetricsService{
  public WriteableMetricsService(BasicDataSource dataSource) throws PnR_SystemException{
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
  // WRITE methods for Truth Set data
  // ====================================================================================
  //
  /**
   * Creates a new truth Set based on types, name and a version.  todo add more details here
   *
   * @param type the type of truth set
   * @param name the truth set name
   * @param version the truth set version
   * @return newly-created truthSet instance
   *
   * @throws PnR_SystemException
   */
  public DbTruthSet createTruthSet(String type, String name, String version) throws PnR_SystemException{
    //todo add validations here
    return getTruthSetDAO().create(type,name,version);
  }

  /**
   * Update Truth set based on its ID. todo add more details here
   *
   * @param id the truth set id
   * @param type the type of truth set
   * @param name the truth set name
   * @param version the truth set version
   * @return updated truthSet instance
   *
   * @throws PnR_SystemException
   */
  public DbTruthSet updateTruthSet (Long id, String type, String name, String version) throws PnR_SystemException{
    //todo add validations here
    return getTruthSetDAO().update(id,type,name,version);
  }

  /**
   * Deletes an existing truth set if it
   * has no associated truths.
   *
   * @param id truth set identifier
   *
   * @throws PnR_SystemException
   */
  public void deleteTruthSet(long id) throws PnR_SystemException{
    getTruthSetDAO().delete(id);
  }

  /**
   * Create a new Truth instance in a truth set.
   * todo add more details for documentation
   *
   * @param truthSetId truth set identifier, must reference an existing true set Id
   * @param value raw data for interpretation
   * @param contextValue additional data associated with the TRUTH
   * @return newly-created truth instance
   *
   * @throws PnR_SystemException
   */
  public DbTruth createTruth(
    long truthSetId, String value, String contextValue)
    throws PnR_SystemException{
    //todo validation
    return  getTruthDAO().create(truthSetId,value,contextValue);
  }

  /**
   * Update an existing Truth instance.
   * todo add more details
   * @param truthId attribute identifier, must reference an existing truth
   * @param truthSetId truth set identifier, must reference an existing true set Id
   * @param value raw data for interpretation
   * @param contextValue additional data associated with the TRUTH
   * @return updated truth instance
   *
   * @throws PnR_SystemException
   */
  public DbTruth updateTruth(
    long truthId, long truthSetId, String value, String contextValue)
    throws PnR_SystemException{
    //todo validate
    return getTruthDAO().update(truthId,truthSetId,value,contextValue);
  }

  /**
   * Delete an existing DbTruth.
   *  todo more deatils here for enunciate
   * @param id attribute identifier
   *
   * @throws PnR_SystemException
   */
  public void deleteTruth(long id) throws PnR_SystemException{
    // todo validate
    getTruthDAO().delete(id);
  }

}
