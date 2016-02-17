package org.familysearch.standards.impl;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.TruthSetDAO;
import org.familysearch.standards.model.DbTruthSet;
import org.familysearch.standards.model.exception.PnR_SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanjomen on 2/5/2016.
 */
public class TruthSetDAOImpl extends DAOImplBase implements TruthSetDAO{

  /**
   * @param dataSource data-source for all JDBC operations
   */
  @Autowired
  public TruthSetDAOImpl(BasicDataSource dataSource) throws PnR_SystemException {
    super(dataSource);
  }

  /* (non-Javadoc)
   * @see TruthSetDAO#get(long)
   */
  @Override
  public DbTruthSet get(long id) throws PnR_SystemException {
    try (Connection conn = this.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rset = stmt.executeQuery("SELECT * FROM truth_set WHERE id = " + id)) {
      if (rset.next()) {
        Long setId = getLong(rset, "id");
        String setName = getString(rset, "name");
        String setType = getString(rset, "type");
        String setVersion = getString(rset, "version");

        return new DbTruthSet(setId,setName,setType,setVersion);
      }
    }
    catch (SQLException ex) {
      throw new PnR_SystemException(ex.getMessage());
    }

    return null;
  }

  /* (non-Javadoc)
   * @see TruthSetDAO#get(java.lang.String)
   */
  @Override
  public  DbTruthSet get(String name) throws PnR_SystemException {
    ResultSet rset = null;
    try (Connection conn = this.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM truth_set WHERE name = ?")) {
      stmt.setString(1, name);
      rset = stmt.executeQuery();
      if (rset.next()) {
        Long setId = getLong(rset, "id");
        String setName = getString(rset, "name");
        String setType = getString(rset, "type");
        String setVersion = getString(rset, "version");

        return new DbTruthSet(setId,setName,setType,setVersion);
      }
      return null;
    }
    catch (SQLException ex) {
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
  }

  /* (non-Javadoc)
   * @see NameDAO#get(java.lang.String)
   */
  @Override
  public List<DbTruthSet> getTruthSets() throws PnR_SystemException {
    ResultSet rset = null;
    try (Connection conn = this.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM truth_set")) {
      rset = stmt.executeQuery();
      List<DbTruthSet> truthSetList = new ArrayList<>();
      while (rset.next()) {
        Long setId = getLong(rset, "id");
        String setName = getString(rset, "name");
        String setType = getString(rset, "type");
        String setVersion = getString(rset, "version");
        truthSetList.add(new DbTruthSet(setId,setName,setType,setVersion));
      }
      return truthSetList;
    }
    catch (SQLException ex) {
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
  }

  /* (non-Javadoc)
   * @see NameDAO#create(java.lang.String, java.lang.String)
   */
  @Override
  public DbTruthSet create(String name,String type, String version) throws PnR_SystemException {
    DbTruthSet newTruthSet = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    ResultSet rset = null;
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      if (type == null) {
        type = ("placeRep");
      }
      try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO truth_set(name, type, version) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
        setString(stmt, 1, name);
        setString(stmt, 2, type);
        setString(stmt, 3, version);
        int cnt = stmt.executeUpdate();
        if (cnt == 1) {
          rset = stmt.getGeneratedKeys();
          if (rset.next()) {
            long setId = rset.getLong(1);
            newTruthSet = new DbTruthSet(setId, name, type, version);
//            LOGGER.info(
//              null, MODULE_NAME, "New name created",
//              "nameId", String.valueOf(nameId),
//              "text", text,
//              "locale", locale.getLocaleAsString());
          }
          else {
          //  LOGGER.error(null, MODULE_NAME, "Unable to create name -- couldn't get generated key");
          }
        }
        else {
        //  LOGGER.error(null, MODULE_NAME, "Unable to create name -- couldn't insert data.");
        }
        conn.commit();
      }
      catch (SQLException ex) {
        conn.rollback();
        //LOGGER.error(ex, MODULE_NAME, "Unable to create name -- system error");
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
      //LOGGER.error(ex, MODULE_NAME, "Unable to create name -- no connection available");
      throw new PnR_SystemException(ex.getMessage());
    }

    return newTruthSet;
  }

  /* (non-Javadoc)
   * @see NameDAO#create(java.lang.String, java.lang.String)
   */
  @Override
  public DbTruthSet update(Long id,String name,String type, String version) throws PnR_SystemException {
    DbTruthSet updatedTruthSet = null;

    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    ResultSet rset = null;
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      if (type == null) {
        type = ("placeRep");
      }
      try (PreparedStatement stmt = conn.prepareStatement("UPDATE truth_set SET type = ?, name = ?, version =? WHERE id = ?;", Statement.RETURN_GENERATED_KEYS)) {
        setString(stmt, 1, type);
        setString(stmt, 2, name);
        setString(stmt, 3, version);
        setLong(stmt, 4, id);
        int cnt = stmt.executeUpdate();
        if (cnt == 1) {
          rset = stmt.getGeneratedKeys();
          if (rset.next()) {
            Long TruthSetId = rset.getLong("id");
            if (!(id.equals(TruthSetId))) {
//              LOGGER.info(
//                null, MODULE_NAME, "ID updated does not match ID submitted for update.",
//                "Id submitted", String.valueOf(id),
//                "Id updated", String.valueOf(nameId),
//                "text", text,
//                "locale", locale.getLocaleAsString());
            }
            updatedTruthSet = new DbTruthSet(rset.getLong("id"), rset.getString("type"), rset.getString("name"),rset.getString(version));
//            LOGGER.info(
//              null, MODULE_NAME, "New name created",
//              "nameId", String.valueOf(rset.getLong("name_id")),
//              "text", rset.getString("text"),
//              "locale", rset.getString("locale"));
          }
          else {
           // LOGGER.error(null, MODULE_NAME, "Unable to create name -- couldn't get generated key");
          }
        }
        else {
          //LOGGER.error(null, MODULE_NAME, "Unable to create name -- couldn't insert data.");
        }
        conn.commit();
      }
      catch (SQLException ex) {
        conn.rollback();
       // LOGGER.error(ex, MODULE_NAME, "Unable to create name -- system error");
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
      //LOGGER.error(ex, MODULE_NAME, "Unable to create name -- no connection available");
      throw new PnR_SystemException(ex.getMessage());
    }

    return updatedTruthSet;
  }

  /* (non-Javadoc)
   * @see NameDAO#delete(long)
   */
  @Override
  public void delete(long id) throws PnR_SystemException {
    // The following construct tries to capture the Java 7 "try with resources"
    // paradigm with manual transaction control
    try (Connection conn = this.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM truth_set WHERE id = ?")) {
        setLong(stmt, 1, id);
        int delCnt = stmt.executeUpdate();
        if (delCnt == 1) {
          //LOGGER.info(null, MODULE_NAME, "Deleted a name", "nameId", String.valueOf(id));
        }
        else {
          //LOGGER.info(null, MODULE_NAME, "Delete name failed", "nameId", String.valueOf(id));
        }
        conn.commit();
      }
      catch (SQLException ex) {
        conn.rollback();
       // LOGGER.error(ex, MODULE_NAME, "Unable to delete name -- system error");
        throw new PnR_SystemException(ex.getMessage());
      }
      finally {
        conn.setAutoCommit(true);
      }
    }
    catch (SQLException ex) {
      //LOGGER.error(ex, MODULE_NAME, "Unable to delete name -- no connection available");
      throw new PnR_SystemException(ex.getMessage());
    }
  }

}
