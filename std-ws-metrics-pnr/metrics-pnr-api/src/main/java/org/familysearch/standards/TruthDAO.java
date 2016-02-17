package org.familysearch.standards;

import org.familysearch.standards.model.DbTruth;
import org.familysearch.standards.model.DbTruthSet;
import org.familysearch.standards.model.exception.PnR_SystemException;

import java.util.List;

/**
 * Created by juanjomen on 2/4/2016.
 */
public interface TruthDAO {

  /**
   * Retrieve a {@link DbTruth} based on its primary key value.
   *
   * @param id attribute identifier
   * @return associated truth, or null
   *
   * @throws PnR_SystemException
   */
  DbTruth get(long id) throws PnR_SystemException;

  /**
   * Retrieve a list of {@link DbTruth} instances associated with a specific truth set
   *
   * @param setId truth set identifier
   * @return list of attribute instances
   *
   * @throws PnR_SystemException
   */
  List<DbTruth> getBySetId(long setId) throws PnR_SystemException;

  /**
   * Create a new {@link DbTruth} instance.
   *
   * @param truthSetId truth set identifier, must reference an existing true set Id
   * @param value raw data for interpretation
   * @param contextValue additional data associated with the TRUTH
   * @return newly-created truth instance
   *
   * @throws PnR_SystemException
   */
  DbTruth create(
    long truthSetId, String value, String contextValue)
    throws PnR_SystemException;

  /**
   * Update an existing {@link DbTruth} instance.
   *
   * @param truthId attribute identifier, must reference an existing truth
   * @param truthSetId truth set identifier, must reference an existing true set Id
   * @param value raw data for interpretation
   * @param contextValue additional data associated with the TRUTH
   * @return updated truth instance
   *
   * @throws PnR_SystemException
   */
  DbTruth update(
    long truthId, long truthSetId, String value, String contextValue)
    throws PnR_SystemException;

  /**
   * Delete an existing {@link DbTruthSet} DbTruth.
   *
   * @param id attribute identifier
   *
   * @throws PnR_SystemException
   */
  void delete(long id) throws PnR_SystemException;
}
