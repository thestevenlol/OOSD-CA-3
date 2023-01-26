package oosd.ca3.menus;

import oosd.ca3.Main;
import oosd.ca3.util.TableHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
    *
    */

    private final TableHandler tableHandler = new TableHandler();
    private final JScrollPane basketScrollPane = new JScrollPane();
    private final JTable basketTable = new JTable(tableHandler.getBasketTable());

    public Basket(final JButton btnProducts, final JButton btnAdmin, final JButton btnLogout) {

        setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnProducts);
        buttonPanel.add(btnAdmin);
        buttonPanel.add(btnLogout);

        add(buttonPanel, c);

        c.gridy++;

        // I do not know why, but the table column names are not showing.
        // I have tried to fix this, but I have not been able to.
        // I weep, and commit in defeat.
        // TODO: Fix this.
        add(basketScrollPane.add(basketTable), c);

        c.anchor = GridBagConstraints.NORTH;
        c.gridy = 1;
        c.gridx = 1;

        final JLabel totalLabel = new JLabel("Total: ");
        final JTextField totalTextField = new JTextField(10);
        final JButton removeSelected = new JButton("Remove selected");
        final JButton purchase = new JButton("Purchase");

        add(totalLabel, c);
        c.gridx++;
        add(totalTextField, c);

        c.gridx = 1;
        c.gridy++;
        c.gridwidth = 2;

        add(removeSelected, c);

        c.gridy++;

        add(purchase, c);

        removeSelected.addActionListener(
                e -> {
                    // If no item is selected
                    if (basketTable.getSelectedRow() == -1) {
                        JOptionPane.showMessageDialog(null, "Please select an item to remove.");
                        return;
                    }

                    // Remove the selected item from the basket
                    removeFromTable(basketTable);
                }
        );

    }

    private void removeFromTable(final JTable table) {
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

}
