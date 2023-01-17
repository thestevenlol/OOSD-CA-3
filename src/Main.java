import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Main {

    public static final Logger logger = Logger.getLogger("CA3");
    public static SQL sql;

    public static void main(String[] args) {
        sql = new SQL();

        // Table creation
        // Customers
        String sqlString = "CREATE TABLE IF NOT EXISTS customers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "country TEXT NOT NULL," +
                "city TEXT NOT NULL," +
                "phone TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        try (PreparedStatement statement = sql.prepareStatement(sqlString)) {
            sql.update(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Products

    }
}