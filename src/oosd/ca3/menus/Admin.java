package oosd.ca3.menus;

import oosd.ca3.Main;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends JPanel {

    // Constructor
    public Admin(JButton btnProducts, JButton btnBasket, JButton btnLogout) {
        setLayout(new GridBagLayout()); // Set the layout to GridBagLayout.
        final GridBagConstraints c = new GridBagConstraints(); // Constraints for the GridBagLayout.

        // Creating a JPanel to hold the buttons so they are centered at the top of the screen.
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnProducts);
        buttonPanel.add(btnBasket);
        buttonPanel.add(btnLogout);

        // Set x and y grids to 0.
        // Set insets (padding) to 5 on each side.
        // Set the fill to horizontal so the button panel fills all the available cell space.
        // Set the gridwidth to 6 so the button panel spans all 6 columns.
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 6;

        // Add the button panel to the Admin panel.
        add(buttonPanel, c);

        // Column 1 - Add

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;

        final JLabel addProductName = new JLabel("Add New Product");
        add(addProductName, c);

        c.gridwidth = 1;
        c.gridy++;

        final JLabel productName = new JLabel("Product Name: ");
        add(productName, c);

        c.gridx++;

        final JTextField productNameField = new JTextField(10);
        add(productNameField, c);


        c.gridx = 0;
        c.gridy++;

        final JLabel productDescription = new JLabel("Product Description: ");
        add(productDescription, c);

        c.gridx++;

        final JTextField productDescriptionField = new JTextField();
        add(productDescriptionField, c);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy++;

        final JLabel productPrice = new JLabel("Product Price: ");
        add(productPrice, c);

        c.gridx++;

        final JTextField productPriceField = new JTextField(10);
        add(productPriceField, c);

        c.gridx = 0;
        c.gridy++;

        final JLabel quantityLabel = new JLabel("Quantity: ");
        add(quantityLabel, c);

        c.gridx++;

        final JTextField quantityField = new JTextField(10);
        add(quantityField, c);

        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 2;

        final JButton addProductButton = new JButton("Add Product");
        add(addProductButton, c);

        // Column 2 - Remove

        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 2;

        final JLabel removeProduct = new JLabel("Product Deletion");
        add(removeProduct, c);

        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy++;

        final JLabel removeProductLabel = new JLabel("Product ID: ");
        add(removeProductLabel, c);

        c.gridx++;

        final JTextField removeProductField = new JTextField(10);
        add(removeProductField, c);

        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 2;

        final JButton removeProductButton = new JButton("Delete Product");
        add(removeProductButton, c);

        c.gridy++;

        final JButton unRemoveProductButton = new JButton("Reinstate Product");
        add(unRemoveProductButton, c);

        // Column 3 - Update

        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 2;

        final JLabel updateProduct = new JLabel("Update Product");
        add(updateProduct, c);

        c.gridwidth = 1;
        c.gridy++;

        final JLabel updateProductLabel = new JLabel("Product ID: ");
        add(updateProductLabel, c);

        c.gridx++;

        final JTextField updateProductField = new JTextField(10);
        add(updateProductField, c);

        c.gridx = 4;
        c.gridy++;

        final JLabel updateProductName = new JLabel("Product Name: ");
        add(updateProductName, c);

        c.gridx++;

        final JTextField updateProductNameField = new JTextField(10);
        add(updateProductNameField, c);

        c.gridx = 4;
        c.gridy++;

        final JLabel updateProductDescription = new JLabel("Product Description: ");
        add(updateProductDescription, c);

        c.gridx++;

        final JTextField updateProductDescriptionField = new JTextField(10);
        add(updateProductDescriptionField, c);

        c.gridx = 4;
        c.gridy++;

        final JLabel updateProductPrice = new JLabel("Product Price: ");
        add(updateProductPrice, c);

        c.gridx++;

        final JTextField updateProductPriceField = new JTextField(10);
        add(updateProductPriceField, c);

        c.gridx = 4;
        c.gridy++;

        final JLabel updateProductQuantity = new JLabel("Product Quantity: ");
        add(updateProductQuantity, c);

        c.gridx++;

        final JTextField updateProductQuantityField = new JTextField(10);
        add(updateProductQuantityField, c);

        c.gridx = 4;
        c.gridy++;
        c.gridwidth = 2;

        final JButton updateProductButton = new JButton("Update Product");
        add(updateProductButton, c);

        c.gridwidth = 1;

        addProductButton.addActionListener(
                // Add a new product to the database.
                // Uses a Lambda expression to create an ActionListener.
                // e refers to the ActionListener event variable.
                e -> {
                    // Get the product name from the text field.
                    // Check that it is not an empty field.
                    final String name = productNameField.getText();
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid name");
                        return;
                    }

                    // Get the product description from the text field.
                    // Check that it is not an empty field.
                    final String description = productDescriptionField.getText();
                    if (description.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid description");
                        return;
                    }


                    // Get the product price from the text field.
                    // Check that it is not an invalid price. (Not a float)
                    float price;
                    try {
                        price = Float.parseFloat(productPriceField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid price");
                        return;
                    }

                    // Get the product quantity from the text field.
                    // Check that it is not an invalid quantity. (Not an int)
                    int quantity;
                    try {
                        quantity = Integer.parseInt(quantityField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid quantity");
                        return;
                    }

                    // SQL statement to select an item from the DB based on the name.
                    // The name is converted to lowercase using 'LOWER(name)' to avoid case sensitivity.
                    String sql = "SELECT * FROM products WHERE LOWER(name) = ?";

                    // Try with resources to automatically close the statement and result set.
                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {

                        // Set the name to the first '?' in the SQL statement.
                        statement.setString(1, name.toLowerCase());

                        // Get the result set from the statement.
                        ResultSet resultSet = statement.executeQuery();

                        // If there is a row, continue.
                        if (resultSet.next()) {

                            // Get the value for the 'deleteFlag' column.
                            // We check this to be more specific to the user on why the product already exists.
                            // If the value is 0, the product exists and is not deleted, so we tell the user to use the update function.
                            // If the value is 1, the product exists and is deleted, so we tell the user to reinstate it instead.
                            final int deleted = resultSet.getInt("deleteFlag");
                            if (deleted == 0) {
                                JOptionPane.showMessageDialog(null, "Product already exists, " +
                                        "use update function instead.\n" +
                                        "Product ID: " + resultSet.getInt("id"));
                                return;
                            }
                            JOptionPane.showMessageDialog(null, "Product already exists, but " +
                                    "is deleted. Reinstate it instead.\n" +
                                    "Product ID: " + resultSet.getInt("id"));
                            return;
                        }
                    } catch (final SQLException ex) {
                        Main.logger.severe("Error checking existence whilst adding product: ");
                        ex.printStackTrace();
                    }

                    // SQL statement to insert a new product into the database.
                    sql = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?)";

                    // Try with resources to automatically close the statement.
                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {

                        // Set the values for the '?' in the SQL statement.
                        statement.setString(1, name);
                        statement.setString(2, description);
                        statement.setFloat(3, price);
                        statement.setInt(4, quantity);
                        statement.executeUpdate();

                        // Clear the text fields.
                        productNameField.setText("");
                        productDescriptionField.setText("");
                        productPriceField.setText("");
                        quantityField.setText("");

                        // Show a message to the user.
                        JOptionPane.showMessageDialog(null, "Product added.");
                    } catch (SQLException ex) {
                        Main.logger.severe("Error adding product: ");
                        ex.printStackTrace();
                    }
                }
        );

        removeProductButton.addActionListener(
                // Add a new product to the database.
                // Uses a Lambda expression to create an ActionListener.
                // e refers to the ActionListener event variable.
                e -> {
                    // Get ID and make sure it's valid. (Check if it's not an int)
                    int id = 0;
                    try {
                        id = Integer.parseInt(removeProductField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid ID");
                    }

                    // Check if product exists and isn't deleted.
                    String sql = "SELECT * FROM products WHERE id = ? AND deleteFlag = 0";

                    // Try with resources to automatically close the statement and result set.
                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {

                        // Set the ID to the first '?' in the SQL statement.
                        statement.setInt(1, id);
                        final ResultSet result = statement.executeQuery();
                        if (!result.next()) {
                            JOptionPane.showMessageDialog(null, "Product not found.");
                            return;
                        }
                    } catch (SQLException ex) {
                        Main.logger.severe("Error checking existence whilst deleting product: ");
                        ex.printStackTrace();
                    }

                    // Delete product by setting the deleteFlag to 1.
                    sql = "UPDATE products SET deleteFlag = 1 WHERE id = ?";

                    // Try with resources to automatically close the statement.
                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {

                        // Set the ID to the first '?' in the SQL statement.
                        statement.setInt(1, id);
                        statement.executeUpdate();

                        // Clear the text field.
                        removeProductField.setText("");

                        // Show a message to the user.
                        JOptionPane.showMessageDialog(null, "Product deleted.");
                    } catch (SQLException ex) {
                        Main.logger.severe("Error deleting product: ");
                        ex.printStackTrace();
                    }
                }
        );

        unRemoveProductButton.addActionListener(
                e -> {
                    // Get ID and make sure it's valid.
                    int id;
                    try {
                        id = Integer.parseInt(removeProductField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid ID");
                        return;
                    }

                    // Check if product exists and is deleted.
                    String sql = "SELECT * FROM products WHERE id = ? AND deleteFlag = 1";

                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
                        statement.setInt(1, id);
                        try (final ResultSet result = statement.executeQuery()) {
                            if (!result.next()) {
                                JOptionPane.showMessageDialog(null, "Product is not deleted.");
                                return;
                            }
                        }
                    } catch (SQLException ex) {
                        Main.logger.severe("Error checking existence whilst reinstating product: ");
                        ex.printStackTrace();
                    }

                    // Re-instate product.
                    sql = "UPDATE products SET deleteFlag = 0 WHERE id = ? AND deleteFlag = 1";

                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
                        statement.setInt(1, id);
                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Product reinstated.");
                    } catch (SQLException ex) {
                        Main.logger.severe("Error reinstating product: ");
                        ex.printStackTrace();
                    }
                }
        );

        updateProductButton.addActionListener(
                e -> {
                    // Get ID and make sure it's a valid int.
                    int id;
                    try {
                        id = Integer.parseInt(updateProductField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid ID");
                        return;
                    }

                    String sql = "SELECT * FROM products WHERE id = ? AND deleteFlag = 0";
                    ResultSet result;

                    // Check for existence
                    final PreparedStatement statement = Main.sql.prepareStatement(sql);
                    try {
                        statement.setInt(1, id);
                        result = statement.executeQuery();
                        if (!result.next()) {
                            JOptionPane.showMessageDialog(null, "Product not found.");
                            return;
                        }
                    } catch (final SQLException ex) {
                        Main.logger.severe("Error querying for product whilst updating: ");
                        ex.printStackTrace();
                        return;
                    }

                    // Get current values
                    String name;
                    String description;
                    float price;
                    int quantity;

                    try {
                        name = result.getString("name");
                        description = result.getString("description");
                        price = result.getFloat("price");
                        quantity = result.getInt("stock");
                        statement.close();
                    } catch (final SQLException ex) {
                        Main.logger.severe("Error getting product data whilst updating: ");
                        ex.printStackTrace();
                        return;
                    }

                    // Get new values
                    if (!updateProductNameField.getText().isEmpty()) {
                        name = updateProductNameField.getText();
                    }

                    if (!updateProductDescriptionField.getText().isEmpty()) {
                        description = updateProductDescriptionField.getText();
                    }

                    if (!updateProductPriceField.getText().isEmpty()) {
                        try {
                            price = Float.parseFloat(updateProductPriceField.getText());
                        } catch (final NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid price");
                            return;
                        }
                    }

                    if (!updateProductQuantityField.getText().isEmpty()) {
                        try {
                            quantity = Integer.parseInt(updateProductQuantityField.getText());
                        } catch (final NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid quantity");
                            return;
                        }
                    }

                    // Update product
                    sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE id = ?";

                    try (final PreparedStatement st = Main.sql.prepareStatement(sql)) {
                        st.setString(1, name);
                        st.setString(2, description);
                        st.setFloat(3, price);
                        st.setInt(4, quantity);
                        st.setInt(5, id);
                        st.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Product updated.");
                    } catch (final SQLException ex) {
                        Main.logger.severe("Error updating product: ");
                        ex.printStackTrace();
                    }

                }
        );
    }

}
