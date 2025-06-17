package services;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseService {

    public void assignLecturerToCourse(String lecturerId, String courseId) {
        String sql = "INSERT INTO lecturer_courses(lecturer_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lecturerId);
            stmt.setString(2, courseId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Lecturer assigned to course successfully.");
            } else {
                System.out.println("❌ Assignment failed.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error assigning lecturer: " + e.getMessage());
        }
    }
}
