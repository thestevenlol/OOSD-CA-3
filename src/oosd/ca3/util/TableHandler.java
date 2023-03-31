package oosd.ca3.util;

import oosd.ca3.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableHandler {

    public JTable getProductsTable() {
        JTable table;
        final String[] columnNames = {"ID", "Name", "Description", "Price", "Quantity"};
        List<String[]> products = new ArrayList<>();

        // SQL String.
        String sql = "SELECT * FROM products WHERE deleteFlag = 0";

        // Create try-with-resources statement to execute the SQL query.
        try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
            // Create a new String array to store the data from the database.
            String[] productRow = new String[5];
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                do {
                    // Populate the String array with the data from the database while the result set has data.
                    productRow[0] = String.valueOf(resultSet.getInt("id"));
                    productRow[1] = resultSet.getString("name");
                    productRow[2] = resultSet.getString("description");
                    productRow[3] = String.valueOf(resultSet.getFloat("price"));
                    productRow[4] = resultSet.getInt("stock") == 0 ? "Out of stock" : String.valueOf(resultSet.getInt("stock"));

                    // Add the String array to the List.
                    products.add(productRow);

                    // Create a new String array to store the next row of data.
                    productRow = new String[5];
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            // If there is an error, print the stack trace.
            Main.logger.severe("Error getting products from database: ");
            e.printStackTrace();
        }

        // Convert the List to a 2D array.
        int dataSize = products.size();
        String[][] data = new String[dataSize][5];

        for (int i = 0; i < dataSize; i++) {
            data[i] = products.get(i);
        }

        // Create a new JTable with the data and column names.
        table = new JTable(data, columnNames);
        return table;
    }

    public TableModel getBasketTable() {
        TableModel model;
        final String[] columnNames = {"Product ID", "Name", "Price Per", "Date Added", "Quantity", "Total Cost"};
        List<String[]> products = new ArrayList<>();

        String sql = "SELECT products.name, products.id, products.price, invoices.date, invoices.quantity, " +
                "invoices.quantity * products.price AS total FROM products INNER JOIN invoices on products.id = product_id " +
                "INNER JOIN customers on invoices.customer_id = ? AND paid = 0 GROUP BY invoices.id";

        try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
            statement.setInt(1, Main.userId);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                do {
                    String[] productRow = new String[6];
                    productRow[0] = String.valueOf(resultSet.getInt("id"));
                    productRow[1] = resultSet.getString("name");
                    productRow[2] = String.valueOf(resultSet.getFloat("price"));
                    productRow[3] = resultSet.getString("date");
                    productRow[4] = String.valueOf(resultSet.getFloat("quantity"));
                    productRow[5] = String.valueOf(resultSet.getFloat("total"));
                    products.add(productRow);
                } while (resultSet.next());
            }
        } catch (final SQLException e) {
            Main.logger.severe("Error getting basket from database: ");
            e.printStackTrace();
        }

        int dataSize = products.size();
        String[][] data = new String[dataSize][4];

        for (int i = 0; i < dataSize; i++) {
            data[i] = products.get(i);
        }

        model = new DefaultTableModel(data, columnNames);
        return model;
    }

}
