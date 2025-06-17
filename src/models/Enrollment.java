package models;

/**
 * Enrollment links students and courses.
 */
public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int courseId;
    private String enrollmentDate;
    private String grade;
    private String status;

    public Enrollment(int enrollmentId, int studentId, int courseId,
                      String enrollmentDate, String grade, String status) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.grade = grade;
        this.status = status;
    }

    // Getters and setters
    public int getEnrollmentId() { return enrollmentId; }
    public int getStudentId() { return studentId; }
    public int getCourseId() { return courseId; }
    public String getEnrollmentDate() { return enrollmentDate; }
    public String getGrade() { return grade; }
    public String getStatus() { return status; }

    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setStatus(String status) { this.status = status; }
}
