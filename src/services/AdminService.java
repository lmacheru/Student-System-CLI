package services;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminService {

    /**
     * Block a user by username (e.g., when deregistered).
     */
    public boolean blockUser(String userId) {
        String sql = "UPDATE users SET blocked = TRUE WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ User " + userId + " has been blocked.");
                return true;
            } else {
                System.out.println("⚠️ User " + userId + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error blocking user: " + e.getMessage());
        }
        return false;
    }

    /**
     * Unblock a user by username.
     */
    public boolean unblockUser(String userId) {
        String sql = "UPDATE users SET blocked = FALSE WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ User " + userId + " has been unblocked.");
                return true;
            } else {
                System.out.println("⚠️ User " + userId + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error unblocking user: " + e.getMessage());
        }
        return false;
    }

    /**
     * Assign a lecturer to a course.
     */
    public boolean assignLecturerToCourse(String lecturerId, String courseId) {
        String sql = "INSERT INTO course_lecturers (course_id, lecturer_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseId);
            ps.setString(2, lecturerId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Lecturer " + lecturerId + " assigned to course " + courseId);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error assigning lecturer: " + e.getMessage());
        }
        return false;
    }

    /**
     * Optional: remove lecturer from a course.
     */
    public boolean removeLecturerFromCourse(String lecturerId, String courseId) {
        String sql = "DELETE FROM course_lecturers WHERE course_id = ? AND lecturer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseId);
            ps.setString(2, lecturerId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Lecturer " + lecturerId + " removed from course " + courseId);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error removing lecturer: " + e.getMessage());
        }
        return false;
    }
}
