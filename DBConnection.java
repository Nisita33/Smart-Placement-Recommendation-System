import java.sql.Connection;
import java.lang.ClassNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Single place that knows how to connect to MySQL.
 * Update URL / USER / PASSWORD to match your local setup.
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/placement_system";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // For older MySQL Connector/J versions you may need:
            // Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
    throw new SQLException("MySQL Driver not found: " + e.getMessage());
}
        
        catch (SQLException e) {
            System.out.println("DB Connection failed: " + e.getMessage());
            throw e;
        }
    }
}
