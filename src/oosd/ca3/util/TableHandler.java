package oosd.ca3.util;

import oosd.ca3.Main;

import javax.swing.*;
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

        String sql = "SELECT * FROM products";

        try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
            String[] productRow = new String[5];
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                do {
                    productRow[0] = String.valueOf(resultSet.getInt("id"));
                    productRow[1] = resultSet.getString("name");
                    productRow[2] = resultSet.getString("description");
                    productRow[3] = String.valueOf(resultSet.getFloat("price"));
                    productRow[4] = String.valueOf(resultSet.getInt("stock"));
                    products.add(productRow);
                    productRow = new String[5];
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            Main.logger.severe("Error getting products from database: ");
            e.printStackTrace();
        }

        int dataSize = products.size();
        String[][] data = new String[dataSize][5];

        for (int i = 0; i < dataSize; i++) {
            data[i] = products.get(i);
        }

        table = new JTable(data, columnNames);
        return table;
    }

}
