package services;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentService {

    // Helper method to get userType by userId
    private String getUserType(String userId) throws SQLException {
        String userType = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT userType FROM users WHERE userId = ?")) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userType = rs.getString("userType");
            }
        }
        return userType;
    }

    public void enrollStudentToCourse(String studentId, String courseId) {
        String sql = "INSERT INTO student_courses(student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Student enrolled to course successfully.");
            } else {
                System.out.println("❌ Enrollment failed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error enrolling student: " + e.getMessage());
        }
    }

    public void updateStudent(String requesterId, String targetStudentId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM users WHERE userId = ?")) {

            selectStmt.setString(1, targetStudentId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Student (user) not found.");
                return;
            }

            String requesterType = getUserType(requesterId);
            if (requesterType == null) {
                System.out.println("❌ Requester not found.");
                return;
            }

            Scanner scanner = new Scanner(System.in);

            if (requesterType.equalsIgnoreCase("student")) {
                if (!requesterId.equals(targetStudentId)) {
                    System.out.println("❌ You can only update your own profile.");
                    return;
                }

                System.out.print("Enter new first name: ");
                String firstName = scanner.nextLine();

                System.out.print("Enter new last name: ");
                String lastName = scanner.nextLine();

                System.out.print("Enter new address: ");
                String address = scanner.nextLine();

                System.out.print("Enter new phone: ");
                String phone = scanner.nextLine();

                System.out.print("Enter new email: ");
                String email = scanner.nextLine();

                System.out.print("Enter new password: ");
                String password = scanner.nextLine();

                String updateSql = "UPDATE users SET firstName = ?, lastName = ?, address = ?, phone = ?, email = ?, password = ? WHERE userId = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, firstName);
                    updateStmt.setString(2, lastName);
                    updateStmt.setString(3, address);
                    updateStmt.setString(4, phone);
                    updateStmt.setString(5, email);
                    updateStmt.setString(6, password);
                    updateStmt.setString(7, targetStudentId);

                    int updated = updateStmt.executeUpdate();
                    if (updated > 0) {
                        System.out.println("✅ Profile updated successfully.");
                    } else {
                        System.out.println("❌ Update failed.");
                    }
                }

            } else if (requesterType.equalsIgnoreCase("admin")) {
                System.out.print("Type 'reset' to reset password to 'Student1': ");
                String input = scanner.nextLine();

                if ("reset".equalsIgnoreCase(input)) {
                    String updateSql = "UPDATE users SET password = ? WHERE userId = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, "Student1");
                        updateStmt.setString(2, targetStudentId);

                        int updated = updateStmt.executeUpdate();
                        if (updated > 0) {
                            System.out.println("✅ Password reset successfully.");
                        } else {
                            System.out.println("❌ Password reset failed.");
                        }
                    }
                } else {
                    System.out.println("❌ Invalid input. Password reset cancelled.");
                }

            } else {
                System.out.println("❌ You do not have permission to update this user.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error updating student: " + e.getMessage());
        }
    }

    public void addGrade(String studentId, String assignmentId, String grade) {
        String sql = "UPDATE assignments SET grade = ? WHERE student_id = ? AND assignment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, grade);
            stmt.setString(2, studentId);
            stmt.setString(3, assignmentId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Grade updated successfully.");
            } else {
                System.out.println("❌ Could not update grade (check IDs).");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error adding grade: " + e.getMessage());
        }
    }

    public void viewAllStudents() {
        String sql = "SELECT userId, firstName, lastName FROM users WHERE userType = 'student'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("📋 All Students:");
            while (rs.next()) {
                System.out.printf("ID: %s, Name: %s %s%n",
                        rs.getString("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving students: " + e.getMessage());
        }
    }

    public void viewAllStudentCourses() {
        String sql = "SELECT s.userId, s.firstName, s.lastName, c.course_name FROM users s " +
                "LEFT JOIN student_courses sc ON s.userId = sc.student_id " +
                "LEFT JOIN courses c ON sc.course_id = c.id " +
                "WHERE s.userType = 'student' ORDER BY s.userId";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            String currentStudent = "";
            while (rs.next()) {
                String studentId = rs.getString("userId");
                if (!studentId.equals(currentStudent)) {
                    currentStudent = studentId;
                    System.out.printf("\nStudent ID: %s, Name: %s %s%n",
                            studentId,
                            rs.getString("firstName"),
                            rs.getString("lastName"));
                }
                String courseName = rs.getString("course_name");
                if (courseName != null) {
                    System.out.println("  - Course: " + courseName);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving student courses: " + e.getMessage());
        }
    }

    public void viewCoursesByStudent(String studentId) {
        String sql = "SELECT c.course_name FROM student_courses sc " +
                "JOIN courses c ON sc.course_id = c.id WHERE sc.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Courses enrolled by Student ID " + studentId + ":");
                boolean hasCourses = false;
                while (rs.next()) {
                    System.out.println(" - " + rs.getString("course_name"));
                    hasCourses = true;
                }
                if (!hasCourses) {
                    System.out.println("No courses found for this student.");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving courses: " + e.getMessage());
        }
    }
}
