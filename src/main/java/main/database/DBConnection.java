package main.database;

import main.Server;
import main.exceptions.DatabaseConnectionException;
import main.exceptions.DatabaseCreateException;
import main.exceptions.DatabaseDropException;
import main.exceptions.DatabaseException;

import java.sql.*;

/**
 * @author D.bergum
 *         <p>
 *         Manage database connection
 */
class DBConnection {

    private Connection con;
    private Statement statement;

    private boolean working;

    /**
     * Initialise connection objects
     */
    DBConnection() {
        con = null;
        working = false;
    }

    /**
     * Connect to database
     *
     * @throws DatabaseConnectionException Could not connect to database
     */
    void openDB() throws DatabaseConnectionException {
        try {
            while (working) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Server.log.addErrorToLog("openDB: " + e.toString());
                }
            }
            working = true;
            if (isOpen()) {
                con.close();
            }
            con = null;
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:database.db";
            con = DriverManager.getConnection(url);
            con.setAutoCommit(false);
            statement = con.createStatement();
            working = false;
        } catch (Exception e) {
            working = false;
            Server.log.addErrorToLog("openDB: " + e.toString());
            throw new DatabaseConnectionException("Could not open database.");
        }
        Server.log.addToLog("Opened database successfully.");
    }

    /**
     * Check if connected to database
     *
     * @return connected
     */
    boolean isOpen() {
        try {
            if (con != null && !con.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            Server.log.addErrorToLog("isOpen: " + e.toString());
        }
        return false;
    }

    /**
     * Execute sql query
     *
     * @param sql sql query
     * @return ResultSet
     * @throws DatabaseConnectionException Not connected to database
     */
    private ResultSet executeQuery(String sql) throws DatabaseConnectionException, DatabaseException {
        if (isOpen()) {
            try {
                return statement.executeQuery(sql);
            } catch (SQLException e) {
                throw new DatabaseException("Can't execute query.");
            }
        } else {
            Server.log.addErrorToLog("executeQuery: Not connected to database.");
            throw new DatabaseConnectionException("Not connected to database.");
        }
    }

    /**
     * Execute sql update
     *
     * @param sql sql update
     * @throws DatabaseConnectionException Not connected to database
     */
    private void executeUpdate(String sql) throws DatabaseConnectionException, DatabaseException {
        if (isOpen()) {
            try {
                statement.executeUpdate(sql);
                con.commit();
            } catch (SQLException e) {
                throw new DatabaseException("Can't execute update.");
            }
        } else {
            Server.log.addErrorToLog("executeUpdate: Not connected to database.");
            throw new DatabaseConnectionException("Not connected to database.");
        }
    }

    /**
     * Execute sql insert
     *
     * @param sql sql insert
     * @throws DatabaseConnectionException Not connected to database
     */
    private ResultSet executeInsert(String sql) throws DatabaseConnectionException, DatabaseException {
        if (isOpen()) {
            try {
                statement.executeUpdate(sql);
                con.commit();
            } catch (SQLException e) {
                throw new DatabaseException("Can't execute insert.");
            }
            try {
                return statement.getGeneratedKeys();
            } catch (Exception e) {
                return null;
            }
        } else {
            Server.log.addErrorToLog("executeInsert: Not connected to database.");
            throw new DatabaseConnectionException("Not connected to database.");
        }
    }

    /**
     * Redirect sql to specific handler
     * Call free() after execute SELECT and closing ResultSet
     *
     * @param sql sql statement
     * @return ResultSet, if SELECT; null else
     * @throws DatabaseException           Invalid query
     * @throws DatabaseConnectionException Not connected to database
     */
    ResultSet execute(String sql) throws DatabaseException, DatabaseConnectionException, DatabaseDropException, DatabaseCreateException {
        try {
            while (working) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Server.log.addErrorToLog("execute: " + e.toString());
                }
            }
            working = true;
            String pref = sql.substring(0, sql.indexOf(" ")).trim().toUpperCase();
            switch (pref) {
                case "SELECT":
                    return executeQuery(sql);
                case "DELETE":
                    executeUpdate(sql);
                    working = false;
                    return null;
                case "INSERT":
                    ResultSet rs = executeInsert(sql);
                    working = false;
                    return rs;
                case "UPDATE":
                    executeUpdate(sql);
                    working = false;
                    return null;
                case "CREATE":
                    try {
                        statement.executeUpdate(sql);
                        con.commit();
                    } catch (Exception e) {
                        throw new DatabaseCreateException();
                    }
                    working = false;
                    return null;
                case "DROP":
                    try {
                        statement.executeUpdate(sql);
                        con.commit();
                    } catch (Exception e) {
                        throw new DatabaseDropException();
                    }
                    working = false;
                    return null;
                default:
                    working = false;
                    throw new DatabaseException("Invalid query: " + pref);
            }
        } catch (DatabaseDropException dbd) {
            throw dbd;
        } catch (Exception e) {
            Server.log.addErrorToLog("execute: " + e.toString());
            working = false;
            throw e;
        }
    }

    /**
     * Set the working flag to false
     */
    void free() {
        working = false;
    }
}
