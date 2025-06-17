package models;

/**
 * Department model for academic department information.
 */
public class Department {
    private int departmentId;
    private String departmentName;
    private String departmentHead;
    private String location;
    private double budget;
    private String phone;

    public Department(int departmentId, String departmentName, String departmentHead,
                      String location, double budget, String phone) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentHead = departmentHead;
        this.location = location;
        this.budget = budget;
        this.phone = phone;
    }

    // Getters and setters
    public int getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
    public String getDepartmentHead() { return departmentHead; }
    public String getLocation() { return location; }
    public double getBudget() { return budget; }
    public String getPhone() { return phone; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public void setDepartmentHead(String departmentHead) { this.departmentHead = departmentHead; }
    public void setLocation(String location) { this.location = location; }
    public void setBudget(double budget) { this.budget = budget; }
    public void setPhone(String phone) { this.phone = phone; }
}
