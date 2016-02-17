package api;

import Db.DbTruthSet;
import exception.NameSystemException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.List;

/**
 * Implement the basic {@link TruthSetDAO} functionality.  It includes the main method for
 * pulling a list of names based on some attribute criteria.
 *
 * @author wjohnson000
 */
public class TruthSetDAOImpl extends DAOImplBase implements TruthSetDAO {

  /**
   * @param dataSource data-source for all JDBC operations
   */
  @Autowired
  public TruthSetDAOImpl(BasicDataSource dataSource) throws NameSystemException {
    super(dataSource);
  }

  /* (non-Javadoc)
   * @see NameDAO#get(long)
   */
  @Override
  public DbTruthSet get(long id) throws NameSystemException {
//    try (Connection conn = this.getConnection();
//         Statement stmt = conn.createStatement();
//         ResultSet rset = stmt.executeQuery("SELECT * FROM truthSets WHERE truthSet_id = " + id)) {
//      if (rset.next()) {
//        Long truthSetId = getLong(rset, "truthSet_id");
//        String type = getString(rset, "truthSet_type");
//        String name = getString(rset, "truthSet_name");
//        String version = getString(rset, "version");
//
//        return new DbTruthSet(truthSetId, type, name, version);
//      }
//    }
//    catch (SQLException ex) {
//      LOGGER.error(ex, MODULE_NAME, "Unable to retrieve truth set", "truth set-id", String.valueOf(id));
//      throw new NameSystemException(ex.getMessage());
//    }

    return null;
  }

  /* (non-Javadoc)
   * @see NameDAO#get(java.lang.String)
   */
  @Override
  public DbTruthSet get(String name) throws NameSystemException {
    ResultSet rset = null;
//    try (Connection conn = this.getConnection();
//         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM truthSets WHERE text = ?")) {
//      stmt.setString(1, name);
//      rset = stmt.executeQuery();
//      if (rset.next()) {
//        Long truthSetId = getLong(rset, "truthSet_id");
//        String type = getString(rset, "truthSet_type");
//        name = getString(rset, "truthSet_name");
//        String version = getString(rset, "version");
//
//        return new DbTruthSet(truthSetId, type, name, version);
//      }
      return null;
//    }
//    catch (SQLException ex) {
//      LOGGER.error(ex, MODULE_NAME, "Unable to retrieve truth set", "name", name);
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
  }

  /* (non-Javadoc)
   * @see NameDAO#create(java.lang.String, java.lang.String)
   */
  @Override
  public DbTruthSet create(String type,String name,String version) throws NameSystemException {
    DbTruthSet newTruthSet = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    ResultSet rset = null;
//    try (Connection conn = this.getConnection()) {
//      conn.setAutoCommit(false);
//      try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO truthSets(type,name,version) VALUES(?, ?,?)", Statement.RETURN_GENERATED_KEYS)) {
//        setString(stmt, 1, type);
//        setString(stmt, 2, name);
//        setString(stmt, 3, version);
//        int cnt = stmt.executeUpdate();
//        if (cnt == 1) {
//          rset = stmt.getGeneratedKeys();
//          if (rset.next()) {
//            long truthSetId = rset.getLong(1);
//            newTruthSet = new DbTruthSet(truthSetId, type ,name, version);
//            LOGGER.info(
//              null, MODULE_NAME, "New truth set created",
//              "truthSetId", String.valueOf(truthSetId),
//              "type", type,
//              "name", name,
//              "version", version);
//          }
//          else {
//            LOGGER.error(null, MODULE_NAME, "Unable to create truth set -- couldn't get generated key");
//          }
//        }
//        else {
//          LOGGER.error(null, MODULE_NAME, "Unable to create truth set -- couldn't insert data.");
//        }
//        conn.commit();
//      }
//      catch (SQLException ex) {
//        conn.rollback();
//        LOGGER.error(ex, MODULE_NAME, "Unable to create truth set -- system error");
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
//      LOGGER.error(ex, MODULE_NAME, "Unable to create truth set -- no connection available");
//      throw new NameSystemException(ex.getMessage());
//    }

    return newTruthSet;
  }

