package com.kajal.security;

import java.sql.*;

public class DatabaseSaver {

    public static void insertSuspiciousIP(String ip, int failedAttempts) {
        // Database details
        String url = "jdbc:mysql://localhost:3306/threatdb";
        String user = "root";             // 🔁 Replace with your MySQL username
        String password = "root"; // 🔁 Replace with your MySQL password

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create connection
            Connection conn = DriverManager.getConnection(url, user, password);

            // Prepare SQL insert query
            String sql = "INSERT INTO suspicious_ips (ip_address, failed_attempts) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ip);
            stmt.setInt(2, failedAttempts);

            // Execute the insert
            stmt.executeUpdate();
            System.out.println("Inserted into DB: " + ip + " → " + failedAttempts + " attempts");

            // Close connections
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        }
    }
}
