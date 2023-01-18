import java.util.logging.Logger;

public class Main {

    public static final Logger logger = Logger.getLogger("CA3");
    public static SQL sql;

    public static void main(String[] args) {
        sql = new SQL();
        new Login();
    }
}