package oosd.ca3.menus;

import oosd.ca3.Main;
import oosd.ca3.util.SQL;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
*
* Created by Jack Foley
* Student ID: C00274246
* Object-Oriented Software Development
* Continuous Assessment 3
*
* This class is used to handle the login screen.
*
*/

public class Login extends JFrame {

    // Variable initialisation.
    private String email = null;
    private String password = null;

    // Constructor
    public Login() {
        super("Login"); // Pass the title to the JFrame constructor.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a JPanel to hold the components.
        final JPanel panel = new JPanel(new GridBagLayout()); // Set the layout to GridBagLayout.
        GridBagConstraints constraints = new GridBagConstraints(); // Constraints for the GridBagLayout.

        // Set x and y grids to 0.
        // Set insets (padding) to 5 on each side.
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);

        // Start adding components to the panel.
        panel.add(new JLabel("Email: "), constraints);
        final JTextField emailField = new JTextField(10);

        constraints.gridx++;
        panel.add(emailField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Password: "), constraints);
        final JPasswordField passwordField = new JPasswordField(10);

        constraints.gridx++;
        panel.add(passwordField, constraints);

        final JButton loginButton = new JButton("Login");
        final JButton registerButton = new JButton("Register");

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(loginButton, constraints);

        constraints.gridx++;
        panel.add(registerButton, constraints);
        // End adding components to the panel.

        // Add the panel to the JFrame.
        add(panel);

        loginButton.addActionListener(
                // Lambda expression to handle the login button.
                // e is the ActionEvent variable.
                e -> {
                    email = emailField.getText(); // Get the email from the email field.

                    // Using StringBuilder to convert char[] to String
                    final StringBuilder builder = new StringBuilder();
                    for (char c : passwordField.getPassword()) {
                        builder.append(c);
                    }
                    password = builder.toString().trim();
                    login(); // Call the login method.
                }
        );

        registerButton.addActionListener(
                e -> {
                    this.dispose(); // Closes the window.
                    new Register(); // Opens the registration window.
                }
        );

        pack(); // Resize the window to fit the components.
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
    
    private void login() {
        // Get SQL instance.
        final SQL sql = Main.sql;

        // Check if email is null.
        if (email == null) {
            Main.logger.severe("Email is null.");
            return;
        }

        // Check if password is null.
        if (password == null) {
            Main.logger.severe("Password is null.");
            return;
        }

        // SQL query string.
        // Set email to lowercase to prevent case sensitivity.
        final String sqlString = "SELECT * FROM customers WHERE LOWER(email) = ?";

        // Perform SQL query.
        // Using try-with-resource to automatically close the PreparedStatement when done with it.
        try (final PreparedStatement statement = sql.prepareStatement(sqlString)) {
            statement.setString(1, email.toLowerCase()); // Set first question mark.
            final ResultSet resultSet = sql.query(statement); // Get ResultSet from query.
            if (!resultSet.next()) { // If there is nothing to move the cursor to, there is no account.
                JOptionPane.showMessageDialog(this, "No account found with that email.");
                return;
            }

            // Get the password from the database.
            final String dbPassword = resultSet.getString("password");

            // Check if the password is correct.
            if (!password.equals(dbPassword)) {
                JOptionPane.showMessageDialog(this, "Incorrect password.");
                return;
            }

            dispose();
            new MenuManager();
            Main.userId = resultSet.getInt("id");
        } catch (SQLException e) {
            Main.logger.severe("There was an error with logging in.");
            e.printStackTrace();
        }
    }

}
