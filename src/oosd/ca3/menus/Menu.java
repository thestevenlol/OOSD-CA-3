package oosd.ca3.menus;

import javax.swing.*;
import java.awt.*;

/*
*
* Created by Jack Foley
* Student ID: C00274246
* Object-Oriented Software Development
* Continuous Assessment 3
*
* This class is used to handle all the menus in the application.
* Instantiating this class will display the Products menu.
* Should be invoked after logging in.
*
*/

public class Menu extends JFrame {

    final JButton btnProducts = new JButton("Products");
    final JButton btnBasket = new JButton("Basket");
    final JButton btnAdmin = new JButton("Admin");
    final JButton btnLogout = new JButton("Logout");

    public Menu() { // Default menu
        super("Products");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new Products(btnBasket, btnAdmin, btnLogout));
        setLocationRelativeTo(null); // Center the window
        pack();
        setVisible(true);

        btnBasket.addActionListener(e -> {
            setTitle("Basket");
            setContentPane(new Basket());
            pack();
        });

        btnProducts.addActionListener(e -> {
            setTitle("Products");
            setContentPane(new Products(btnBasket, btnAdmin, btnLogout));
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
