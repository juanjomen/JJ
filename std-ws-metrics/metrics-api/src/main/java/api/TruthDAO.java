package api;

import api.model.DbTruth;
import api.model.exception.NameSystemException;

import java.util.List;

public interface TruthDAO {

    /**
     * Retrieve a {@link DbTruth} based on its primary key value.
     * 
     * @param id attribute identifier
     * @return associated attribute, or null
     * 
     * @throws api.model.exception.NameSystemException
     */
    DbTruth get(long id) throws NameSystemException;

    /**
     * Retrieve a list of {@link DbTruth} instances associated with a specific name
     * 
     * @param truthId name identifier
     * @return list of truth instances
     * 
     * @throws NameSystemException
     */
    List<DbTruth> getByNameId(long truthId) throws api.model.exception.NameSystemException;

    /**
     * Create a new {@link DbTruth} instance.
     *
     * @param truthSetId attribute identifier, must reference an existing true set Id
     * @param value identifier, or null
     * @param contextValue place-rep-group identifier, or null
     * @return newly-created attribute instance
     * 
     * @throws NameSystemException
     */
    DbTruth create(
      long truthSetId, String value, String contextValue)
      throws NameSystemException;

    /**
     * Update an existing {@link DbTruth} instance.
     * 
     * @param truthId attribute identifier, must reference an existing attribute
     * @param setId name identifier
     * @param value identifier, or null
     * @param contextValue place-rep-group identifier, or null
     * @return newly-updated truth instance
     * 
     * @throws NameSystemException
     */
    DbTruth update(
      long truthId, long setId, String value, String contextValue)
                    throws NameSystemException;

    /**
     * Delete an existing {@link DbTruth} DbTruth.
     * 
     * @param id truth identifier
     * 
     * @throws NameSystemException
     */
    void delete(long id) throws NameSystemException;
}
