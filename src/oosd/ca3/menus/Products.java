package oosd.ca3.menus;

import oosd.ca3.Main;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Products extends JPanel {

    public Products(final JButton btnBasket, final JButton btnAdmin, final JButton btnLogout) {
        setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnBasket);
        buttonPanel.add(btnAdmin);
        buttonPanel.add(btnLogout);

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);

        add(buttonPanel, c);

        c.gridy++;

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

        final JTable table = new JTable(data, columnNames);
        table.setDefaultEditor(Object.class, null); // Disable editing
        c.gridheight = 5;
        add(new JScrollPane(table), c);
        c.gridheight = 1;

        c.gridy = 1;
        c.gridx++;

        final JLabel selectedProduct = new JLabel("Selected product: None");
        final JLabel selectedProductId = new JLabel("Selected product ID: ");
        final JLabel selectedProductPrice = new JLabel("Selected product price: ");
        final JLabel selectedProductQuantity = new JLabel("Purchase quantity: ");
        final JTextField purchaseQuantity = new JTextField(5);
        final JCheckBox editQuantity = new JCheckBox("Edit quantity");

        c.anchor = GridBagConstraints.NORTH;
        add(selectedProduct, c);
        c.gridy++;
        add(selectedProductId, c);
        c.gridy++;
        add(selectedProductPrice, c);
        c.gridy++;
        add(selectedProductQuantity, c);
        c.gridy++;
        add(editQuantity, c);
        c.anchor = GridBagConstraints.CENTER;



        // On row selected.
        table.getSelectionModel().addListSelectionListener(
                e -> {
                    int id;
                    try {
                        id = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0));
                    } catch (NumberFormatException ex) {
                        Main.logger.severe("Error getting product ID when clicking on table row: ");
                        ex.printStackTrace();
                        return;
                    }

                    final String name = (String) table.getValueAt(table.getSelectedRow(), 1);
                    final float price = Float.parseFloat((String) table.getValueAt(table.getSelectedRow(), 3));
                    final int quantity = 1;

                    selectedProduct.setText("Selected product: " + name);
                    selectedProductId.setText("Selected product ID: " + id);
                    selectedProductPrice.setText("Selected product price: $" + price * quantity);
                    selectedProductQuantity.setText("Purchase quantity: " + quantity);
                }
        );

        editQuantity.addActionListener(
                e -> {
                    if (editQuantity.isSelected()) {
                        selectedProductQuantity.setText("Purchase quantity: ");
                        c.gridx++;
                        add(purchaseQuantity, c);
                    } else {
                        selectedProductQuantity.setText("Purchase quantity: 1");
                        remove(purchaseQuantity);
                    }
                }
        );
    }

}