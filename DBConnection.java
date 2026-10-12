import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = env("MYSQL_URL",
        "jdbc:mysql://localhost:3306/placement_system");
    private static final String USER = env("MYSQL_USER", "root");
    private static final String PASSWORD = env("MYSQL_PASSWORD", "");

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? fallback : v;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver not found: " + e.getMessage());
        }
    }
}
