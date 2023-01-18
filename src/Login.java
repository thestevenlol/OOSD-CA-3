import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    private String email = null;
    private String password = null;
    
    public Login() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        final JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);

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



        add(panel);


        loginButton.addActionListener(
                e -> {
                    email = emailField.getText();

                    // Using StringBuilder to convert char[] to String
                    final StringBuilder builder = new StringBuilder();
                    for (char c : passwordField.getPassword()) {
                        builder.append(c);
                    }
                    password = builder.toString().trim();
                    login();
                }
        );

        registerButton.addActionListener(
                e -> {
                    this.dispose(); // Closes the window.
                    new Register(); // Opens the registration window.
                }
        );

        pack();
        setVisible(true);
    }
    
    private void login() {
        // TODO: If email is valid but not password, say password invalid instead of no account.
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
        final String sqlString = "SELECT * FROM customers WHERE email = ? AND password = ?";

        // Perform SQL query.
        // Using try-with-resource to automatically close the PreparedStatement when done with it.
        try (final PreparedStatement statement = sql.prepareStatement(sqlString)) {
            statement.setString(1, email); // Set first question mark.
            statement.setString(2, password); // Set second question mark.
            final ResultSet resultSet = sql.query(statement); // Get ResultSet from query.
            if (!resultSet.next()) { // If there is nothing to move the cursor to, there is no account.
                System.out.println("No account.");
                return;
            }
            System.out.println("Logged in.");
        } catch (SQLException e) {
            Main.logger.severe("There was an error with logging in.");
            e.printStackTrace();
        }
    }

}