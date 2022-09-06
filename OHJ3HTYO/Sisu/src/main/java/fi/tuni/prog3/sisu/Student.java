package fi.tuni.prog3.sisu;

import java.util.HashMap;

/**
 * Student represents a student, or more specifically the user. Is used to store
 * user data.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class Student {
    private String name;
    private String number;
    private String degree;
    private int credits;
    private HashMap<String, Boolean> coursesDone = new HashMap<>();

    /**
     * Initialized a new Student.
     * 
     * @param name   student name.
     * @param number student number.
     */
    public Student(String name, String number) {
        this.name = name;
        this.number = number;
    }

    /**
     * @return The current student name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The current student number;
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return The current student degree;
     */
    public String getDegree() {
        return degree;
    }

    /**
     * @param newDegree degree (name) to set.
     */
    public void setDegree(String newDegree) {
        degree = newDegree;
        clearCoursesDone();
        credits = 0;
    }

    /**
     * @return The current amount of credits earned.
     */
    public int getCredits() {
        return credits;
    }

    public String getCreditsAsString() {
        return Integer.toString(this.credits);
    }

    /**
     * @param earnedCredits credits to be added to the total.
     */
    public void addCredits(int earnedCredits) {
        credits = credits + earnedCredits;
    }

    /**
     * @param lostCredits credits to be subtracted from the total.
     */
    public void subtractCredits(int lostCredits) {
        credits = credits - lostCredits;
    }

    /**
     * @return HashMap of the current students courses and their status (true =
     *         passed, false = not passed).
     */
    public HashMap<String, Boolean> getCoursesDone() {
        return coursesDone;
    }

    /**
     * @param course course to be added.
     * @param status added courses status.
     */
    public void addCoursesDone(String course, Boolean status) {
        this.coursesDone.put(course, status);

    }

    /**
     * Clears this students courses.
     */
    public void clearCoursesDone() {
        this.coursesDone.clear();
    }
}
