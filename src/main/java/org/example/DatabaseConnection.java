package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Veritabanı bilgilerini buraya giriyoruz
    private static final String URL = "jdbc:postgresql://localhost:5432/onlineshopping";
    private static final String USER = "postgres"; // Kendi PostgreSQL kullanıcı adın
    private static final String PASSWORD = "12345678"; // Kendi PostgreSQL şifreni buraya yaz!

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Veritabanına başarıyla bağlanıldı!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
            return null;
        }
    }

    // Test etmek için geçici bir main metodu
    public static void main(String[] args) {
        getConnection();
    }


}
