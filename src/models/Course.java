package models;

/**
 * Course model stores course information.
 */
public class Course {
    private int courseId;
    private String courseCode;
    private String courseName;
    private String description;
    private int credits;
    private String department;
    private int instructorId;
    private String semester;

    public Course(int courseId, String courseCode, String courseName, String description,
                  int credits, String department, int instructorId, String semester) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.department = department;
        this.instructorId = instructorId;
        this.semester = semester;
    }

    // Getters and setters
    public int getCourseId() { return courseId; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getDescription() { return description; }
    public int getCredits() { return credits; }
    public String getDepartment() { return department; }
    public int getInstructorId() { return instructorId; }
    public String getSemester() { return semester; }

    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setDescription(String description) { this.description = description; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setDepartment(String department) { this.department = department; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    public void setSemester(String semester) { this.semester = semester; }
}
