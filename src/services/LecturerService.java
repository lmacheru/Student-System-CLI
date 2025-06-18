package services;

import java.sql.*;
import database.DBConnection;  // Make sure this import matches your actual package

public class LecturerService {

    private final Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public void viewEnrolledStudents(String lecturerId, String courseId) throws SQLException {
        String sql = "SELECT s.student_id, s.first_name, s.last_name, s.email "
                + "FROM students s "
                + "JOIN student_courses sc ON s.student_id = sc.student_id "
                + "JOIN lecturer_courses lc ON lc.course_id = sc.course_id "
                + "WHERE lc.lecturer_id = ? AND sc.course_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lecturerId);
            stmt.setString(2, courseId);

            ResultSet rs = stmt.executeQuery();

            System.out.println("Enrolled students for course " + courseId + ":");
            while (rs.next()) {
                String sid = rs.getString("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                System.out.printf("ID: %s, Name: %s %s, Email: %s%n", sid, firstName, lastName, email);
            }
        }
    }

    public void viewCoursesByLecturer(String lecturerId) throws SQLException {
        String sql = "SELECT c.course_id, c.course_name "
                + "FROM courses c "
                + "JOIN lecturer_courses lc ON c.course_id = lc.course_id "
                + "WHERE lc.lecturer_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lecturerId);

            ResultSet rs = stmt.executeQuery();

            System.out.println("Courses assigned to lecturer " + lecturerId + ":");
            while (rs.next()) {
                String courseId = rs.getString("course_id");
                String courseName = rs.getString("course_name");
                System.out.printf("Course ID: %s, Name: %s%n", courseId, courseName);
            }
        }
    }

    public void updateLecturer(String lecturerId, String firstName, String lastName, String email,String phone) throws SQLException {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, email = ? , phone = ?WHERE userid = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(3, phone);
            stmt.setString(4, lecturerId);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Lecturer details updated successfully.");
            } else {
                System.out.println("No lecturer found with ID: " + lecturerId);
            }
        }
    }

    public boolean addGrade(String studentId, String course_id, String grade) {
        String sqlCheck = "SELECT * FROM assignments WHERE student_id = ? AND course_id = ?";
        String sqlUpdate = "UPDATE assignments SET grade = ? WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // Check if the assignment exists for the student
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                checkStmt.setString(1, studentId);
                checkStmt.setString(2, course_id);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Assignment found, update the grade
                    try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                        updateStmt.setString(1, grade);
                        updateStmt.setString(2, studentId);
                        updateStmt.setString(3, course_id);
                        int rowsUpdated = updateStmt.executeUpdate();

                        if (rowsUpdated > 0) {
                            System.out.println("✅ Grade updated successfully.");
                            return true;
                        } else {
                            System.out.println("❌ Failed to update the grade.");
                            return false;
                        }
                    }
                } else {
                    System.out.println("❌ No matching assignment found to update the grade.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error adding/updating grade: " + e.getMessage());
            return false;
        }
    }

    public void resignFromCourse(String lecturerId, String courseId) throws SQLException {
        String sql = "DELETE FROM lecturer_courses WHERE lecturer_id = ? AND course_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lecturerId);
            stmt.setString(2, courseId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Resigned from course successfully.");
            } else {
                System.out.println("No assignment found for this lecturer and course.");
            }
        }
    }
}
