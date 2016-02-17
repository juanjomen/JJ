package services;

import Db.DbTruth;
import Db.DbTruthSet;
import exception.NameDataException;
import exception.NameSystemException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.core.logging.Logger;

import java.text.MessageFormat;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Service class with methods for creating, updating or deleting a persisted entity
 * (type, name or attribute).  Exhaustive validation is done to ensure that the
 * data is fully provided before the operation continues.  Any validation error will
 * result in a {@link NameDataException} being thrown.
 *
 * @author wjohnson000
 */
public class WriteableMetricsService extends BaseMetricsService {

  /**
   * Shared logger and module name used for logging purposes
   */
  private static final Logger LOGGER = new Logger(WriteableMetricsService.class);

  /**
   * Message bundle, default and override
   */
  private ResourceBundle systemMessages;

  /**
   * Constructor requires a non-null data-source
   *
   * @param dataSource data-source
   */
  public WriteableMetricsService(BasicDataSource dataSource) throws NameSystemException {
    super(dataSource);
    loadSystemMessages();
  }

  /**
   * Perform a clean shutdown of any services
   */
  public void shutdown() {
    // For now, do nothing
  }

  //
  // ====================================================================================
  // WRITE methods for Truth set data
  // ====================================================================================
  //

  /**
   * Create a new name based on some text and a valid StdLocale.  The text
   * can't be null or blank, it can't be more than 64 characters in length, and it must
   * be unique when compared against existing name instances.  If not supplied, the
   * locale will be set to "EN" (English, Latin script).
   *
   * @param type truth set's type
   * @param name truth set's name
   * @param version truths set's version
   * @return new name with a unique identifier assigned
   * @throws NameSystemException for data validation error, system error
   */
  public DbTruthSet createTruthSet(String type, String name, String version) throws NameSystemException {
    validateTruthSetData(type, name, version, "CREATE");

    return getTruthSetDAO().create(type, name, version);
  }

  /**
   * Update a name based on its ID.  The text
   * can't be null or blank, it can't be more than 64 characters in length, and it must
   * be unique when compared against existing name instances.  If not supplied, the
   * locale will be set to "EN" (English, Latin script).
   *
   * @param id             name's id
   **@param type truth set's type
   * @param name truth set's name
   * @param version truths set's version
   * @return new name with a unique identifier assigned
   * @throws NameSystemException for data validation error, system error
   */
  public DbTruthSet updateTruthSet(Long id, String type, String name, String version) throws NameSystemException {
    validateTruthSetData(type, name,version, "UPDATE");

    return getTruthSetDAO().update(id, type, name,version);
  }

  /**
   * Delete an existing truth set.  The operation will succeed if the truth set exists but
   * doesn't have any associated truths.
   *
   * @param truthSetId truth set identifier
   * @throws NameSystemException for data validation error, system error
   */
  public void deleteTruthSet(long truthSetId) throws NameSystemException {
    validateCanDeleteName(truthSetId);

    getTruthSetDAO().delete(truthSetId);
  }

  //
  // ====================================================================================
  // WRITE methods for TRUTHS data
  // ====================================================================================
  //

  /**
   * Create a new Truth for an existing truth set.  Here be the rules:
   * <ul>
   * <li>The truth set identifier must reference an existing truth set.</li>
   * <li>The type identifier, if given, must reference an existing type.</li>
   * </ul>
   *
   * @param truthSetId      true set identifier
   * @param type
   * @param value
   * @param contextValue

   * @return newly created attribute instance
   * @throws NameSystemException
   */
  public DbTruth createTruth(
      long truthSetId, String type, String value, String contextValue)
      throws NameSystemException {

    validateTruthData(truthSetId,value,contextValue);

    return getTruthDAO().create(truthSetId,value,contextValue);
  }

  /**
   * Update an existing truth.  Here be the rules:
   * <ul>
   * <li>The truth identifier must reference an existing truth.</li>
   * <li>The truth set identifier must reference an existing truth set.</li>
   * </ul>
   *
   * @param truthId
   * @param truthSetId
   * @param type
   * @param value
   * @param contextValue
   * @return newly created attribute instance
   * @throws NameSystemException
   */
  public DbTruth updateTruth( long truthId,
    long truthSetId, String type, String value, String contextValue)
    throws NameSystemException{

    validateTruthExists(truthId);
    validateTruthData(truthId, type, value);

    return getTruthDAO().update(truthId,truthSetId,value,contextValue);
  }

