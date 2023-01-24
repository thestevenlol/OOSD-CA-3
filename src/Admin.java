import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends JPanel {

    public Admin(JButton btnProducts, JButton btnBasket, JButton btnLogout) {
        setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnProducts);
        buttonPanel.add(btnBasket);
        buttonPanel.add(btnLogout);

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 6;

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
                e -> {
                    final String name = productNameField.getText();
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid name");
                        return;
                    }

                    final String description = productDescriptionField.getText();
                    if (description.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid description");
                        return;
                    }

                    float price;
                    try {
                        price = Float.parseFloat(productPriceField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid price");
                        return;
                    }

                    int quantity;
                    try {
                        quantity = Integer.parseInt(quantityField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid quantity");
                        return;
                    }

                    String sql = "SELECT * FROM products WHERE LOWER(name) = ?";

                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
                        statement.setString(1, name.toLowerCase());
                        ResultSet resultSet = statement.executeQuery();
                        if (resultSet.next()) {
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

                    sql = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?)";

                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
                        statement.setString(1, name);
                        statement.setString(2, description);
                        statement.setFloat(3, price);
                        statement.setInt(4, quantity);
                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Product added.");
                    } catch (SQLException ex) {
                        Main.logger.severe("Error adding product: ");
                        ex.printStackTrace();
                    }
                }
        );

        removeProductButton.addActionListener(
                e -> {
                    // Get ID and make sure it's valid.
                    int id = 0;
                    try {
                        id = Integer.parseInt(removeProductField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid ID");
                    }

                    // Check if product exists and isn't deleted.
                    String sql = "SELECT * FROM products WHERE id = ? AND deleteFlag = 0";

                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
                        statement.setInt(1, id);
                        try (final ResultSet result = statement.executeQuery()) {
                            if (!result.next()) {
                                JOptionPane.showMessageDialog(null, "Product not found.");
                                return;
                            }
                        }
                    } catch (SQLException ex) {
                        Main.logger.severe("Error checking existence whilst deleting product: ");
                        ex.printStackTrace();
                    }

                    // Delete product.
                    sql = "UPDATE products SET deleteFlag = 1 WHERE id = ?";

                    try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
                        statement.setInt(1, id);
                        statement.executeUpdate();
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
                                JOptionPane.showMessageDialog(null, "Product not found.");
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
