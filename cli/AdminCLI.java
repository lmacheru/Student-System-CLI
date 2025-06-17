package cli;

import services.StudentService;
import services.CourseService;
import services.AdminService;

import java.sql.SQLException;
import java.util.Scanner;

import static cli.MainMenu.Logged_In_username;

public class AdminCLI {
    private final Scanner scanner = new Scanner(System.in);
    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();
    private final AdminService adminService = new AdminService();

    // For demo purposes, hardcode admin user ID

    public void showMenu() throws SQLException {
        int choice;

        do {
            System.out.println("\n📚 Admin Dashboard");
            System.out.println("1. Enroll Student to Course");
            System.out.println("2. Update Student Info");
            System.out.println("3. Unblock Student");
            System.out.println("4. Add Grades to Assignment");
            System.out.println("5. View All Students");
            System.out.println("6. View Student Courses");
            System.out.println("7. Enroll Lecturer to Course");
            System.out.println("8. Back");

            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    enrollStudentToCourse();
                    break;
                case 2:
                    updateStudentInfo();
                    break;
                case 3:
                    unblockStudent();
                    break;
                case 4:
                    addStudentGrades();
                    break;
                case 5:
                    studentService.viewAllStudents();
                    break;
                case 6:
                    viewStudentCourses();
                    break;
                case 7:
                    enrollLecturerToCourse();
                    break;
                case 8:
                    System.out.println("🏠 Returning to main menu...");
                    break;
                default:
                    System.out.println("❌ Invalid option. Try again.");
                    break;
            }
        } while (choice != 8);
    }

    private void enrollStudentToCourse() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        studentService.enrollStudentToCourse(studentId, courseId);
    }

    private void updateStudentInfo() throws SQLException {
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine();
        // Pass adminId as requester, and studentId as target student
        studentService.updateStudent(Logged_In_username, studentId);
    }

    private void unblockStudent() {
        System.out.print("Enter Student ID to unblock: ");
        String studentId = scanner.nextLine();
        adminService.unblockUser(studentId);
    }

    private void addStudentGrades() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Assignment ID: ");
        String assignmentId = scanner.nextLine();
        System.out.print("Enter Grade: ");
        String grade = scanner.nextLine();
        studentService.addGrade(studentId, assignmentId, grade);
    }

    private void viewStudentCourses() {
        System.out.print("Enter Student ID (or type 'all' to view all): ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("all")) {
            studentService.viewAllStudentCourses();
        } else {
            studentService.viewCoursesByStudent(input);
        }
    }

    private void enrollLecturerToCourse() {
        System.out.print("Enter Lecturer ID: ");
        String lecturerId = scanner.nextLine();
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        courseService.assignLecturerToCourse(lecturerId, courseId);
    }
}
