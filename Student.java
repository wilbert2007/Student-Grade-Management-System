import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private String name;
    private int age;
    private String department;
    private double marks;
    private AttendanceList attendance;
    private List<String> enrolledCourses;

    public Student(int id, String name, int age, String department, double marks) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
        this.marks = marks;
        this.attendance = new AttendanceList();
        this.enrolledCourses = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public AttendanceList getAttendance() {
        return attendance;
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void enrollCourse(String courseCode) {
        if (!enrolledCourses.contains(courseCode)) {
            enrolledCourses.add(courseCode);
        }
    }

    public void dropCourse(String courseCode) {
        enrolledCourses.remove(courseCode);
    }

    @Override
    public String toString() {
        return String.format("[ID: %-4d | Name: %-15s | Age: %-2d | Dept: %-10s | Marks: %-5.2f | Attendance: %-3.0f%%]",
                id, name, age, department, marks, attendance.getAttendancePercentage());
    }
}
