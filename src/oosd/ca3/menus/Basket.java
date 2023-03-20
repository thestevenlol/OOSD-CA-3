package oosd.ca3.menus;

import oosd.ca3.Main;
import oosd.ca3.util.TableHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Basket extends JPanel {

    /*
    *
    * TODO:
    *  - Decrement stock when an item is purchased.
    *  - Add a button to remove an item from the basket.
    *  - Add a button to purchase all items in the basket.
    */

    public Basket(final JButton btnProducts, final JButton btnAdmin, final JButton btnLogout) {

        setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;

        // Components
        final JScrollPane basketScrollPane = new JScrollPane();
        TableHandler tableHandler = new TableHandler();
        final JTable basketTable = new JTable(tableHandler.getBasketTable());
        final JLabel totalLabel = new JLabel("Total: ");
        final JTextField totalTextField = new JTextField(10);
        final JButton removeSelected = new JButton("Remove selected");
        final JButton purchase = new JButton("Purchase");
        final JPanel buttonPanel = new JPanel();


        // Component adjustment
        basketTable.setDefaultEditor(Object.class, null); // Disable editing
        basketTable.getTableHeader().setReorderingAllowed(false); // Disable reordering
        totalTextField.setEditable(false);

        // Adding components
        buttonPanel.add(btnProducts);
        buttonPanel.add(btnAdmin);
        buttonPanel.add(btnLogout);

        c.gridwidth = 3;
        add(buttonPanel, c);
        c.gridwidth = 1;

        c.gridy++;
        c.gridheight = 3;
        basketScrollPane.add(basketTable);
        basketScrollPane.setViewportView(basketTable);
        add(basketScrollPane, c);
        c.gridheight = 1;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridx++;
        add(totalLabel, c);
        c.gridx++;
        add(totalTextField, c);

        c.gridwidth = 2;
        c.gridx--;
        c.gridy++;
        add(removeSelected, c);
        c.gridy++;
        add(purchase, c);

        // Events and other logic
        updateTotalPrice(basketTable, totalTextField);

        removeSelected.addActionListener(
                e -> {
                    // If no item is selected
                    if (basketTable.getSelectedRow() == -1) {
                        JOptionPane.showMessageDialog(null, "Please select an item to remove.");
                        return;
                    }

                    // Remove the selected item from the basket
                    removeFromTableAndBasket(basketTable);
                    updateTotalPrice(basketTable, totalTextField);
                }
        );

        purchase.addActionListener(
                e -> {
                    // If no item is selected
                    if (basketTable.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Please add an item to the basket.");
                        return;
                    }

                    // Clear table
                    if (purchase(basketTable)) {
                        ((DefaultTableModel) basketTable.getModel()).setRowCount(0);
                        totalTextField.setText("0.0");
                    }

                }
        );

    }

    private boolean purchase(final JTable table) {
        // Set 'paid' in invoices to 1 where customer_id = Main.userId
        String sql = "UPDATE invoices SET paid = 1 WHERE customer_id = ?";
        try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
            statement.setInt(1, Main.userId);
            statement.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Remove stock from products based on the amount purchased
        sql = "UPDATE products SET stock = stock - ? WHERE id = ?";
        try (final PreparedStatement statement = Main.sql.prepareStatement(sql)) {
            for (int i = 0; i < table.getRowCount(); i++) {
                statement.setInt(1, (int) Float.parseFloat(table.getValueAt(i, 4).toString()));
                statement.setInt(2, (int) Float.parseFloat(table.getValueAt(i, 0).toString()));
                statement.executeUpdate();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void removeFromTableAndBasket(final JTable table) {
        final String sql = "DELETE FROM invoices WHERE customer_id = ? AND product_id = ?";
        try (PreparedStatement statement = Main.sql.prepareStatement(sql)) {
            statement.setInt(1, Main.userId);
            statement.setInt(2, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // remove from table and update
        ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
    }

    private void updateTotalPrice(final JTable table, JTextField totalTextField) {
        // Get total
        float totalPrice = 0;
        final TableColumn totalColumn = table.getColumnModel().getColumn(5);

        // Get each row's total and add it to the totalPrice variable
        for (int i = 0; i < table.getRowCount(); i++) {
            totalPrice += Float.parseFloat(table.getValueAt(i, totalColumn.getModelIndex()).toString());
        }

        // Set the total text field to the total price
        totalTextField.setText(String.valueOf(totalPrice));
    }

}
