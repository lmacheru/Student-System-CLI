package cli;

import services.AuthenticationService;
import database.DBInitializer;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthenticationService authService = new AuthenticationService();
    public static String Logged_In_username ="";
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

        // Check if user exists before anything else
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
                    new StudentCLI(Logged_In_username, 1001).showStudentDashboard();
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

        String userId = generateUserId();
        System.out.println("Generated User ID (username): " + userId);

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        if (firstName.equalsIgnoreCase("cancel")) return;

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        if (lastName.equalsIgnoreCase("cancel")) return;

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
            System.out.print("User Type (admin/student): ");
            userType = scanner.nextLine().toLowerCase();
            if (userType.equalsIgnoreCase("cancel")) return;
            if (userType.equals("admin") || userType.equals("student")) break;
            System.out.println("\u274C Invalid user type. Please enter 'admin' or 'student'.");
        }

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
                userId, firstName, lastName, address, email,
                phone, dob, userType, password
        );

        if (success) {
            System.out.println("\u2705 Registration successful! You can now log in with User ID: " + userId);
        } else {
            System.out.println("\u274C Registration failed. Please try again.");
        }
    }

    private String generateUserId() {
        return UUID.randomUUID().toString().substring(0, 5).toUpperCase();
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
