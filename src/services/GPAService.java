package services;

import models.Enrollment;
import java.util.List;

/**
 * GPA calculation based on enrolled course grades.
 */
public class GPAService {

    public double calculateGPA(List<Enrollment> enrollments) {
        double totalPoints = 0;
        int totalCourses = 0;

        for (Enrollment e : enrollments) {
            if (e.getGrade() == null) continue;
            double points = getPoints(e.getGrade());
            if (points >= 0) {
                totalPoints += points;
                totalCourses++;
            }
        }
        return totalCourses == 0 ? 0.0 : totalPoints / totalCourses;
    }

    private double getPoints(String grade) {
        if (grade == null) {
            return -1.0;
        }
        switch (grade.toUpperCase()) {
            case "A":
                return 4.0;
            case "B":
                return 3.0;
            case "C":
                return 2.0;
            case "D":
                return 1.0;
            case "F":
                return 0.0;
            default:
                return -1.0;
        }
    }

}
