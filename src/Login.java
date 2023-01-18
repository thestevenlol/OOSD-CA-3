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
        setSize(700, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel parent = new JPanel();
        final JPanel parent2 = new JPanel();

        final JButton loginButton = new JButton("Login");
        final JButton registerButton = new JButton("Register");

        final JTextField emailField = new JTextField(20);
        final JPasswordField passwordField = new JPasswordField(20);

        parent.add(new JLabel("Email: "));
        parent.add(emailField);

        parent.add(new JLabel("Password: "));
        parent.add(passwordField);

        parent2.add(loginButton);
        parent2.add(registerButton);

        add(parent, BorderLayout.CENTER);
        add(parent2, BorderLayout.SOUTH);

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
