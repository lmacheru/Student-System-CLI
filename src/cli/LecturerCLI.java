package cli;

import services.LecturerService;

import java.sql.SQLException;
import java.util.Scanner;

public class LecturerCLI {
    private final Scanner scanner = new Scanner(System.in);
    public final LecturerService lecturerService = new LecturerService();
    private final String lecturerId; // the logged-in lecturer's ID

    public LecturerCLI(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public void showMenu() throws SQLException {
        int choice;

        do {
            System.out.println("\n📚 Lecturer Dashboard, You Teach");
            System.out.println("1. View Enrolled Students to Course");
            System.out.println("2. View Assigned Courses");
            System.out.println("3. Update Lecturer Details");
            System.out.println("4. Add Grades to Assignment");
            System.out.println("5. Resign from Course");  // removed deregister option as per your request
            System.out.println("6. Back");

            System.out.print("Choose an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewEnrolledStudentsToCourse();
                    break;
                case 2:
                    viewAssignedCourses();
                    break;
                case 3:
                    updateLecturerDetails();
                    break;
                case 4:
                    addGradesToAssignment();
                    break;
                case 5:
                    resignFromCourse();
                    break;
                case 6:
                    System.out.println("🏠 Returning to main menu...");
                    break;
                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        } while (choice != 6);
    }

    private void viewEnrolledStudentsToCourse() throws SQLException {
        System.out.print("Enter Course ID to view enrolled students: ");
        String courseId = scanner.nextLine();
        lecturerService.viewEnrolledStudents(lecturerId, courseId);
    }

    private void viewAssignedCourses() throws SQLException {
        lecturerService.viewCoursesByLecturer(lecturerId);
    }

    private void updateLecturerDetails() throws SQLException {
        System.out.println("Update your details:");

        System.out.print("Enter new First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter new Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter new Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter new Phone number: ");
        String phone = scanner.nextLine();

        // Pass all required parameters to updateLecturer
        lecturerService.updateLecturer(lecturerId, firstName, lastName, email,phone);
    }

    private void addGradesToAssignment() throws SQLException {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Assignment ID: ");
        String course_id = scanner.nextLine();
        System.out.print("Enter Grade: ");
        String grade = scanner.nextLine();
        lecturerService.addGrade(studentId, course_id, grade);
    }

    private void resignFromCourse() throws SQLException {
        System.out.print("Enter Course ID to resign from: ");
        String courseId = scanner.nextLine();
        lecturerService.resignFromCourse(lecturerId, courseId);
    }
}
