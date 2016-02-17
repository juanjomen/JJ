package api;

import Db.DbTruth;
import exception.NameSystemException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.List;

/**
 * Implement the basic {@link TruthDAO} functionality.
 *
 * @author wjohnson000
 */
public class TruthDAOImpl extends DAOImplBase implements TruthDAO {

  /**
   * @param dataSource data-source for all JDBC operations
   */
  @Autowired
  public TruthDAOImpl(BasicDataSource dataSource) {
    super(dataSource);
  }

  /* (non-Javadoc)
   * @see AttributeDAO#get(long)
   */
  @Override
  public DbTruth get(long id) throws NameSystemException {
    ResultSet rset = null;
//    try (Connection conn = this.getConnection();
//         Statement stmt = conn.createStatement()) {
//      rset = stmt.executeQuery("SELECT * FROM truth WHERE truth_id = " + id);
//      if (rset.next()) {
//        Long truthId = getLong(rset, "truth_id");
//        Long setId = getLong(rset, "set_id");
//        String value = getString(rset,"value");
//        String contextValue = getString(rset,"context_value");
//        return new DbTruth(truthId, setId, value, contextValue);
//      }
//    }
//    catch (SQLException ex) {
//      LOGGER.error(ex, MODULE_NAME, "Unable to retrieve truth", "truth-id", String.valueOf(id));
//      throw new NameSystemException(ex.getMessage());
//    }
//    finally {
//      if (null != rset) {
//        try {
//          rset.close();
//        }
//        catch (SQLException ex) {
//          throw new NameSystemException(ex.getMessage());
//        }
//      }
//    }
    return null;
  }

  /* (non-Javadoc)
   * @see AttributeDAO#create(long, java.lang.Long, java.lang.String, java.lang.String, java.lang.Long, java.lang.Boolean, java.lang.Boolean, java.lang.Integer, java.lang.Integer)
   */
  @Override
  public DbTruth create(long setId, String value, String contextValue)
      throws NameSystemException {
    DbTruth newTruth = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
//    try (Connection conn = this.getConnection()) {
//      conn.setAutoCommit(false);
//      String query = "INSERT INTO truth(Id, setId, value, contextValue) VALUES(?, ?, ?, ?)";
//      ResultSet rset = null;
//      try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//        setLong(stmt, 1, setId);
//        setString(stmt, 2, value);
//        setString(stmt, 3, contextValue);
//
//        int cnt = stmt.executeUpdate();
//        if (cnt == 1) {
//          rset = stmt.getGeneratedKeys();
//          if (rset.next()) {
//            long truthId = rset.getLong(1);
//            newTruth = new DbTruth(truthId, setId, value, contextValue);
//            LOGGER.info(
//                null, MODULE_NAME, "New truth created",
//                "truthId", String.valueOf(truthId),
//                "setId", String.valueOf(setId),
//                "value", String.valueOf(value),
//                "contextValue", String.valueOf(contextValue));
//          }
//          else {
//            LOGGER.error(null, MODULE_NAME, "Unable to create truth -- couldn't get generated key");
//          }
//        }
//        else {
//          LOGGER.error(null, MODULE_NAME, "Unable to create truth -- couldn't insert data");
//        }
//        conn.commit();
//      }
//      catch (SQLException ex) {
//        conn.rollback();
//        LOGGER.error(ex, MODULE_NAME, "Unable to create truth -- system error");
//        throw new NameSystemException(ex.getMessage());
//      }
//      finally {
//        conn.setAutoCommit(true);
//        if (null != rset) {
//          try {
//            rset.close();
//          }
//          catch (SQLException ex) {
//            throw new NameSystemException(ex.getMessage());
//          }
//        }
//      }
//    }
//    catch (SQLException ex) {
//      LOGGER.error(ex, MODULE_NAME, "Unable to create attribute -- no connection available");
//      throw new NameSystemException(ex.getMessage());
//    }

    return newTruth;
  }

