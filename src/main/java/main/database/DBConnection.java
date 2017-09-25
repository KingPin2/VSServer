package main.database;

import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseException;

import java.sql.*;

/**
 * @author D.bergum
 *
 * Manage main.main.database connection
 */
public class DBConnection {

    Connection con;
    Statement statement;

    private boolean working;

    /**
     * Initialise connection objects
     */
    protected DBConnection(){
        con = null;
        working = false;
    }

    /**
     * Connect to main.main.database
     * @throws DatabaseConnectionException
     */
    protected void openDB() throws DatabaseConnectionException {
        try {
            while (working) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e){
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
        } catch ( Exception e ) {
            working = false;
            e.printStackTrace();
            throw new DatabaseConnectionException("Could not open database.");
        }
        System.out.println("Opened database successfully.");
    }

    /**
     * Close connection to main.main.database
     * @throws DatabaseConnectionException
     */
    protected void closeDB() throws DatabaseConnectionException {
        try {
            while (working) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e){
                }
            }
            working = true;
            if (isOpen()) {
                statement.close();
                con.close();
            }
            con = null;
            working = false;
        } catch (Exception e) {
            working = false;
            throw new DatabaseConnectionException("Could not close database.");
        }
        System.out.println("Closed database successfully.");
    }

    /**
     * Renews the used statement
     * @throws DatabaseException
     */
    protected void renewStatement() throws DatabaseException {
        try {
            if (isOpen()) {
                while (working) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e){
                    }
                }
                working = true;
                statement.close();
                statement = null;
                statement = con.createStatement();
                working = false;
            }
        } catch (Exception e) {
            working = false;
            throw new DatabaseException("Could not renew statement.");
        }
    }

    /**
     * Check if connected to main.main.database
     * @return connected
     */
    protected boolean isOpen() {
        try {
            if (con != null && !con.isClosed()){
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    /**
     * Execute sql query
     * @param sql sql query
     * @return ResultSet
     * @throws DatabaseConnectionException
     */
    private ResultSet executeQuery(String sql) throws DatabaseConnectionException, DatabaseException {
        if (isOpen()) {
            try {
                return statement.executeQuery(sql);
            } catch (SQLException e) {
                throw new DatabaseException("Can't execute query.");
            }
        } else {
            throw  new DatabaseConnectionException("Not connected to database.");
        }
    }

    /**
     * Execute sql update
     * @param sql sql update
     * @throws DatabaseConnectionException
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
            throw  new DatabaseConnectionException("Not connected to database.");
        }
    }

    /**
     * Execute sql insert
     * @param sql sql insert
     * @throws DatabaseConnectionException
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
            throw  new DatabaseConnectionException("Not connected to database.");
        }
    }

    /**
     * Redirect sql to specific handler
     * Call free() after execute SELECT and closing ResultSet
     * @param sql sql statement
     * @return ResultSet, if SELECT; null else
     * @throws DatabaseException
     * @throws DatabaseConnectionException
     */
    protected ResultSet execute(String sql) throws DatabaseException, DatabaseConnectionException {
        try {
            while (working) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e){
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
                    } catch (Exception e){
                        throw new DatabaseException("Error creating table");
                    }
                    working = false;
                    return null;
                case "DROP":
                    try {
                        statement.executeUpdate(sql);
                        con.commit();
                    } catch (Exception e){
                        throw new DatabaseException("Error dropping table");
                    }
                    working = false;
                    return null;
                default:
                    working = false;
                    throw new DatabaseException("Invalid query: " + pref);
            }
        } catch (Exception e){
            working = false;
            throw e;
        }
    }

    /**
     * Set the working flag to false
     */
    protected void free(){
        working = false;
    }
}
