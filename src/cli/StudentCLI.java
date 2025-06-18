package cli;

import services.LecturerService;
import services.StudentService;

import java.util.Scanner;

import static cli.MainMenu.Logged_In_username;

/**
 * StudentCLI provides the command-line interface for logged-in students.
 */
public class StudentCLI {
    private final String studentId; // Student's unique ID
    private final Scanner scanner = new Scanner(System.in); // Scanner for user input
    private final StudentService studentService = new StudentService(); // Student-related service handler
    public final LecturerService lecturerService = new LecturerService();

    /**
     * Constructor to initialize CLI with the logged-in student's ID.
     * @param studentId The ID of the currently logged-in student.
     */
    public StudentCLI(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Displays the student dashboard with available menu options.
     */
    public void showStudentDashboard() {
        while (true) {
            System.out.println("\n🎓 Welcome, Student: " + studentId);
            System.out.println("1. Update My Details");
            System.out.println("2. Submit Assignment");
            System.out.println("3. View My Grades");
            System.out.println("4. Back");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    updateDetails(); // Allows student to update address, phone, and email
                    break;
                case "2":
                    submitAssignment(); // Allows student to submit an assignment
                    break;
                case "3":
                    viewGrades(); // Displays student's grades along with lecturer and course info
                    break;
                case "4":
                    System.out.println("🔙 Returning to main menu...");
                    return; // Exit to main dashboard or login screen
                default:
                    System.out.println("❌ Invalid option. Try again."); // Error for invalid menu input
            }
        }
    }

    /**
     * Allows the student to update their personal details.
     */
    private void updateDetails() {
        System.out.println("\n✏️ Update My Details");

       studentService.updateStudent(Logged_In_username, Logged_In_username);
    }

    /**
     * Allows the student to submit an assignment for a specific course.
     */
    private void submitAssignment() {
        System.out.println("\n📝 Submit Assignment");

        // Prompt for assignment name
        System.out.print("Enter Assignment Name: ");
        String assignmentName = scanner.nextLine();

        // Prompt for course ID
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        // Call service to handle assignment submission
        boolean success = studentService.submitAssignment(studentId, courseId, assignmentName);

        // Show result to user
        if (success) {
            System.out.println("✅ Assignment submitted successfully.");
        } else {
            System.out.println("❌ Failed to submit assignment.");
        }
    }

    /**
     * Displays the student's grades along with course and lecturer information.
     */
    private void viewGrades() {
        System.out.println("\n📊 Your Grades:");
        studentService.viewGrades(studentId); // Call service to fetch and display grades
    }
}
