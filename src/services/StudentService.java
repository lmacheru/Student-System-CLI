package services;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class StudentService {

    /**
     * Enrolls a student to a given course.
     *
     * @param studentId the student ID
     * @param courseId  the course ID
     * @return true if enrollment successful, false otherwise
     */
    public boolean enrollStudentToCourse(String studentId, String courseId) {
        String sql = "INSERT INTO student_course_enrollments (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setString(2, courseId);
            ps.executeUpdate();

            System.out.println("✅ Student " + studentId + " enrolled to course " + courseId);
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error enrolling student to course: " + e.getMessage());
            return false;
        }
    }

    /**
     * Views all courses a student is enrolled in.
     *
     * @param studentId the student ID
     */
    public void viewEnrolledCourses(String studentId) {
        String sql = "SELECT c.course_id, c.course_name FROM courses c " +
                "JOIN student_course_enrollments sce ON c.course_id = sce.course_id " +
                "WHERE sce.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n📚 Courses enrolled by student " + studentId + ":");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("- " + rs.getString("course_id") + ": " + rs.getString("course_name"));
            }
            if (!found) {
                System.out.println("No enrolled courses found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving enrolled courses: " + e.getMessage());
        }
    }


    /**
     * Updates student personal information (like address, phone, etc).
     *
     * @param userId        the ID of the user performing update (for authorization or audit)
     * @param studentIdToUpdate the student ID whose info is being updated
     */
    public void updateStudent(String userId, String studentIdToUpdate) {
        // For simplicity, let's just update address and phone in this example.
        // In practice, you would probably pass parameters or have a dedicated DTO.

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter new address: ");
            String newAddress = scanner.nextLine();

            System.out.print("Enter new phone number: ");
            String newPhone = scanner.nextLine();

            String sql = "UPDATE users SET address = ?, phone = ? WHERE userId = ? AND user_type = 'student'";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, newAddress);
                ps.setString(2, newPhone);
                ps.setString(3, studentIdToUpdate);

                int updated = ps.executeUpdate();
                if (updated > 0) {
                    System.out.println("✅ Student info updated successfully.");
                } else {
                    System.out.println("❌ No student found with ID: " + studentIdToUpdate);
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error updating student info: " + e.getMessage());
        }
    }

    /**
     * Views all students enrolled in courses (for Admin or Lecturer use).
     */
    public void viewEnrolledStudents() {
        String sql = "SELECT u.userId, u.first_name, u.last_name, c.course_id, c.course_name " +
                "FROM users u " +
                "JOIN student_course_enrollments sce ON u.userId = sce.student_id " +
                "JOIN courses c ON sce.course_id = c.course_id " +
                "WHERE u.user_type = 'student'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n👨‍🎓 Enrolled Students:");
            boolean found = false;
            while (rs.next()) {
                found = true;
                String studentId = rs.getString("userId");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String courseId = rs.getString("course_id");
                String courseName = rs.getString("course_name");

                System.out.println(studentId + " - " + name + " | Course: " + courseId + " - " + courseName);
            }

            if (!found) {
                System.out.println("No students enrolled in any courses.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving enrolled students: " + e.getMessage());
        }
    }
    public boolean deregisterStudentFromCourse(String studentId, String courseId) {
        String sql = "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error deregistering student: " + e.getMessage());
            return false;
        }
    }

    public boolean hasEnrolledCourses(String studentId) {
        String sql = "SELECT COUNT(*) FROM student_courses WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking student courses: " + e.getMessage());
        }
        return false;
    }

    /**
     * Submit an assignment for the given student.
     *
     * @param studentId      The ID of the student submitting the assignment
     * @param courseId       The ID of the course
     * @param assignmentName The assignment name/title
     * @return true if submission succeeded, false otherwise
     */
    public boolean submitAssignment(String studentId, String courseId, String assignmentName) {
        String sql = "INSERT INTO assignments (assignment_id, student_id, course_id, assignment_name) VALUES (?, ?, ?, ?)";
        String assignmentId = UUID.randomUUID().toString(); // generate unique ID

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, assignmentId);
            stmt.setString(2, studentId);
            stmt.setString(3, courseId);
            stmt.setString(4, assignmentName);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Display grades for the given student, including course and lecturer info.
     *
     * @param studentId The ID of the student whose grades to view
     */
    public void viewGrades(String studentId) {
        // Query joins assignments, courses, users (lecturer) tables to get full info
        String sql = "SELECT a.assignment_name, a.grade, a.course_id, l.firstName || ' ' || l.lastName AS lecturer_name " +
                "FROM assignments a " +
                "JOIN lecturer_courses lc ON lc.course_id = a.course_id " +
                "JOIN users l ON lc.lecturer_id = l.userId " +
                "WHERE a.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                String assignmentName = rs.getString("assignment_name");
                String grade = rs.getString("grade");
                String courseName = rs.getString("course_id");
                String lecturerName = rs.getString("lecturer_name");

                System.out.println("-------------------------------------");
                System.out.println("Assignment: " + assignmentName);
                System.out.println("Grade: " + (grade != null ? grade : "Not graded yet"));
                System.out.println("Course: " + courseName);
                System.out.println("Lecturer: " + lecturerName);
            }

            if (!found) {
                System.out.println("No grades found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

