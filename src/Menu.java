import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    public Menu() { // Default menu
        super("Products");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new Products());
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public Menu(JPanel panel, String title) { // For opening new windows
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(panel);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private class Products extends JPanel {

        public Products() {
            setLayout(new GridBagLayout());
            final GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 5);

            final JButton btnBasket = new JButton("Basket");
            final JButton btnAdmin = new JButton("Admin");
            final JButton btnLogout = new JButton("Logout");

            add(btnBasket, c);
            c.gridx++;
            add(btnAdmin, c);
            c.gridx++;
            add(btnLogout, c);

            btnBasket.addActionListener(e -> {
                new Menu(new Basket(), "Basket");
                dispose();
            });

            btnAdmin.addActionListener(e -> {
                new Menu(new Admin(), "Admin");
                dispose();
            });
        }

    }

    private class Basket extends JPanel {

        public Basket() {
            setLayout(new GridBagLayout());
            final GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 5);

            final JButton btnProducts = new JButton("Products");
            final JButton btnAdmin = new JButton("Admin");
            final JButton btnLogout = new JButton("Logout");

            add(btnProducts, c);
            c.gridx++;
            add(btnAdmin, c);
            c.gridx++;
            add(btnLogout, c);

            btnProducts.addActionListener(e -> {
                new Menu();
                dispose();
            });

            btnAdmin.addActionListener(e -> {
                new Menu(new Admin(), "Admin");
                dispose();
            });
        }

    }

    private class Admin extends JPanel {

        public Admin() {
            setLayout(new GridBagLayout());
            final GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 5);

            final JButton btnProducts = new JButton("Products");
            final JButton btnBasket = new JButton("Basket");
            final JButton btnLogout = new JButton("Logout");

            add(btnProducts, c);
            c.gridx++;
            add(btnBasket, c);
            c.gridx++;
            add(btnLogout, c);

            c.gridx = 0;
            c.gridy++;

            final JLabel addProductName = new JLabel("Product Name: ");
            add(addProductName, c);


            btnProducts.addActionListener(e -> {
                dispose();
                new Menu();
            });

            btnBasket.addActionListener(e -> {
                dispose();
                new Menu(new Basket(), "Basket");
            });
        }

    }

}