  /**
   * Delete an existing attribute.  The operation will succeed if the attribute exists.
   *
   * @param attrId attribute identifier
   * @throws NameSystemException for data validation error, system error
   */
  public void deleteTruth(long attrId) throws NameSystemException {
    validateTruthExists(attrId);

    getTruthDAO().delete(attrId);
  }


  /**
   * Validate the name and locale parameters for a {@link DbTruthSet} instance.
   *
   * @param type           truth set's type
   * @param name           truth set's name
   * @param version        truth set's version
   * @throws NameSystemException
   */
  private void validateTruthSetData(String type, String name, String version, String method) throws NameSystemException {
    if (type == null || name.trim().length() == 0) {
      //TODO: Need log entry to bring up to current logging standard
      throw new NameDataException(getMessage("name.01.text_is_null"));
    }

    if (name.trim().length() > 255) {
      //TODO: Need log entry to bring up to current logging standard
      throw new NameDataException(getMessage("name.02.text_too_long", name));
    }

    DbTruthSet dbTruthSet = getTruthSetDAO().get(name);
    if (method.equals("CREATE") && dbTruthSet != null) {
      //TODO: Need log entry to bring up to current logging standard
      throw new NameDataException(getMessage("name.03.text_not_unique", name));
    }

    if (method.equals("UPDATE") && dbTruthSet == null) {
      //TODO: Need log entry to bring up to current logging standard
      throw new NameDataException(getMessage("name.05.not_found", name));
    }
  }

  /**
   * Validate a name's candidacy for deletion, which means it must exists but can't
   * have any attached attributes.
   *
   * @param truthSetId name identifier
   * @throws NameSystemException
   */
  private void validateCanDeleteName(long truthSetId) throws NameSystemException {
    DbTruthSet dbTruthSet = getTruthSetDAO().get(truthSetId);
    if (dbTruthSet == null) {
      throw new NameDataException(getMessage("name.05.not_found", truthSetId));
    }

    List<DbTruth> dbAttrs = getTruthDAO().getByNameId(truthSetId);
    if (dbAttrs != null && dbAttrs.size() > 0) {
      //TODO: Need log entry to bring up to current logging standard
      throw new NameDataException(getMessage("name.06.has_attr", truthSetId));
    }
  }

  /**
   * Validate all of the details of a potential {@link DbTruth} instance.
   *
   * @param truthSetId       truth set identifier -- must reference a valid truth set entry
   * @param value
   * @param contextValue
   */
  private void validateTruthData(long truthSetId, String value, String contextValue)
      throws NameSystemException {

    DbTruthSet dbTruthSet = getTruthSetDAO().get(truthSetId);
    if (dbTruthSet == null) {
      //TODO: Need log entry to bring up to current logging standard
      throw new NameDataException(getMessage("attr.01.invalid_name_id", truthSetId));
    }

    if (value == null ) {
      throw new NameDataException(getMessage("attr.06.no_gender"));
    }
  }

  /**
   * Validate an attribute's candidacy for deletion, which means it has to exist.
   *
   * @param attrId attribute identifier
   * @throws NameSystemException
   */
  private void validateTruthExists(long attrId) throws NameSystemException {
    DbTruth dbTruth = getTruthDAO().get(attrId);
    if (dbTruth == null) {
      //TODO: Need log entry to bring up to current logging standard
      throw new NameDataException(getMessage("attr.08.not_found", attrId));
    }
  }

  //
  // ====================================================================================
  // Helper methods
  // ====================================================================================
  //

  /**
   * Load the system messages from a properties file
   */
  private void loadSystemMessages() {
    // As a back-up, load the messages from the class path
    LOGGER.warn(null, MODULE_NAME, "Loading message-properties file from class path");
    systemMessages = ResourceBundle.getBundle("messages");
  }

  /**
   * Resolve a message from resource bundle, based on a "key".  The basic set of
   * messages are loaded from the local resources so should always be present.  This
   * list can be overridden to contain custom messages.
   *
   * @param msgKey resource-bundle message key
   * @param values replacement values used when the message is formatted
   * @return Formatted message with value substitution
   */
  private String getMessage(String msgKey, Object... values) {
    String msg;
    try {
      msg = systemMessages.getString(msgKey);
    }
    catch (MissingResourceException ex) {
      return "Error in data service: " + msgKey;
    }

    return MessageFormat.format(msg, values);
  }
}
