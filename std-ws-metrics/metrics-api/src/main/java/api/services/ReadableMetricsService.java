package api.services;

import api.model.DbTruth;
import api.model.DbTruthSet;
import api.model.exception.NameSystemException;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;

/**
 * Master service that controls all read access of the underlying NAME-DB system.
 *
 * @author wjohnson000
 */
public class ReadableMetricsService extends BaseMetricsService {

  /**
   * Constructor requires a non-null data-source
   *
   * @param dataSource data-source
   */
  public ReadableMetricsService(BasicDataSource dataSource) throws NameSystemException {
    super(dataSource);
  }

  /**
   * Perform a clean shutdown of any services
   */
  public void shutdown() {
    // For now, do nothing
  }


  // ====================================================================================
  // READ methods for Truth sets data
  // ====================================================================================
  //

  public DbTruthSet getTruthSet(long truthSetId) throws NameSystemException {
    return getTruthSetDAO().get(truthSetId);
  }

  public DbTruthSet getTruthSet(String truthSetName) throws NameSystemException {
    return getTruthSetDAO().get(truthSetName);
  }

  public List<DbTruthSet> searchTruthSet(
      String type, String name, String version)
      throws NameSystemException {
    return getTruthSetDAO().search(type,name,version);
  }

  //
  // ====================================================================================
  // READ methods for ATTRIBUTE data
  // ====================================================================================
  //
  public DbTruth getAttribute(long attrId) throws NameSystemException {
    return getTruthDAO().get(attrId);
  }

  public List<DbTruth> getAttributesByName(long nameId) throws NameSystemException {
    return getTruthDAO().getByNameId(nameId);
  }
}
