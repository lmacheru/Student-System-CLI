package services;

import java.sql.*;
import java.util.UUID;

/**
 * AuthenticationService handles all user authentication,
 * login validation, registration, and account status (blocked or not).
 */
public class AuthenticationService {
    private static final String DB_URL = "jdbc:sqlite:src/database/student_system.db"; // Update path if needed

    /**
     * Generate a user ID with a prefix based on userType.
     * Examples: AdmXXXX, LecXXXX, StuXXXX
     */
    public String generatePrefixedUserId(String userType) {
        String uuid = UUID.randomUUID().toString().substring(0, 9).toUpperCase();
        switch (userType.toLowerCase()) {
            case "admin":
                return "ADM" + uuid;
            case "lecturer":
                return "LEC" + uuid;
            case "student":
                return "STU" + uuid;
            default:
                return "USR" + uuid;
        }
    }

    /**
     * Check if a user with the given userId exists in the database.
     */
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

    /**
     * Check if a user with the given ID Number already exists in the system.
     */
    public boolean idNumberExists(String idNumber) {
        String sql = "SELECT COUNT(*) FROM users WHERE idNumber = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if the user is currently blocked.
     */
    public boolean isBlocked(String userId) {
        String sql = "SELECT blocked FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("blocked") == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Block a user account (e.g., after multiple failed login attempts).
     */
    public boolean blockUser(String userId) {
        String sql = "UPDATE users SET blocked = 1 WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validate login credentials and ensure the user is not blocked.
     */
    public boolean validateLogin(String userId, String password) {
        String sql = "SELECT password, blocked FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("blocked") != 1 &&
                        rs.getString("password").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get the user type (e.g., admin, lecturer, student) by userId.
     */
    public String getUserType(String userId) {
        String sql = "SELECT userType FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("userType");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Register a new user in the system.
     */
    public boolean register(String userId, String firstName, String idNumber, String lastName, String address,
                            String email, String phone, String dob, String userType, String password) {
        String sql = "INSERT INTO users(userId, firstName, lastName, idNumber, address, email, phone, dateOfBirth, userType, password, blocked) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, firstName);
            stmt.setString(3, idNumber);
            stmt.setString(4, lastName);
            stmt.setString(5, address);
            stmt.setString(6, email);
            stmt.setString(7, phone);
            stmt.setString(8, dob);
            stmt.setString(9, userType.toLowerCase());
            stmt.setString(10, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
