package oosd.ca3.menus;

import oosd.ca3.Main;
import oosd.ca3.util.SQL;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends JFrame {

    private final SQL sql = Main.sql;

    public Register() {
        super("oosd.ca3.menus.Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);

        final String[] countries = {
                "Ireland",
                "United Kingdom",
                "United States",
                "Canada",
                "Australia",
                "New Zealand",
                "South Africa",
                "India",
                "China",
                "Japan",
                "Russia",
                "Germany",
                "France",
                "Italy",
                "Spain",
                "Netherlands",
                "Belgium",
                "Sweden",
                "Norway",
                "Denmark",
                "Finland",
                "Iceland",
                "Portugal",
                "Greece",
                "Turkey",
                "Egypt",
                "Morocco",
                "Algeria",
                "Libya",
        };

        // Verification REGEX Patterns
        // Email
        final String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Password
        final String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

        // Phone
        final String phonePattern = "^[0-9]{10}$";

        // Name
        final String namePattern = "^[a-zA-Z]{2,}$";

        final JLabel firstNameLabel = new JLabel("First Name");
        final JTextField firstNameField = new JTextField(10);

        final JLabel lastNameLabel = new JLabel("Last Name");
        final JTextField lastNameField = new JTextField(10);

        final JLabel countryLabel = new JLabel("Country");
        final JComboBox<String> countryBox = new JComboBox<>(countries);
        countryBox.setSelectedIndex(0);

        final JLabel cityLabel = new JLabel("City");
        final JTextField cityField = new JTextField(10);

        final JLabel phoneLabel = new JLabel("Phone Number");
        final JTextField phoneField = new JTextField(10);

        final JLabel emailLabel = new JLabel("Email");
        final JTextField emailField = new JTextField(10);

        final JLabel passwordLabel = new JLabel("Password");
        final JTextField passwordField = new JPasswordField(10);

        final JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        final JTextField confirmPasswordField = new JPasswordField(10);

        final JButton registerButton = new JButton("Register");
        final JButton cancelButton = new JButton("Cancel");

        panel.add(firstNameLabel, c);
        c.gridx++;
        panel.add(firstNameField, c);

        c.gridx = 0;
        c.gridy++;

        panel.add(lastNameLabel, c);
        c.gridx++;
        panel.add(lastNameField, c);

        c.gridx = 0;
        c.gridy++;

        panel.add(countryLabel, c);
        c.gridx++;
        panel.add(countryBox, c);

        c.gridx = 0;
        c.gridy++;

        panel.add(cityLabel, c);
        c.gridx++;
        panel.add(cityField, c);

        c.gridx = 0;
        c.gridy++;

        panel.add(phoneLabel, c);
        c.gridx++;
        panel.add(phoneField, c);

        c.gridx = 0;
        c.gridy++;

        panel.add(emailLabel, c);
        c.gridx++;
        panel.add(emailField, c);

        c.gridx = 0;
        c.gridy++;

        panel.add(passwordLabel, c);
        c.gridx++;
        panel.add(passwordField, c);

        c.gridx = 0;
        c.gridy++;

        panel.add(confirmPasswordLabel, c);
        c.gridx++;
        panel.add(confirmPasswordField, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        panel.add(registerButton, c);

        c.gridy++;
        panel.add(cancelButton, c);

        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        registerButton.addActionListener(
                e -> {
                    // Validation
                    // First Name
                    if (!firstNameField.getText().matches(namePattern)) {
                        JOptionPane.showMessageDialog(null, "First Name must be at least 2 characters long and contain only letters");
                        return;
                    }

                    // Last Name
                    if (!lastNameField.getText().matches(namePattern)) {
                        JOptionPane.showMessageDialog(null, "Last Name must be at least 2 characters long and contain only letters");
                        return;
                    }

                    // Phone
                    if (!phoneField.getText().matches(phonePattern)) {
                        JOptionPane.showMessageDialog(null, "Phone Number must be 10 digits long");
                        return;
                    }

                    // Email
                    if (!emailField.getText().matches(emailPattern)) {
                        JOptionPane.showMessageDialog(null, "Email must be a valid email address");
                        return;
                    }

                    // Password
                    if (!passwordField.getText().matches(passwordPattern)) {
                        if (passwordField.getText().length() < 12) {
                            JOptionPane.showMessageDialog(null, "If password is below 12 characters, it must have at least one uppercase letter, one lowercase letter, one number and one special character.");
                            return;
                        }
                    }

                    // Confirm Password
                    if (!confirmPasswordField.getText().matches(passwordPattern)) {
                        if (confirmPasswordField.getText().length() < 12) {
                            JOptionPane.showMessageDialog(null, "If password is below 12 characters, it must have at least one uppercase letter, one lowercase letter, one number and one special character.");
                            return;
                        }
                    }

                    // Passwords match
                    if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                        JOptionPane.showMessageDialog(null, "Passwords do not match");
                        return;
                    }

                    // All verifications passed if here.
                    // Check if user already exists
                    // If not, add user to database
                    // Check email as it should be unique
                    String sqlText = "SELECT * FROM customers WHERE email = ?";

                    boolean userExists = false;

                    try (final PreparedStatement statement = sql.prepareStatement(sqlText)) {
                        statement.setString(1, emailField.getText().trim()); // Trimmed to remove trailing whitespace.
                        final ResultSet result = statement.executeQuery();
                        if (result.next()) {
                            JOptionPane.showMessageDialog(null, "Email already exists. Please use a different email.");
                            userExists = true;
                        }
                    } catch (SQLException ex) {
                        Main.logger.severe("Error when checking if user already exists on account registration:");
                        ex.printStackTrace();
                    }

                    if (!userExists) {
                        sqlText = "INSERT INTO customers (first_name, last_name, country, city, phone, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

                        try (final PreparedStatement statement = sql.prepareStatement(sqlText)) {
                            statement.setString(1, firstNameField.getText().trim());
                            statement.setString(2, lastNameField.getText().trim());
                            statement.setString(3, (String) countryBox.getSelectedItem()); // Can cast as string as JComboBox<String> only contains strings.
                            statement.setString(4, cityField.getText().trim());
                            statement.setString(5, phoneField.getText().trim());
                            statement.setString(6, emailField.getText().trim());
                            statement.setString(7, passwordField.getText().trim());
                            statement.executeUpdate();
                        } catch (SQLException ex) {
                            Main.logger.severe("Error when adding user to database on account registration:");
                            ex.printStackTrace();
                        }

                        JOptionPane.showMessageDialog(null, "User successfully registered, you can now log in.");
                        dispose();
                        new Login();
                    }

                }
        );

        cancelButton.addActionListener(
                e -> {
                    dispose();
                    new Login();
                }
        );
    }

}
