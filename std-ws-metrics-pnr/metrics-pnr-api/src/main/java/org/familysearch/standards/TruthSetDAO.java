package org.familysearch.standards;

import org.familysearch.standards.model.DbTruthSet;
import org.familysearch.standards.model.exception.PnR_SystemException;

import java.util.List;

/**
 * Created by juanjomen on 2/4/2016.
 */
public interface TruthSetDAO {
  /**
   * Retrieve a {@link DbTruthSet} based on its primary key value.
   *
   * @param id truth set identifier
   * @return associated truth set, or null
   *
   * @throws PnR_SystemException
   */
  DbTruthSet get(long id) throws PnR_SystemException;


  /**
   * Retrieve a {@link DbTruthSet} name based on the name text.
   *
   * @param name name text
   * @return associated truth set, or null
   *
   * @throws PnR_SystemException
   */
  DbTruthSet get(String name) throws PnR_SystemException;

  /**
   * Retrieve a {@link DbTruthSet} list of all truth sets.
   *
   * @return all truth sets, or null
   *
   * @throws PnR_SystemException
   */
  List<DbTruthSet> getTruthSets() throws PnR_SystemException;

  /**
   * Create a new {@link DbTruthSet} instance.
   *
   * @param type the type of truth set
   * @param name the truth set name
   * @param version the truth set version
   * @return newly-created truthSet instance
   *
   * @throws PnR_SystemException
   */
  DbTruthSet create(String type, String name, String version) throws PnR_SystemException;

  /**
   * Update a {@link DbTruthSet} instance.
   *
   * @param id the truth set id
   * @param type the type of truth set
   * @param name the truth set name
   * @param version the truth set version
   * @return updated truthSet instance
   *
   * @throws PnR_SystemException
   */
  DbTruthSet update(Long id, String type, String name, String version) throws PnR_SystemException;

  /**
   * Delete an existing {@link  DbTruthSet} instance.  This will work only if the truth set
   * has no associated truths.
   *
   * @param id truth set identifier
   *
   * @throws PnR_SystemException
   */
  void delete(long id) throws PnR_SystemException;

}
