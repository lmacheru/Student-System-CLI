package services;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminService {

    public void unblockUser(String userId) {
        String sql = "UPDATE users SET blocked = FALSE WHERE userid = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ User unblocked successfully.");
            } else {
                System.out.println("❌ User not found or could not unblock.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error unblocking user: " + e.getMessage());
        }
    }
}
