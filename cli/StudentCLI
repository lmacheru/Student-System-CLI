package cli;

import services.StudentService;

/**
 * StudentCLI provides the CLI interface for logged-in students.
 */
public class StudentCLI {
    private final String studentName;
    private final int studentId;

    public StudentCLI(String studentName, int studentId) {
        this.studentName = studentName;
        this.studentId = studentId;
    }

    public void showStudentDashboard() {
        System.out.println("\n🎓 Welcome, " + studentName + " (ID: " + studentId + ")");
        System.out.println("You have limited access.");
        System.out.println("Features like viewing grades, attendance, or requesting changes coming soon!");
    }
}
