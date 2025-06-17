package services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles uploading and storing of assignments per student.
 */
public class AssignmentService {
    private Map<Integer, File> studentAssignments = new HashMap<>();

    public void uploadAssignment(int studentId, File file) {
        studentAssignments.put(studentId, file);
        System.out.println("Assignment uploaded for student ID: " + studentId);
    }

    public File getAssignment(int studentId) {
        return studentAssignments.get(studentId);
    }
}
