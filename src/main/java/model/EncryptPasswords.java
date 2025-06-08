package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.codec.binary.*;
import dbase.databaseconnection;

public class EncryptPasswords {

    public static void encryptAllPasswords() {
        System.out.println("Starting password encryption process...");
        
        try (Connection conn = databaseconnection.getConnection()) {
            // Verify connection
            if (conn == null || conn.isClosed()) {
                System.err.println("Failed to get database connection");
                return;
            }

            // Count total users first
            int totalUsers = countUsers(conn);
            System.out.println("Total users in database: " + totalUsers);

            // Start transaction
            conn.setAutoCommit(false);

            String selectQuery = "SELECT user_id, password FROM tbl_user WHERE password IS NOT NULL";
            String updateQuery = "UPDATE tbl_user SET password = ? WHERE user_id = ?";

            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                
                ResultSet rs = selectStmt.executeQuery();
                int processedCount = 0;
                int encryptedCount = 0;
                int skippedCount = 0;

                while (rs.next()) {
                    processedCount++;
                    String userId = rs.getString("user_id");
                    String plainPassword = rs.getString("password");

                    System.out.println("\nProcessing user ID: " + userId);
                    System.out.println("Current password: " + plainPassword);

                    if (isEncrypted(plainPassword)) {
                        System.out.println("Password appears already encrypted - skipping");
                        skippedCount++;
                        continue;
                    }

                    String encryptedPassword = SecurityServlet.encrypt(plainPassword);
                    System.out.println("Encrypted to: " + encryptedPassword);

                    updateStmt.setString(1, encryptedPassword);
                    updateStmt.setString(2, userId);
                    updateStmt.executeUpdate();
                    encryptedCount++;
                }

                conn.commit();
                System.out.println("\nEncryption complete!");
                System.out.println("Records processed: " + processedCount);
                System.out.println("Passwords encrypted: " + encryptedCount);
                System.out.println("Passwords skipped (already encrypted): " + skippedCount);

                if (encryptedCount == 0 && skippedCount == 0) {
                    System.out.println("\nWARNING: No passwords were processed!");
                    System.out.println("Possible causes:");
                    System.out.println("1. No records in tbl_user table");
                    System.out.println("2. All password fields are NULL");
                    System.out.println("3. Database connection issue");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int countUsers(Connection conn) throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM tbl_user";
        try (PreparedStatement stmt = conn.prepareStatement(countQuery);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private static boolean isEncrypted(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        try {
            // More accurate check for your encryption pattern
            return password.matches("^[A-Za-z0-9+/]+={0,2}$") && 
                   password.length() % 4 == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        encryptAllPasswords();
    }
}