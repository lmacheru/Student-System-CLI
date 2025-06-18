package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBInitializer sets up the required database tables for the Student Management System.
 * It creates tables if they do not already exist.
 */
public class DBInitializer {

    /**
     * Initializes all required tables for the application.
     */
    public static void initializeDatabase() {
        // Updated 'users' table to include 'idNumber' field
        String usersTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "userId TEXT PRIMARY KEY," +                          // Unique user ID (e.g., Adm001)
                "firstName TEXT NOT NULL," +                          // User's first name
                "lastName TEXT NOT NULL," +                           // User's last name
                "idNumber TEXT NOT NULL," +                           // South African ID Number
                "address TEXT NOT NULL," +                            // Residential address
                "email TEXT NOT NULL," +                              // Email address
                "phone TEXT NOT NULL," +                              // Contact number
                "dateOfBirth TEXT NOT NULL," +                        // Date of birth
                "userType TEXT NOT NULL," +                           // Role: admin, student, lecturer
                "password TEXT NOT NULL," +                           // Account password
                "blocked INTEGER DEFAULT 0" +                         // 0 = active, 1 = blocked
                ");";


        String studentCoursesTableSql = "CREATE TABLE IF NOT EXISTS student_courses (" +
                "student_id TEXT NOT NULL," +                         // Links to userId of student
                "course_id TEXT NOT NULL," +                          // Links to course ID
                "PRIMARY KEY (student_id, course_id)," +
                "FOREIGN KEY (student_id) REFERENCES users(userId) ON DELETE CASCADE," +
                "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE" +
                ");";

        String lecturerCoursesTableSql = "CREATE TABLE IF NOT EXISTS lecturer_courses (" +
                "lecturer_id TEXT NOT NULL," +                        // Links to userId of lecturer
                "course_id TEXT NOT NULL," +                          // Links to course ID
                "PRIMARY KEY (lecturer_id, course_id)," +
                "FOREIGN KEY (lecturer_id) REFERENCES users(userId) ON DELETE CASCADE," +
                "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE" +
                ");";

        String assignmentsTableSql = "CREATE TABLE IF NOT EXISTS assignments (" +
                "assignment_id TEXT PRIMARY KEY," +                  // Unique assignment ID
                "student_id TEXT NOT NULL," +                        // References users (student)
                "course_id TEXT NOT NULL," +                         // References course
                "assignment_name TEXT NOT NULL," +                   // Name of assignment
                "grade TEXT," +                                      // Optional grade (A, B+, etc.)
                "FOREIGN KEY (student_id) REFERENCES users(userId) ON DELETE CASCADE," +
                "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE" +
                ");";

        String deregistrationRequestsTableSql = "CREATE TABLE IF NOT EXISTS deregistration_requests (" +
                "request_id TEXT PRIMARY KEY," +                     // Unique deregistration request ID
                "student_id TEXT NOT NULL," +                        // Student initiating request
                "course_id TEXT NOT NULL," +                         // Course to deregister from
                "requested_by TEXT NOT NULL," +                      // Who submitted (student or admin)
                "status TEXT NOT NULL DEFAULT 'Pending'," +          // Status of request
                "request_date TEXT NOT NULL," +                      // Date of request
                "FOREIGN KEY (student_id) REFERENCES users(userId) ON DELETE CASCADE," +
                "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE," +
                "FOREIGN KEY (requested_by) REFERENCES users(userId) ON DELETE CASCADE" +
                ");";



        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create each table
            stmt.execute(usersTableSql);
            stmt.execute(studentCoursesTableSql);
            stmt.execute(lecturerCoursesTableSql);
            stmt.execute(assignmentsTableSql);
            stmt.execute(deregistrationRequestsTableSql);

            System.out.println("✅ Database tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
        }
    }
}
