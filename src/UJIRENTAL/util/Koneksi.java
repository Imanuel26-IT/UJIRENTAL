package UJIRENTAL.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author IMANUEL VALEN LINGGI
 */
public class Koneksi {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_ujirental?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static Connection koneksi;

    public static Connection getConnection() throws SQLException {
        if (koneksi == null || koneksi.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                koneksi = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL JDBC tidak ditemukan: " + e.getMessage(), e);
            }
        }
        return koneksi;
    }
}