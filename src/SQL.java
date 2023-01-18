import java.sql.*;
import java.util.logging.Logger;


    /*
    *
    * File created by Jack Foley
    * 17/01/2023
    *
    */

public class SQL {

    private final Logger logger = Main.logger;
    private Connection connection;

    public SQL() {
        connect();

        // Table creation
        // Customers
        String sqlString = "CREATE TABLE IF NOT EXISTS customers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "country TEXT NOT NULL," +
                "city TEXT NOT NULL," +
                "phone TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "admin INT(1) NOT NULL DEFAULT(0)" +
                ");";

        try (PreparedStatement statement = this.prepareStatement(sqlString)) {
            this.update(statement);
        } catch (SQLException e) {
            logger.severe("Error creating customers table: ");
            e.printStackTrace();
        }

        // Products
        sqlString = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "stock INTEGER NOT NULL" +
                ");";

        try (PreparedStatement statement = this.prepareStatement(sqlString)) {
            this.update(statement);
        } catch (SQLException e) {
            logger.severe("Error creating products table: ");
            e.printStackTrace();
        }

        // Invoices
        sqlString = "CREATE TABLE IF NOT EXISTS invoices (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_id INTEGER NOT NULL," +
                "date TEXT NOT NULL," +
                "FOREIGN KEY (customer_id) REFERENCES customers(id)" +
                ");";

        try (PreparedStatement statement = this.prepareStatement(sqlString)) {
            this.update(statement);
        } catch (SQLException e) {
            logger.severe("Error creating invoices table: ");
            e.printStackTrace();
        }
    }

    // Connects the application to the database.
    public void connect() {
        if (isConnected()) {
            logger.severe("Already connected to database!");
            return;
        }

        final String connectionUrl = "jdbc:sqlite:storage/database.db";
        try {
            connection = DriverManager.getConnection(connectionUrl);
            logger.info("Connected to database");
        } catch (SQLException e) {
            logger.severe("Failed to connect to database");
            logger.severe("Stack trace:");
            e.printStackTrace();
        }
    }

    // Disconnects the database from the application. Should be used when the application is closed.
    @SuppressWarnings("unused")
    public void disconnect() {
        if (connection == null) return;
        try {
            connection.close();
            logger.info("Disconnected from database");
        } catch (SQLException e) {
            logger.severe("Failed to disconnect from database");
            logger.severe("Stack trace:");
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    // Creates a PreparedStatement to be used with the update() and query() methods.
    public PreparedStatement prepareStatement(final String sql) {
        if (!isConnected()) {
            logger.severe("Failed to prepare statement: not connected to database");
            return null;
        }

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            logger.severe("Failed to prepare statement");
            logger.severe("Stack trace:");
            e.printStackTrace();
        }
        return statement;
    }

    // Sends an update to the database.
    public void update(final PreparedStatement statement) {
        if (!isConnected()) {
            logger.severe("Failed to update: not connected to database");
            return;
        }

        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to update");
            logger.severe("Stack trace:");
            e.printStackTrace();
        }
    }

    // Sends a query to the database and returns the result.
    public ResultSet query(final PreparedStatement statement) {
        if (!isConnected()) {
            logger.severe("Failed to query: not connected to database");
            return null;
        }

        ResultSet result = null;
        try {
            result = statement.executeQuery();
        } catch (SQLException e) {
            logger.severe("Failed to query");
            logger.severe("Stack trace:");
            e.printStackTrace();
        }
        return result;
    }



}
