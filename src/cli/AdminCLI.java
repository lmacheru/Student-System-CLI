package cli;

import services.LecturerService;
import services.StudentService;
import services.CourseService;
import services.AdminService;

import java.sql.SQLException;
import java.util.Scanner;

import static cli.MainMenu.Logged_In_username;

public class AdminCLI {
    private final Scanner scanner = new Scanner(System.in);
    public final LecturerService lecturerService = new LecturerService();

    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();
    private final AdminService adminService = new AdminService();

    public void showMenu() throws SQLException {
        int choice;

        do {
            System.out.println("\n📚 Admin Dashboard");
            System.out.println("1. Enroll Student to Course"); // Enroll registered student to one or more courses
            System.out.println("2. Update Student Info"); // Update student info and optionally unblock
            System.out.println("3. Update Admin Details"); // Update admin's own information
            System.out.println("4. Add Grades to Assignment"); // Grade assignments submitted by students
            System.out.println("5. Deregister Student from Course"); // Deregister student directly, no approval needed
            System.out.println("6. View All Enrolled Students"); // Show only students enrolled in courses
            System.out.println("7. View All School Courses"); // Show all available courses
            System.out.println("8. Assign Lecturer to Course"); // Assign a lecturer to a course
            System.out.println("9. Back");

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
                    updateAdminDetails();
                    break;
                case 4:
                    addStudentGrades();
                    break;
                case 5:
                    deregisterStudentFromCourse();
                    break;
                case 6:
                    studentService.viewEnrolledStudents();
                    break;
                case 7:
                    courseService.viewAllCourses();
                    break;
                case 8:
                    enrollLecturerToCourse();
                    break;
                case 9:
                    System.out.println("🏠 Returning to main menu...");
                    break;
                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        } while (choice != 9);
    }

    private void enrollStudentToCourse() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Course ID (or multiple Course IDs comma-separated): ");
        String[] courseIds = scanner.nextLine().split(",");
        for (String courseId : courseIds) {
            studentService.enrollStudentToCourse(studentId.trim(), courseId.trim());
        }
    }

    private void updateStudentInfo() throws SQLException {
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine();
        studentService.updateStudent(Logged_In_username, studentId);

        System.out.print("Do you want to unblock this student if blocked? (yes/no): ");
        String unblock = scanner.nextLine();
        if (unblock.equalsIgnoreCase("yes")) {
            adminService.unblockUser(studentId);
        }
    }

    private void updateAdminDetails() throws SQLException {
        System.out.println("🛠️ Updating your own profile...");
        studentService.updateStudent(Logged_In_username, Logged_In_username);
    }

    private void addStudentGrades() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Assignment ID: ");
        String course_id = scanner.nextLine();
        System.out.print("Enter Grade: ");
        String grade = scanner.nextLine();
        lecturerService.addGrade(studentId, course_id, grade);
    }

    private void deregisterStudentFromCourse() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Course ID to deregister: ");
        String courseId = scanner.nextLine();

        boolean success = studentService.deregisterStudentFromCourse(studentId, courseId);
        if (success) {
            System.out.println("✅ Student deregistered from course successfully.");

            // Check if student has any courses left
            boolean hasCourses = studentService.hasEnrolledCourses(studentId);
            if (!hasCourses) {
                // Block user since no courses remain
                adminService.blockUser(studentId);
                System.out.println("⚠️ Student has no more enrolled courses and is now blocked.");
            }
        } else {
            System.out.println("❌ Failed to deregister student from course.");
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
