package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_FOLDER = "src/database";
    private static final String DB_FILE = "student_system.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FOLDER + File.separator + DB_FILE;

    static {
        // Ensure the database folder exists
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Created database directory: " + folder.getAbsolutePath());
            } else {
                System.err.println("Failed to create database directory: " + folder.getAbsolutePath());
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("Using DB file: " + new File(DB_FOLDER, DB_FILE).getAbsolutePath());
        return DriverManager.getConnection(DB_URL);
    }
}
