package org.application.dbairline.model.data;

import org.application.dbairline.controller.AdministrationWindowController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAUtility {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static boolean isSet = false;

    /**
     * Sets the credentials for connecting to the MySQL server with the specified URL and the given user and password.
     *
     * @param url      the URL of the database
     * @param user     the username to connect to the database
     * @param password the password to connect to the database
     * @return {@code true} if the credentials are successfully set, {@code false} otherwise
     */
    public static boolean setCredentials(String url, String user, String password) {
        if (!isSet) {
            URL = url;
            USER = user;
            PASSWORD = password;
            isSet = true;
            return true;
        }
        return false;
    }

    /**
     * Resets the credentials for connecting to the MySQL server.
     * After calling this method, the URL, user, password, and isSet fields will be set to null or false.
     * This method should be called when the connection to the database is no longer needed or when an error occurs during connection.
     */
    public static void resetCredentials() {
        URL = null;
        USER = null;
        PASSWORD = null;
        isSet = false;
    }

    /**
     * Retrieves a connection to the MySQL database.
     *
     * @return a Connection object representing the connection to the database
     * @throws SQLException if an error occurs while establishing the connection
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }

    /**
     * Retrieves a connection to the MySQL database with the specified isolation level.
     *
     * @param isolationLevel the isolation level for the connection.
     *                       It can be one of the following constants defined in the java.sql.Connection class:
     *                       - Connection.TRANSACTION_NONE
     *                       - Connection.TRANSACTION_READ_UNCOMMITTED
     *                       - Connection.TRANSACTION_READ_COMMITTED
     *                       - Connection.TRANSACTION_REPEATABLE_READ
     *                       - Connection.TRANSACTION_SERIALIZABLE
     * @return a Connection object representing the connection to the MySQL database.
     * @throws SQLException if an error occurs while establishing the connection.
     */
    public static Connection getConnection(Integer isolationLevel) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setTransactionIsolation(isolationLevel);
        return connection;
    }

    /**
     * Retrieves a Connection object to connect to the MySQL database with the specified auto commit and isolation level settings.
     *
     * @param doAutoCommit   the auto commit setting for the Connection object
     *                       true if auto commit is enabled, false if it is disabled
     * @param isolationLevel the isolation level for the Connection object
     *                       as defined by the java.sql.Connection constants:
     *                       - Connection.TRANSACTION_NONE
     *                       - Connection.TRANSACTION_READ_UNCOMMITTED
     *                       - Connection.TRANSACTION_READ_COMMITTED
     *                       - Connection.TRANSACTION_REPEATABLE_READ
     *                       - Connection.TRANSACTION_SERIALIZABLE
     * @return a Connection object to connect to the MySQL database with the specified settings
     * @throws SQLException if an error occurs while establishing the connection
     */
    public static Connection getConnection(Boolean doAutoCommit, Integer isolationLevel) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(doAutoCommit);
        connection.setTransactionIsolation(isolationLevel);
        return connection;
    }

    //TODO: update every class using this method to include rollback

    /**
     * Executes an update SQL statement using the given Connection, SQL query, and optional parameters.
     *
     * @param connection the Connection to use for executing the statement
     * @param query      the SQL query to execute
     * @param params     the optional parameters to be used in the SQL query
     * @return the number of rows affected by the update statement
     * @throws SQLException if an error occurs while executing the statement
     */
    public static int executeUpdate(Connection connection, String query, Object... params) throws SQLException, IllegalArgumentException {
        if (connection == null) {
            throw new SQLException("Connection object is null");
        }
        if (query != null && !query.trim().isEmpty() && params != null) {
            PreparedStatement queryStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                queryStatement.setObject(i + 1, params[i]);
            }
            return queryStatement.executeUpdate();

        } else {
            throw new IllegalArgumentException("Invalid SQL query or parameters provided.");
        }
    }

    /**
     * Executes an update SQL statement using the given Connection, SQL query, and optional parameters.
     *
     * @param connection the Connection to use for executing the statement
     * @param query      the SQL query to execute
     * @param params     the optional parameters to be used in the SQL query
     * @return the number of rows affected by the update statement
     * @throws SQLException if an error occurs while executing the statement
     */
    public static ResultSet executeUpdateWithGeneratedKeys(Connection connection, String query, Object... params) throws SQLException, IllegalArgumentException {
        if (connection == null) {
            throw new SQLException("Connection object is null");
        }
        if (query != null && !query.trim().isEmpty() && params != null) {
            PreparedStatement queryStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                queryStatement.setObject(i + 1, params[i]);
            }
            boolean result = queryStatement.execute();

            if (!result) {
                return queryStatement.getGeneratedKeys();
            } else {
                throw new SQLException("No keys were generated. Check if the executed statement can return generated keys.");
            }
        }
        return null;
    }

    /**
     * Executes a SQL query with optional parameters and returns the result set.
     *
     * @param query  the SQL query to execute
     * @param params optional parameters to be used in the SQL query
     * @return the result set generated by the query
     * @throws SQLException if an error occurs while executing the query
     */
    public static ResultSet executeQuery(Connection connection, String query, Object... params) throws SQLException, IllegalArgumentException {
        if (connection == null) {
            throw new SQLException("Connection object is null");
        }
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL query is null or empty");
        }
        if (!query.trim().toUpperCase().startsWith("SELECT")) {
            throw new IllegalArgumentException("Query must be a SELECT query");
        }

        PreparedStatement queryStatement = connection.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            queryStatement.setObject(i + 1, params[i]);
        }

        return queryStatement.executeQuery();
    }

    /**
     * Retrieves all data from the given ResultSet and returns a DataResult object.
     *
     * @param resultSet the ResultSet containing the data to be retrieved
     * @return a DataResult object containing the column names and data rows
     * @throws SQLException if an error occurs while retrieving the data
     */
    public static AdministrationWindowController.DataResult getAllData(ResultSet resultSet) throws SQLException {

        List<List<Object>> data = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();

        int columnCount = resultSet.getMetaData().getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(resultSet.getMetaData().getColumnName(i));
        }

        while (resultSet.next()) {
            List<Object> row = new ArrayList<>();
            for (int j = 1; j <= columnCount; j++) {
                row.add(resultSet.getObject(j));
            }
            data.add(row);
        }

        return new AdministrationWindowController.DataResult(columnNames, data);
    }
}