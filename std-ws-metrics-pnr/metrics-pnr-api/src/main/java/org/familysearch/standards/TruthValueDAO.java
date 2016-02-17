package org.familysearch.standards;

import org.familysearch.standards.model.DbTruthValue;
import org.familysearch.standards.model.exception.PnR_SystemException;

import java.util.List;

/**
 * Created by juanjomen on 2/4/2016.
 */
public interface TruthValueDAO {
  /**
   * Retrieve a {@link DbTruthValue} based on its primary key value.
   *
   * @param id truth value identifier
   * @return associated truth value, or null
   *
   * @throws PnR_SystemException
   */
  DbTruthValue get(long id) throws PnR_SystemException;


  /**
   * Retrieve a List of all {@link DbTruthValue} instances
   * of a truth
   *
   * @return list Truth Values
   *
   * @throws PnR_SystemException
   */
  List<DbTruthValue> getByTruthId(Long truthId) throws PnR_SystemException;

  /**
   * Create a new {@link DbTruthValue} instance.
   *
   * @param truthId the truth id associate with the truth value
   * @param val value expected from the results
   * @param contextVal additional data associated with the TRUTH
   * @param score a value between 0 and 1
   * @param isPositive boolean to indicate if this value SHOULD (true) or SHOULD NOT (false) be matched
   * @return newly-created DbTruthValue instance
   *
   * @throws PnR_SystemException
   */
  DbTruthValue create(Long truthId, String val, String contextVal, Long score, boolean isPositive) throws PnR_SystemException;

  /**
   * Update a {@link DbTruthValue} instance.
   *
   * @param id the truth value Id
   * @param truthId the truth id associate with the truth value
   * @param val value expected from the results
   * @param contextVal additional data associated with the TRUTH
   * @param score a value between 0 and 1
   * @param isPositive boolean to indicate if this value SHOULD (true) or SHOULD NOT (false) be matched
   * @return updated DbTruthValue instance
   *
   * @throws PnR_SystemException
   */
  DbTruthValue update(Long id, Long truthId, String val, String contextVal, Long score, boolean isPositive) throws PnR_SystemException;

  /**
   * Delete a {@link DbTruthValue} instance.
   *
   * @param truthValueId truth value ID
   *
   * @throws PnR_SystemException
   */
  void delete(Long truthValueId) throws PnR_SystemException;
}
