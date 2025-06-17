package models;

/**
 * Student model holds student profile data.
 */
public class Student {
    private int studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String enrollmentDate;
    private String status;

    public Student(int studentId, String firstName, String lastName, String email,
                   String phone, String dateOfBirth, String enrollmentDate, String status) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    // Getters and setters
    public int getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getEnrollmentDate() { return enrollmentDate; }
    public String getStatus() { return status; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public void setStatus(String status) { this.status = status; }
}