  /* (non-Javadoc)
   * @see NameDAO#create(java.lang.String, java.lang.String)
   */
  @Override
  public DbTruthSet update(Long id,String type,String name,String version) throws NameSystemException{
    DbTruthSet newTruthSet = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    ResultSet rset = null;
//    try (Connection conn = this.getConnection()) {
//      conn.setAutoCommit(false);
//
//      try (PreparedStatement stmt = conn.prepareStatement("UPDATE truthSets SET type = ?, name = ?, version = ? WHERE truthSet_id = ?;", Statement.RETURN_GENERATED_KEYS)) {
//        setString(stmt, 1, type);
//        setString(stmt, 2, name);
//        setString(stmt, 3, version);
//
//        int cnt = stmt.executeUpdate();
//        if (cnt == 1) {
//          rset = stmt.getGeneratedKeys();
//          if (rset.next()) {
//            Long truthSeId = rset.getLong("truthSet_id");
//            if (!(id.equals(truthSeId))) {
//              LOGGER.info(
//                null, MODULE_NAME, "ID updated does not match ID submitted for update.",
//                "Id submitted", String.valueOf(id),
//                "Id updated", String.valueOf(truthSeId),
//                "type", type,
//                "name", name,
//                "version", version);
//            }
//            newTruthSet = new DbTruthSet(rset.getLong("truthSet_id"), rset.getString("type"),rset.getString("name"),rset.getString("version"));
//            LOGGER.info(
//              null, MODULE_NAME, "New truth set created",
//              "id", String.valueOf(rset.getLong("truthSet_id")),
//              "type", String.valueOf(rset.getLong("truthSet_type")),
//              "name", String.valueOf(rset.getLong("truthSet_name")),
//              "version", String.valueOf(rset.getLong("version")));
//          }
//          else {
//            LOGGER.error(null, MODULE_NAME, "Unable to create truth set -- couldn't get generated key");
//          }
//        }
//        else {
//          LOGGER.error(null, MODULE_NAME, "Unable to create truth set -- couldn't insert data.");
//        }
//        conn.commit();
//      }
//      catch (SQLException ex) {
//        conn.rollback();
//        LOGGER.error(ex, MODULE_NAME, "Unable to create truth set -- system error");
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
//      LOGGER.error(ex, MODULE_NAME, "Unable to create name -- no connection available");
//      throw new NameSystemException(ex.getMessage());
//    }

    return newTruthSet;
  }

  /* (non-Javadoc)
   * @see NameDAO#delete(long)
   */
  @Override
  public void delete(long id) throws NameSystemException {
    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
//    try (Connection conn = this.getConnection()) {
//      conn.setAutoCommit(false);
//      try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM truthSets WHERE name_id = ?")) {
//        setLong(stmt, 1, id);
//        int delCnt = stmt.executeUpdate();
//        if (delCnt == 1) {
//          LOGGER.info(null, MODULE_NAME, "Deleted a truth set", "truthId", String.valueOf(id));
//        }
//        else {
//          LOGGER.info(null, MODULE_NAME, "Delete truth set failed", "truthId", String.valueOf(id));
//        }
//        conn.commit();
//      }
//      catch (SQLException ex) {
//        conn.rollback();
//        LOGGER.error(ex, MODULE_NAME, "Unable to delete truth set -- system error");
//        throw new NameSystemException(ex.getMessage());
//      }
//      finally {
//        conn.setAutoCommit(true);
//      }
//    }
//    catch (SQLException ex) {
//      LOGGER.error(ex, MODULE_NAME, "Unable to delete truth set -- no connection available");
//      throw new NameSystemException(ex.getMessage());
//    }
  }

