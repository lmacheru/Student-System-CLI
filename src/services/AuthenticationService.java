package services;

import java.sql.*;

public class AuthenticationService {
    private static final String DB_URL = "jdbc:sqlite:src/database/student_system.db"; // Adjust path if needed

    // Check if user exists
    public boolean userExists(String userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if user is blocked
    public boolean isBlocked(String userId) {
        String sql = "SELECT blocked FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("blocked") == 1;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Block user after too many failed attempts
    public boolean blockUser(String userId) {
        String sql = "UPDATE users SET blocked = 1 WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate login credentials and that user is not blocked
    public boolean validateLogin(String userId, String password) {
        String sql = "SELECT password, blocked FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int blocked = rs.getInt("blocked");
                if (blocked == 1) {
                    return false; // user blocked
                }
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
            return false; // user not found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get user type (admin/student)
    public String getUserType(String userId) {
        String sql = "SELECT userType FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("userType");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Register user
    public boolean register(String userId, String firstName, String lastName, String address,
                            String email, String phone, String dob, String userType, String password) {
        String sql = "INSERT INTO users(userId, firstName, lastName, address, email, phone, dateOfBirth, userType, password, blocked) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, address);
            stmt.setString(5, email);
            stmt.setString(6, phone);
            stmt.setString(7, dob);
            stmt.setString(8, userType.toLowerCase());
            stmt.setString(9, password);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
