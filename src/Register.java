import javax.swing.*;

public class Register extends JFrame {

    public Register() {
        super("Register");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final String[] countryList = {
                "Ireland",
                "UK",
                "France",
                "Germany",
                "Spain",
                "Poland",
                "Norway",
                "Sweden",
                "Finland",
                "Denmark"
        };

        final JTextField firstName = new JTextField();
        final JTextField lastName = new JTextField();
        final JComboBox<String> countries = new JComboBox<>(countryList);



        setVisible(true);
    }

}
