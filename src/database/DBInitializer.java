package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initializeDatabase() {
        String usersTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "userId TEXT PRIMARY KEY," +
                "firstName TEXT NOT NULL," +
                "lastName TEXT NOT NULL," +
                "address TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "phone TEXT NOT NULL," +
                "dateOfBirth TEXT NOT NULL," +
                "userType TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "blocked INTEGER DEFAULT 0" +  // 0 = not blocked, 1 = blocked
                ");";

        String coursesTableSql = "CREATE TABLE IF NOT EXISTS courses (" +
                "id TEXT PRIMARY KEY," +
                "course_name TEXT NOT NULL," +
                "description TEXT" +
                ");";

        String studentCoursesTableSql = "CREATE TABLE IF NOT EXISTS student_courses (" +
                "student_id TEXT NOT NULL," +
                "course_id TEXT NOT NULL," +
                "PRIMARY KEY (student_id, course_id)," +
                "FOREIGN KEY (student_id) REFERENCES users(userId) ON DELETE CASCADE," +
                "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE" +
                ");";

        String lecturerCoursesTableSql = "CREATE TABLE IF NOT EXISTS lecturer_courses (" +
                "lecturer_id TEXT NOT NULL," +
                "course_id TEXT NOT NULL," +
                "PRIMARY KEY (lecturer_id, course_id)," +
                "FOREIGN KEY (lecturer_id) REFERENCES users(userId) ON DELETE CASCADE," +
                "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE" +
                ");";

        String assignmentsTableSql = "CREATE TABLE IF NOT EXISTS assignments (" +
                "assignment_id TEXT PRIMARY KEY," +
                "student_id TEXT NOT NULL," +
                "course_id TEXT NOT NULL," +
                "assignment_name TEXT NOT NULL," +
                "grade TEXT," +
                "FOREIGN KEY (student_id) REFERENCES users(userId) ON DELETE CASCADE," +
                "FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE" +
                ");";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(usersTableSql);
            stmt.execute(coursesTableSql);
            stmt.execute(studentCoursesTableSql);
            stmt.execute(lecturerCoursesTableSql);
            stmt.execute(assignmentsTableSql);

            System.out.println("✅ Database tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
        }
    }
}
