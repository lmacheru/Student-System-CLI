package models;

/**
 * Instructor model stores faculty data.
 */
public class Instructor {
    private int instructorId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private String hireDate;
    private double salary;

    public Instructor(int instructorId, String firstName, String lastName, String email,
                      String phone, String department, String hireDate, double salary) {
        this.instructorId = instructorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    // Getters and setters
    public int getInstructorId() { return instructorId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDepartment() { return department; }
    public String getHireDate() { return hireDate; }
    public double getSalary() { return salary; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDepartment(String department) { this.department = department; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }
    public void setSalary(double salary) { this.salary = salary; }
}
