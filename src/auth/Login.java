package auth;

public class Login {
    private String username;
    private String password;
    private String role; // "admin" or "student"

    public Login(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public boolean isStudent() {
        return "student".equalsIgnoreCase(role);
    }
}
