package cli;

import services.AuthenticationService;
import database.DBInitializer;

import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthenticationService authService = new AuthenticationService();
    public static String Logged_In_username = "";

    public void start() throws SQLException {
        DBInitializer.initializeDatabase();

        System.out.println("\uD83C\uDFEB Welcome to the Student Management System");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegistration();
                    break;
                case "3":
                    System.out.println("\uD83D\uDC4B Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("\u274C Invalid choice. Please try again.");
            }
        }
    }

    private void handleLogin() throws SQLException {
        System.out.println("\uD83D\uDD10 Login");
        System.out.println("Type 'cancel' at any time to return to the main menu.\n");

        System.out.print("Enter username (User ID): ");
        Logged_In_username = scanner.nextLine();
        if (Logged_In_username.equalsIgnoreCase("cancel")) return;

        if (!authService.userExists(Logged_In_username)) {
            System.out.println("\u274C No such user found. Please check the username or register first.");
            return;
        }

        if (authService.isBlocked(Logged_In_username)) {
            System.out.println("\u26A0 Your account is blocked. Please contact admin.");
            return;
        }

        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if (password.equalsIgnoreCase("cancel")) return;

            if (authService.validateLogin(Logged_In_username, password)) {
                String userType = authService.getUserType(Logged_In_username);
                if ("admin".equalsIgnoreCase(userType)) {
                    new AdminCLI().showMenu();
                } else if ("student".equalsIgnoreCase(userType)) {
                    new StudentCLI(Logged_In_username).showStudentDashboard();
                } else if ("lecturer".equalsIgnoreCase(userType)) {
                    new LecturerCLI(Logged_In_username).showMenu();
                } else {
                    System.out.println("\u274C Unknown user type.");
                }
                return;
            } else {
                attempts++;
                System.out.println("\u274C Incorrect password. Attempts left: " + (3 - attempts));
            }
        }

        System.out.println("\u26A0 Too many failed attempts. Your account is now blocked.");
        authService.blockUser(Logged_In_username);
    }

    private void handleRegistration() {
        System.out.println("\uD83D\uDCCB Register a new user");
        System.out.println("Type 'cancel' at any time to return to the main menu.\n");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        if (firstName.equalsIgnoreCase("cancel")) return;

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        if (lastName.equalsIgnoreCase("cancel")) return;

        String idNumber;
        while (true) {
            System.out.print("ID Number (unique national ID): ");
            idNumber = scanner.nextLine();
            if (idNumber.equalsIgnoreCase("cancel")) return;
            if (authService.idNumberExists(idNumber)) {
                System.out.println("\u274C ID Number already registered. Cannot register duplicate.");
            } else {
                break;
            }
        }
        System.out.print("Address: ");
        String address = scanner.nextLine();
        if (address.equalsIgnoreCase("cancel")) return;

        String email;
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (email.equalsIgnoreCase("cancel")) return;
            if (isValidEmail(email)) break;
            System.out.println("\u274C Invalid email format. Please try again.");
        }

        String phone;
        while (true) {
            System.out.print("Phone: ");
            phone = scanner.nextLine();
            if (phone.equalsIgnoreCase("cancel")) return;
            if (isValidPhone(phone)) break;
            System.out.println("\u274C Invalid phone number. Use digits only (min 10). Please try again.");
        }

        String dob;
        while (true) {
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            dob = scanner.nextLine();
            if (dob.equalsIgnoreCase("cancel")) return;
            if (isValidDate(dob)) break;
            System.out.println("\u274C Invalid date format. Use YYYY-MM-DD. Please try again.");
        }



        String userType;
        while (true) {
            System.out.print("User Type (admin/student/lecturer): ");
            userType = scanner.nextLine().toLowerCase();
            if (userType.equalsIgnoreCase("cancel")) return;
            if ("admin".equals(userType) || "student".equals(userType) || "lecturer".equals(userType)) break;
            System.out.println("\u274C Invalid user type. Please enter 'admin', 'student' or 'lecturer'.");
        }

        String rawId = generateUserId();
        String userId;
        if ("admin".equals(userType)) {
            userId = "Adm" + rawId;
        } else if ("student".equals(userType)) {
            userId = "Stu" + rawId;
        } else {
            userId = "Lec" + rawId;
        }

        System.out.println("Generated User ID (username): " + userId);

        String password;
        while (true) {
            System.out.print("Password (min 6 chars): ");
            password = scanner.nextLine();
            if (password.equalsIgnoreCase("cancel")) return;
            if (password.length() >= 6) break;
            System.out.println("\u274C Password too short. Please try again.");
        }

        if (authService.userExists(userId)) {
            System.out.println("\u274C That user ID is already registered. Try again.");
            return;
        }

        boolean success = authService.register(
                userId, firstName, lastName, idNumber, address, email,
                phone, dob, userType, password
        );

        if (success) {
            System.out.println("\u2705 Registration successful! You can now log in with User ID: " + userId);
        } else {
            System.out.println("\u274C Registration failed. Please try again.");
        }
    }

    private String generateUserId() {
        Random random = new Random();
        int number = 100000000 + random.nextInt(900000000); // 9-digit number
        return String.valueOf(number);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10,}");
    }

    private boolean isValidDate(String date) {
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }
}