  /* (non-Javadoc)
   * @see NameDAO#search(java.lang.Long, java.lang.String, java.lang.String, java.lang.Long, java.lang.Boolean, java.lang.Boolean, java.lang.Integer, java.lang.Integer, int, int)
   */
  @Override
  public List<DbTruthSet> search(String type, String name, String version)
    throws NameSystemException {

//    StringBuilder qryBuff = new StringBuilder();
//    qryBuff.append(
//        "SELECT DISTINCT(name.text), name.name_id, name.locale " +
//            "  FROM attribute AS attr " +
//            "  JOIN name AS name on name.name_id = attr.name_id ");
//
//    // Compose the WHERE clause
//    String and = " WHERE ";
//
//    if (typeId != null) {
//      qryBuff.append(and).append(" attr.type_id = ? ");
//      and = " AND ";
//    }
///*
//    if (locale != null && !(locale.equals(""))) {
//      qryBuff.append(and).append(" attr.locale = ? ");
//      and = " AND ";
//    }
//*/
//    if (repGroupId != null) {
//      qryBuff.append(and).append(" attr.rep_group_id = ? ");
//      and = " AND ";
//    }
//    if (fromYear != null) {
//      qryBuff.append(and).append(" ( attr.to_yr IS NULL OR attr.to_yr > ? ) ");
//      and = " AND ";
//    }
//    if (toYear != null) {
//      qryBuff.append(and).append(" ( attr.from_yr IS NULL OR attr.from_yr < ? ) ");
//      and = " AND ";
//    }
//    if (isMale != null && isMale) {
//      qryBuff.append(and).append(" attr.is_male = TRUE ");
//      and = " AND ";
//    }
//    if (isFemale != null && isFemale) {
//      qryBuff.append(and).append(" attr.is_female = TRUE ");
//      and = " AND ";
//    }
//    if (frequency != null) {
//      qryBuff.append(and).append(" attr.frequency IS NULL OR attr.frequency >= ? ");
//      and = " AND ";
//    }
//    if (weight != null) {
//      qryBuff.append(and).append(" attr.weight IS NULL OR attr.weight >= ? ");
//      and = " AND ";
//    }
//
//    // Add the ORDER BY, LIMIT, ROWS
//    qryBuff.append(" ORDER BY name.text ");
//    if (pageSize > 0) {
//      qryBuff.append(" LIMIT ").append(pageSize).append(" ");
//      qryBuff.append(" OFFSET ").append((pageNumber - 1) * pageSize).append(" ");
//    }
//
//    // Run the silly query after setting the parameters, and fetch the results
//    List<DbName> results = new ArrayList<>();
//    ResultSet rset = null;
//    try (Connection conn = this.getConnection();
//         PreparedStatement stmt = conn.prepareStatement(qryBuff.toString())) {
//      int ndx = 1;
//      if (typeId != null) {
//        setLong(stmt, ndx++, typeId);
//      }
///*
//      if (locale != null) {
//        setString(stmt, ndx++, locale);
//      }
//*/
//      if (repGroupId != null) {
//        setLong(stmt, ndx++, repGroupId);
//      }
//      if (fromYear != null) {
//        setInteger(stmt, ndx++, fromYear);
//      }
//      if (toYear != null) {
//        setInteger(stmt, ndx++, toYear);
//      }
//      if (frequency != null) {
//        setLong(stmt, ndx++, frequency);
//      }
//      if (weight != null) {
//        setInteger(stmt, ndx, weight);
//      }
//
//      rset = stmt.executeQuery();
//      while (rset.next()) {
//        long nameId = getLong(rset, "name_id");
//        String text = getString(rset, "text");
//        StdLocale nLocale = new StdLocale(getString(rset, "locale"));
//        results.add(new DbName(nameId, text, nLocale));
//      }
//    }
//    catch (SQLException ex) {
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
//
    return null;
  }
}
