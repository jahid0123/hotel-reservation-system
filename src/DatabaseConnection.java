import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "isdb62";

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);

    }

}
