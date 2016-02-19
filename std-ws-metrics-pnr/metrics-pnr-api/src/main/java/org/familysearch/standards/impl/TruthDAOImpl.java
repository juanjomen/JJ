package org.familysearch.standards.impl;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.TruthDAO;
import org.familysearch.standards.model.DbTruth;
import org.familysearch.standards.model.exception.PnR_SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanjomen on 2/5/2016.
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
  public DbTruth get(long id) throws PnR_SystemException {
    ResultSet rset = null;
    try (Connection conn = this.getConnection();
         Statement stmt = conn.createStatement()) {
      rset = stmt.executeQuery("SELECT * FROM truth WHERE id = " + id);
      if (rset.next()) {
        Long truthId = getLong(rset, "id");
        Long truthSetId = getLong(rset, "set_id");
        String value = getString(rset, "value");
        String contextValue = getString(rset, "context_val");

        return new DbTruth(truthId,truthSetId,value,contextValue);
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
  public List<DbTruth> getBySetId(long setId) throws PnR_SystemException {
    List<DbTruth> results = new ArrayList<>();

    try (Connection conn = this.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rset = stmt.executeQuery("SELECT * FROM truth WHERE set_id = " + setId)) {
      while (rset.next()) {
        Long truthId = getLong(rset, "id");
        Long truthSetId = getLong(rset, "set_id");
        String value = getString(rset, "val");
        String contextValue = getString(rset, "context_val");

        results.add(new DbTruth(truthId,truthSetId,value,contextValue));
      }
    }
    catch (SQLException ex) {
      //LOGGER.error(ex, MODULE_NAME, "Unable to retrieve attributes by name", "name-id", String.valueOf(nameId));
      throw new PnR_SystemException(ex.getMessage());
    }

    return results;
  }


  /* (non-Javadoc)
   * @see TruthDAO#create(long, java.lang.Long, java.lang.String, java.lang.String, java.lang.Long, java.lang.Boolean, java.lang.Boolean, java.lang.Integer, java.lang.Integer)
   */
  @Override
  public DbTruth create(long setId, String val, String contextVal)
    throws PnR_SystemException {
    DbTruth newTruth = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      String query = "INSERT INTO truth(setId, val, contextVal) VALUES( ?, ?, ?)";
      ResultSet rset = null;
      try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        setLong(stmt, 1, setId);
        setString(stmt, 2,val);
        setString(stmt, 3, contextVal);

        int cnt = stmt.executeUpdate();
        if (cnt == 1) {
          rset = stmt.getGeneratedKeys();
          if (rset.next()) {
            long truthId = rset.getLong(1);
            newTruth = new DbTruth(truthId,setId,val,contextVal);
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

    return newTruth;
  }

  /* (non-Javadoc)
   * @see AttributeDAO#update(long, long, java.lang.Long, java.lang.String, java.lang.String, java.lang.Long, java.lang.Boolean, java.lang.Boolean, java.lang.Integer, java.lang.Integer)
   */
  @Override
  public DbTruth update(long id, long setId, String val, String contextVal)
    throws PnR_SystemException {
    DbTruth updatedTruth = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      String query =
        "UPDATE truth " +
          "   SET set_id = ?, " +
          "       val = ?, " +
          "       context_val = ?, " +
          " WHERE id = ?";

      try (PreparedStatement stmt = conn.prepareStatement(query)) {
        setLong(stmt, 1, setId);
        setString(stmt, 2, val);
        setString(stmt, 3, contextVal);
        setLong(stmt,4,id);

        int cnt = stmt.executeUpdate();
        if (cnt == 1) {
          updatedTruth = new DbTruth(id,setId,val,contextVal);
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

    return updatedTruth;
  }



  /* (non-Javadoc)
   * @see AttributeDAO#delete(long)
   */
  @Override
  public void delete(long id) throws PnR_SystemException {
    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM truth WHERE id = ?")) {
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
