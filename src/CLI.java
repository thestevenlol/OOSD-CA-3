import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CLI {

    /*
    * Used for testing purposes
    * Created by Jack Foley C00274246
    * 18/01/2023
    */

    private final SQL sql = Main.sql;

    public CLI() {
        String[] options = {
                "1. Login",
                "2. Register"
        };

        for (String option : options) {
            System.out.println(option);
        }

        getOption();
    }

    public void getOption() {
        final Scanner scanner = new Scanner(System.in);
        System.out.print("Enter option: ");
        if (scanner.hasNextInt()) {
            final int option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println("Login");
                    login();
                    break;
                case 2:
                    System.out.println("Register");
                    register();
                    break;
                default:
                    System.out.println("Invalid option");
                    getOption();
                    break;
            }
        } else {
            System.out.println("Invalid option");
            getOption();
        }
        scanner.close();
    }

    public void login() {
        String input = null;
        String email = null;
        String password = null;
        String sqlText = "SELECT * FROM customers WHERE email = ? AND password = ?";
        System.out.print("Enter email: ");
        final Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            input = scanner.nextLine();
            email = input;
        }

        PreparedStatement statement = sql.prepareStatement(sqlText);
        try {
            statement.setString(1, input);
        } catch (SQLException e) {
            Main.logger.severe("Failed to set email.");
            e.printStackTrace();
        }

        System.out.print("Enter password: ");
        if (scanner.hasNextLine()) {
            input = scanner.nextLine();
            password = input;
        }

        try {
            statement.setString(2, input);
        } catch (SQLException e) {
            Main.logger.severe("Failed to set email.");
            e.printStackTrace();
        }

        ResultSet set = sql.query(statement);

        try {
            if (!set.next()) {
                Main.logger.severe("Failed to get email or password.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (email == null) {
            Main.logger.severe("Email is null.");
            return;
        }

        if (password == null) {
            Main.logger.severe("Password is null.");
            return;
        }

        try {
            if (email.equals(set.getString("email")) && password.equals(set.getString("password"))) {
                System.out.println("Logged in");
            } else {
                System.out.println("Invalid email or password");
            }
        } catch (SQLException e) {
            Main.logger.severe("Failed to get email or password.");
            e.printStackTrace();
        }

        scanner.close();

        try {
            statement.close();
        } catch (SQLException e) {
            Main.logger.severe("Failed to close statement.");
            e.printStackTrace();
        }
    }

    public void register() {
        final Scanner scanner = new Scanner(System.in);

        System.out.print("Enter email: ");
        String email = null;
        if (scanner.hasNextLine()) {
            email = scanner.nextLine();
        }

        System.out.print("Enter password: ");
        String password = null;
        if (scanner.hasNextLine()) {
            password = scanner.nextLine();
        }

        System.out.print("Enter first name: ");
        String firstName = null;
        if (scanner.hasNextLine()) {
            firstName = scanner.nextLine();
        }

        System.out.print("Enter last name: ");
        String lastName = null;
        if (scanner.hasNextLine()) {
            lastName = scanner.nextLine();
        }

        System.out.print("Enter country: ");
        String country = null;
        if (scanner.hasNextLine()) {
            country = scanner.nextLine();
        }

        System.out.print("Enter city: ");
        String city = null;
        if (scanner.hasNextLine()) {
            city = scanner.nextLine();
        }

        System.out.print("Enter phone number: ");
        String phoneNumber = null;
        if (scanner.hasNextLine()) {
            phoneNumber = scanner.nextLine();
        }

        String sqlText = "INSERT INTO customers (email, password, first_name, last_name, country, city, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        ;
        try (PreparedStatement statement = sql.prepareStatement(sqlText)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, country);
            statement.setString(6, city);
            statement.setString(7, phoneNumber);
            sql.update(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
