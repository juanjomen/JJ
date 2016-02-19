package org.familysearch.standards.impl;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.TruthValueDAO;
import org.familysearch.standards.model.DbTruthValue;
import org.familysearch.standards.model.exception.PnR_SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanjomen on 2/5/2016.
 */
public class TruthValueDAOImpl extends DAOImplBase implements TruthValueDAO {

  /**
   * @param dataSource data-source for all JDBC operations
   */
  @Autowired
  public TruthValueDAOImpl(BasicDataSource dataSource) {
    super(dataSource);
  }


  @Override
  public DbTruthValue get(long id) throws PnR_SystemException {
    ResultSet rset = null;
    try (Connection conn = this.getConnection();
         Statement stmt = conn.createStatement()) {
      rset = stmt.executeQuery("SELECT * FROM truth_value WHERE id = " + id);
      if (rset.next()) {
        Long truthValueId = getLong(rset, "id");
        Long truthId = getLong(rset, "truth_id");
        String val = getString(rset, "val");
        String contextValue = getString(rset, "context_val");
        Long score = getLong(rset, "score");
        Boolean isPositive = getBoolean(rset, "is_positive");

        return new DbTruthValue(truthValueId,truthId,val,contextValue,score,isPositive);
      }
    }
    catch (SQLException ex) {
     // LOGGER.error(ex, MODULE_NAME, "Unable to retrieve attribute", "attr-id", String.valueOf(id));
      throw new PnR_SystemException(ex.getMessage());
    }
    finally {
      if (null != rset) {
        try {
          rset.close();
        }
        catch (SQLException ex) {
          throw new PnR_SystemException(ex.getMessage());
        }
      }
    }
    return null;
  }

  @Override
  public List<DbTruthValue> getByTruthId(Long truthId) throws PnR_SystemException {
    List<DbTruthValue> results = new ArrayList<>();

    try (Connection conn = this.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rset = stmt.executeQuery("SELECT * FROM truth_value WHERE set_id = " + truthId)) {
      while (rset.next()) {

        Long truthValueId = getLong(rset, "id");
        Long truth_Id = getLong(rset, "truth_id");
        String val = getString(rset, "val");
        String contextValue = getString(rset, "context_val");
        Long score = getLong(rset, "score");
        Boolean isPositive = getBoolean(rset, "is_positive");

        results.add(new DbTruthValue(truthValueId,truth_Id,val,contextValue,score,isPositive));
      }
    }
    catch (SQLException ex) {
      //LOGGER.error(ex, MODULE_NAME, "Unable to retrieve attributes by name", "name-id", String.valueOf(nameId));
      throw new PnR_SystemException(ex.getMessage());
    }

    return results;
  }

