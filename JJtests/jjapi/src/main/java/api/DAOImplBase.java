package api;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.core.logging.Logger;

import java.sql.*;

;

/**
 * Base class for all of the DAO classes.  It will manage the database connections.
 * 
 * @author wjohnson000
 *
 */
public abstract class DAOImplBase {

    /** Shared logger and module name used for logging purposes */
    protected static final Logger LOGGER      = Logger.getInstance(api.DAOImplBase.class);
    protected static final String MODULE_NAME = "METRICS_SERVICE";

    /** DataSource from which connections can be gotten */
    private BasicDataSource ds;


    public DAOImplBase(BasicDataSource ds) {
        this.ds = ds;
    }

    /**
     * @return Return a database connection.
     * @throws SQLException 
     */
    protected Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Extract a "String" value from a result-set, checking for null
     * 
     * @param rset result-set
     * @param colName column name
     * @return value from result-set, or null
     * @throws SQLException
     */
    protected String getString(ResultSet rset, String colName) throws SQLException {
        String val = rset.getString(colName);
        return (rset.wasNull()) ? null : val;
    }

    /**
     * Extract an "Integer" value from a result-set, checking for null
     * 
     * @param rset result-set
     * @param colName column name
     * @return value from result-set, or null
     * @throws SQLException
     */
    protected Integer getInteger(ResultSet rset, String colName) throws SQLException {
        Integer val = rset.getInt(colName);
        return (rset.wasNull()) ? null : val;
    }

    /**
     * Extract a "Long" value from a result-set, checking for null
     * 
     * @param rset result-set
     * @param colName column name
     * @return value from result-set, or null
     * @throws SQLException
     */
    protected Long getLong(ResultSet rset, String colName) throws SQLException {
        Long val = rset.getLong(colName);
        return (rset.wasNull()) ? null : val;
    }

    /**
     * Extract a "Boolean" value from a result-set, checking for null
     * 
     * @param rset result-set
     * @param colName column name
     * @return value from result-set, or null
     * @throws SQLException
     */
    protected Boolean getBoolean(ResultSet rset, String colName) throws SQLException {
        Boolean val = rset.getBoolean(colName);
        return (rset.wasNull()) ? null : val;
    }

    /**
     * Set a String parameter into a prepared-statement, allowing for null value
     * @param ps prepared statement
     * @param colNum column number (1-based)
     * @param value String value to set, or NULL
     * @throws SQLException
     */
    protected void setString(PreparedStatement ps, int colNum, String value) throws SQLException {
        if (value == null) {
            ps.setNull(colNum, Types.VARCHAR);
        } else {
            ps.setString(colNum, value);
        }
    }

    /**
     * Set an Integer parameter into a prepared-statement, allowing for null value
     * @param ps prepared statement
     * @param colNum column number (1-based)
     * @param value Integer value to set, or NULL
     * @throws SQLException
     */
    protected void setInteger(PreparedStatement ps, int colNum, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(colNum, Types.INTEGER);
        } else {
            ps.setInt(colNum, value);
        }
    }

    /**
     * Set an Long parameter into a prepared-statement, allowing for null value
     * @param ps prepared statement
     * @param colNum column number (1-based)
     * @param value Double value to set, or NULL
     * @throws SQLException
     */
    protected void setLong(PreparedStatement ps, int colNum, Long value) throws SQLException {
        if (value == null) {
            ps.setNull(colNum, Types.BIGINT);
        } else {
            ps.setLong(colNum, value);
        }
    }

    /**
     * Set a Boolean parameter into a prepared-statement, allowing for null value
     * @param ps prepared statement
     * @param colNum column number (1-based)
     * @param value Boolean value to set, or NULL
     * @throws SQLException
     */
    protected void setBoolean(PreparedStatement ps, int colNum, Boolean value) throws SQLException {
        if (value == null) {
            ps.setNull(colNum, Types.BOOLEAN);
        } else {
            ps.setBoolean(colNum, value);
        }
    }
}