  /* (non-Javadoc)
   * @see AttributeDAO#update(long, long, java.lang.Long, java.lang.String, java.lang.String, java.lang.Long, java.lang.Boolean, java.lang.Boolean, java.lang.Integer, java.lang.Integer)
   */
  @Override
  public DbTruth update(long Id, long setId, String value, String contextValue)
      throws NameSystemException {
    DbTruth updTruth = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
//    try (Connection conn = this.getConnection()) {
//      conn.setAutoCommit(false);
//      String query =
//          "UPDATE truth " +
//              "   SET truth_id = ?, " +
//              "       set_id = ?, " +
//              "       value = ?, " +
//              "       contexValue = ?,"+
//              " WHERE truth_id = ?";
//
//      try (PreparedStatement stmt = conn.prepareStatement(query)) {
//        setLong(stmt, 1, Id);
//        setLong(stmt, 2, setId);
//        setString(stmt, 3, value);
//        setString(stmt, 4, contextValue);
//
//
//        int cnt = stmt.executeUpdate();
//        if (cnt == 1) {
//          updTruth =new DbTruth(Id, setId, value, contextValue);
//          LOGGER.info(
//              null, MODULE_NAME, "Attribute update",
//            "truthId", String.valueOf(Id),
//            "setId", String.valueOf(setId),
//            "value", String.valueOf(value),
//            "contextValue", String.valueOf(contextValue));
//        }
//        else {
//          LOGGER.error(null, MODULE_NAME, "Unable to update truth -- couldn't modify data");
//        }
//        conn.commit();
//      }
//      catch (SQLException ex) {
//        conn.rollback();
//        LOGGER.error(ex, MODULE_NAME, "Unable to update truth -- system error");
//        throw new NameSystemException(ex.getMessage());
//      }
//      finally {
//        conn.setAutoCommit(true);
//      }
//    }
//    catch (SQLException ex) {
//      LOGGER.error(ex, MODULE_NAME, "Unable to update truth -- no connection available");
//      throw new NameSystemException(ex.getMessage());
//    }

    return updTruth;
  }

  /* (non-Javadoc)
   * @see AttributeDAO#getByNameId(long)
   */
  @Override
  public List<DbTruth> getByNameId(long truthSetName) throws NameSystemException {
    List<DbTruth> results = null;// new ArrayList<>();

//    try (Connection conn = this.getConnection();
//         Statement stmt = conn.createStatement();
//         ResultSet rset = stmt.executeQuery("SELECT * FROM truth_set WHERE name = " + truthSetName)) {
//      while (rset.next()) {
//        Long truthId = getLong(rset, "truth_id");
//        Long setId = getLong(rset, "set_id");
//        String value = getString(rset, "value");
//        String contextValue = getString(rset, "context_value");
//        results.add(new DbTruth(truthId, setId, value, contextValue));
//      }
//    }
//    catch (SQLException ex) {
//      LOGGER.error(ex, MODULE_NAME, "Unable to retrieve attributes by name", "name-id", String.valueOf(truthSetName));
//      throw new NameSystemException(ex.getMessage());
//    }

    return results;
  }

  /* (non-Javadoc)
   * @see AttributeDAO#delete(long)
   */
  @Override
  public void delete(long id) throws NameSystemException {
    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
//    try (Connection conn = this.getConnection()) {
//      conn.setAutoCommit(false);
//      try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM truth WHERE truth_id = ?")) {
//        setLong(stmt, 1, id);
//        int delCnt = stmt.executeUpdate();
//        if (delCnt == 1) {
//          LOGGER.info(null, MODULE_NAME, "Deleted a truth", "truthId", String.valueOf(id));
//        }
//        else {
//          LOGGER.info(null, MODULE_NAME, "Delete truth failed", "truthId", String.valueOf(id));
//        }
//        conn.commit();
//      }
//      catch (SQLException ex) {
//        conn.rollback();
//        LOGGER.error(ex, MODULE_NAME, "Unable to delete truth -- system error");
//        throw new NameSystemException(ex.getMessage());
//      }
//      finally {
//        conn.setAutoCommit(true);
//      }
//    }
//    catch (SQLException ex) {
//      LOGGER.error(ex, MODULE_NAME, "Unable to delete truth -- no connection available");
//      throw new NameSystemException(ex.getMessage());
//    }
  }

}
