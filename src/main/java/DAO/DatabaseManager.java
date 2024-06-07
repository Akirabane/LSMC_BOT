package DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

    private static String url;
    private static String username;
    private static String password;

    static {
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);

            url = properties.getProperty("DATABASE_URL");
            username = properties.getProperty("DATABASE_USER");
            password = properties.getProperty("DATABASE_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
