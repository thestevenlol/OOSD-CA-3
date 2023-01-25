package oosd.ca3.menus;

import oosd.ca3.Main;
import oosd.ca3.util.TableHandler;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Products extends JPanel {

    final TableHandler tableHandler = new TableHandler();

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
        c.gridwidth = 3;

        add(buttonPanel, c);

        c.gridwidth = 1;
        c.gridy++;

        final JTable table = tableHandler.getProductsTable();
        table.setDefaultEditor(Object.class, null); // Disable editing
        c.gridheight = 6;
        add(new JScrollPane(table), c);
        c.gridheight = 1;

        c.gridy = 1;
        c.gridx++;

        final JLabel selectedProduct = new JLabel("Selected product: ");
        final JTextField selectedProductTextField = new JTextField(10);

        final JLabel selectedProductId = new JLabel("Selected product ID: ");
        final JTextField selectedProductIdTextField = new JTextField(10);

        final JLabel selectedProductPrice = new JLabel("Selected product price: ");
        final JTextField selectedProductPriceTextField = new JTextField(10);

        final JLabel selectedProductQuantity = new JLabel("Purchase quantity: ");
        final JTextField selectedProductQuantityTextField = new JTextField(10);
        selectedProductQuantityTextField.setText("1");

        final JButton btnAddToBasket = new JButton("Add to basket");

        selectedProductTextField.setEditable(false);
        selectedProductIdTextField.setEditable(false);
        selectedProductPriceTextField.setEditable(false);

        c.anchor = GridBagConstraints.NORTH;
        add(selectedProduct, c);
        c.gridx++;
        add(selectedProductTextField, c);
        c.gridx--;

        c.gridy++;
        add(selectedProductId, c);
        c.gridx++;
        add(selectedProductIdTextField, c);
        c.gridx--;

        c.gridy++;
        add(selectedProductPrice, c);
        c.gridx++;
        add(selectedProductPriceTextField, c);
        c.gridx--;

        c.gridy++;
        add(new JLabel(" "), c); // Empty Label to fill space.

        c.gridy++;
        add(selectedProductQuantity, c);
        c.gridx++;
        add(selectedProductQuantityTextField, c);
        c.gridx--;

        c.gridy++;
        c.gridwidth = 2;
        add(btnAddToBasket, c);
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

                    selectedProductTextField.setText(name);
                    selectedProductIdTextField.setText(String.valueOf(id));
                    selectedProductPriceTextField.setText(String.valueOf((price * quantity)));
                }
        );

        // Add to basket event
        btnAddToBasket.addActionListener(
                e -> {
                    final int quantity = Integer.parseInt(selectedProductQuantityTextField.getText());
                    final int maxQuantity = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 4));
                    final float price = Float.parseFloat(selectedProductPriceTextField.getText()) * quantity;
                    if (quantity > maxQuantity) {
                        JOptionPane.showMessageDialog(null, "You cannot purchase more than " + maxQuantity + " of this product.");
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "Added " + quantity + " of " + selectedProductTextField.getText() + " to basket.");
                }
        );
    }

}