import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    final JButton btnProducts = new JButton("Products");
    final JButton btnBasket = new JButton("Basket");
    final JButton btnAdmin = new JButton("Admin");
    final JButton btnLogout = new JButton("Logout");

    public Menu() { // Default menu
        super("Products");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new Products());
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);

        btnBasket.addActionListener(e -> {
            setTitle("Basket");
            setContentPane(new Basket());
            pack();
        });

        btnProducts.addActionListener(e -> {
            setTitle("Products");
            setContentPane(new Products());
            pack();
        });

        btnAdmin.addActionListener(e -> {
            setTitle("Admin");
            setContentPane(new Admin(btnProducts, btnBasket, btnLogout));
            pack();
        });

        btnLogout.addActionListener(e -> {
            dispose();
            new Login();
        });
    }

    private class Products extends JPanel {

        public Products() {
            setLayout(new GridBagLayout());
            final GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 5);

            add(btnBasket, c);
            c.gridx++;
            add(btnAdmin, c);
            c.gridx++;
            add(btnLogout, c);
        }

    }

    private class Basket extends JPanel {

        public Basket() {
            setLayout(new GridBagLayout());
            final GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 5);

            add(btnProducts, c);
            c.gridx++;
            add(btnAdmin, c);
            c.gridx++;
            add(btnLogout, c);
        }

    }



}
