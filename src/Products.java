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
        add(new JScrollPane(table), c);

        c.gridy++;

        final JLabel selectedProduct = new JLabel("Selected product: None");
        add(selectedProduct, c);

        c.gridy++;

        final JLabel selectedProductId = new JLabel("Selected product ID: ");
        final JLabel selectedProductPrice = new JLabel("Selected product price: ");

        add(selectedProductId, c);

        c.gridy++;

        add(selectedProductPrice, c);

        c.gridy++;


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
                }
        );
    }

}