  @Override
  public DbTruthValue create(Long truthId, String val, String contextVal, Long score, boolean isPositive) throws PnR_SystemException {
    DbTruthValue newTruthValue = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      String query = "INSERT INTO truth_value (truthId, val, contextVal, score, isPositive) VALUES( ?, ?, ?, ?, ?)";
      ResultSet rset = null;
      try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        setLong(stmt, 1, truthId);
        setString(stmt, 2,val);
        setString(stmt, 3, contextVal);
        setLong(stmt, 4, score);
        setBoolean(stmt, 5, isPositive);

        int cnt = stmt.executeUpdate();
        if (cnt == 1) {
          rset = stmt.getGeneratedKeys();
          if (rset.next()) {
            long truthValueId = rset.getLong(1);
            newTruthValue = new DbTruthValue(truthValueId,truthId,val,contextVal,score,isPositive);
//            LOGGER.info(
//              null, MODULE_NAME, "New attribute created",
//              "attrId", String.valueOf(attrId),
//              "nameId", String.valueOf(nameId),
//              "typeId", String.valueOf(typeId),
//              "repGroupId", String.valueOf(repGroupId),
//              "isMale", String.valueOf(isMale),
//              "isFemale", String.valueOf(isFemale),
//              "fromYear", String.valueOf(fromYear),
//              "toYear", String.valueOf(toYear),
//              "frequency", String.valueOf(freq),
//              "weight", String.valueOf(weight));
          }
          else {
            // LOGGER.error(null, MODULE_NAME, "Unable to create attribute -- couldn't get generated key");
          }
        }
        else {
          // LOGGER.error(null, MODULE_NAME, "Unable to create attribute -- couldn't insert data");
        }
        conn.commit();
      }
      catch (SQLException ex) {
        conn.rollback();
        // LOGGER.error(ex, MODULE_NAME, "Unable to create attribute -- system error");
        throw new PnR_SystemException(ex.getMessage());
      }
      finally {
        conn.setAutoCommit(true);
        if (null != rset) {
          try {
            rset.close();
          }
          catch (SQLException ex) {
            throw new PnR_SystemException(ex.getMessage());
          }
        }
      }
    }
    catch (SQLException ex) {
      //LOGGER.error(ex, MODULE_NAME, "Unable to create attribute -- no connection available");
      throw new PnR_SystemException(ex.getMessage());
    }

    return newTruthValue;
  }

  @Override
  public DbTruthValue update(Long id, Long truthId, String val, String contextVal, Long score, boolean isPositive) throws PnR_SystemException {
    DbTruthValue updatedTruthValue = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      String query =
        "UPDATE truth_value " +
          "   SET truth_id = ?, " +
          "       val = ?, " +
          "       context_val = ?, " +
          "       score = ?, " +
          "       is_positive = ?, " +
          " WHERE id = ?";

      try (PreparedStatement stmt = conn.prepareStatement(query)) {
        setLong(stmt, 1, truthId);
        setString(stmt, 2, val);
        setString(stmt, 3, contextVal);
        setLong(stmt, 4, score);
        setBoolean(stmt, 5, isPositive);
        setLong(stmt,6,id);

        int cnt = stmt.executeUpdate();
        if (cnt == 1) {
          updatedTruthValue = new DbTruthValue(id,truthId,val,contextVal,score,isPositive);
//          LOGGER.info(
//            null, MODULE_NAME, "Attribute update",
//            "attrId", String.valueOf(attrId),
//            "nameId", String.valueOf(nameId),
//            "typeId", String.valueOf(typeId),
//            "repGroupId", String.valueOf(repGroupId),
//            "isMale", String.valueOf(isMale),
//            "isFemale", String.valueOf(isFemale),
//            "fromYear", String.valueOf(fromYear),
//            "toYear", String.valueOf(toYear),
//            "frequency", String.valueOf(freq),
//            "weight", String.valueOf(weight));
        }
        else {
          // LOGGER.error(null, MODULE_NAME, "Unable to update attribute -- couldn't modify data");
        }
        conn.commit();
      }
      catch (SQLException ex) {
        conn.rollback();
        // LOGGER.error(ex, MODULE_NAME, "Unable to update attribute -- system error");
        throw new PnR_SystemException(ex.getMessage());
      }
      finally {
        conn.setAutoCommit(true);
      }
    }
    catch (SQLException ex) {
      // LOGGER.error(ex, MODULE_NAME, "Unable to update attribute -- no connection available");
      throw new PnR_SystemException(ex.getMessage());
    }

    return updatedTruthValue;
  }

  @Override
  public void delete(Long id) throws PnR_SystemException {
    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM truth_value WHERE id = ?")) {
        setLong(stmt, 1, id);
        int delCnt = stmt.executeUpdate();
        if (delCnt == 1) {
          //LOGGER.info(null, MODULE_NAME, "Deleted an attribute", "attributeId", String.valueOf(id));
        }
        else {
          //LOGGER.info(null, MODULE_NAME, "Delete attribute failed", "attributeId", String.valueOf(id));
        }
        conn.commit();
      }
      catch (SQLException ex) {
        conn.rollback();
        //LOGGER.error(ex, MODULE_NAME, "Unable to delete attribute -- system error");
        //throw new NameSystemException(ex.getMessage());
      }
      finally {
        conn.setAutoCommit(true);
      }
    }
    catch (SQLException ex) {
      //LOGGER.error(ex, MODULE_NAME, "Unable to delete attribute -- no connection available");
      throw new PnR_SystemException(ex.getMessage());
    }
  }
}
