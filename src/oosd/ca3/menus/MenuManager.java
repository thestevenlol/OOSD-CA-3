package oosd.ca3.menus;

import oosd.ca3.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
*
* Created by Jack Foley
* Student ID: C00274246
* Object-Oriented Software Development
* Continuous Assessment 3
*
* This class is used to handle all the menus in the application. (Except Login and Register)
* Instantiating this class will display the Products menu.
* Should be invoked after logging in.
*
*/

public class MenuManager extends JFrame {

    final JButton btnProducts = new JButton("Products");
    final JButton btnBasket = new JButton("Basket");
    final JButton btnAdmin = new JButton("Admin");
    final JButton btnLogout = new JButton("Logout");

    public MenuManager() { // Default menu
        super("Products");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(new Products(btnBasket, btnAdmin, btnLogout));
        setLocationRelativeTo(null); // Center the window
        pack();
        setVisible(true);

        btnBasket.addActionListener(e -> {
            setTitle("Basket");
            setContentPane(new Basket(btnProducts, btnAdmin, btnLogout));
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

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Main.sql.disconnect();
            }
        });
    }

}
