package api;

import api.model.DbTruthSet;
import api.model.exception.NameSystemException;

import java.util.List;

/**
 * Basic access methods for dealing with {@link DbTruthSet} instances.
 * 
 * @author wjohnson000
 *
 */
public interface TruthSetDAO {

    /**
     * Retrieve a {@link DbTruthSet} based on its primary key value.
     * 
     * @param id truth set identifier
     * @return associated truth set
     * 
     * @throws NameSystemException
     */
    DbTruthSet get(long id) throws NameSystemException;

  /**
   * Retrieve a {@link DbTruthSet} name based on the name text.
   *
   * @param name truth set name
   * @return associated truth set or null
   *
   * @throws NameSystemException
   */
  DbTruthSet get(String name) throws NameSystemException;

  /**
   * Create a new {@link DbTruthSet} instance.
   *
   * @param type the type of truth set
   * @param name the truth set name
   * @param version the truth set version
   * @return newly-created trueSet instance
   *
   * @throws NameSystemException
   */
  DbTruthSet create(String type, String name, String version) throws NameSystemException;

  /**
   * Update a {@link DbTruthSet} instance.
   *
   * @param id the truth set id
   * @param type the type of truth set
   * @param name the truth set name
   * @param version the truth set version
   * @return newly-created trueSet instance
   *
   * @throws NameSystemException
   */
  DbTruthSet update(Long id, String type, String name, String version) throws NameSystemException;

    /**
     * Delete an existing {@link DbTruthSet} instance.  This will work only if the set
     * has no associated truths.
     * 
     * @param id true set identifier
     * 
     * @throws NameSystemException
     */
    void delete(long id) throws NameSystemException;

    /**
     * Search for names based on attribute and other criteria.  The list will be returned in
     * alphabetical order of the Name's text.  A "null" value for any parameter will mean that
     * it won't be used when pulling the list of names.  If both "isMale" and "isFemale" are
     * true, then only names which can be either will be returned.  If both of them are "false"
     * or null, then nothing will be returned.
     *
     * @param type the type of truth set
     * @param name the truth set name
     * @param version the truth set version
     * 
     * @return list of truth sets
     * @throws NameSystemException
     */
    List<DbTruthSet> search(
      String type, String name, String version)
                    throws NameSystemException;
}
