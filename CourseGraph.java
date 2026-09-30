import java.util.*;

public class CourseGraph {

    public static class Course {
        private String code;
        private String title;
        private int credits;

        public Course(String code, String title, int credits) {
            this.code = code.toUpperCase();
            this.title = title;
            this.credits = credits;
        }

        public String getCode() { return code; }
        public String getTitle() { return title; }
        public int getCredits() { return credits; }

        @Override
        public String toString() {
            return String.format("[%s: %s (%d Credits)]", code, title, credits);
        }
    }

    private Map<String, Course> courses;
    // Adjacency list: CourseCode -> List of Prerequisite CourseCodes
    private Map<String, List<String>> prerequisites;
    // CourseCode -> List of Enrolled Student IDs
    private Map<String, List<Integer>> enrolledStudents;

    public CourseGraph() {
        this.courses = new HashMap<>();
        this.prerequisites = new HashMap<>();
        this.enrolledStudents = new HashMap<>();
    }

    public void addCourse(String code, String title, int credits) {
        String key = code.toUpperCase();
        courses.put(key, new Course(code, title, credits));
        prerequisites.putIfAbsent(key, new ArrayList<>());
        enrolledStudents.putIfAbsent(key, new ArrayList<>());
    }

    public void addPrerequisite(String courseCode, String prereqCode) {
        String courseKey = courseCode.toUpperCase();
        String prereqKey = prereqCode.toUpperCase();

        if (!courses.containsKey(courseKey) || !courses.containsKey(prereqKey)) {
            System.out.println("Error: Both courses must exist before adding prerequisite!");
            return;
        }

        List<String> prereqs = prerequisites.get(courseKey);
        if (!prereqs.contains(prereqKey)) {
            prereqs.add(prereqKey);
            System.out.println("Prerequisite added: " + prereqKey + " -> " + courseKey);
        }
    }

    public boolean enrollStudent(String courseCode, int studentId) {
        String key = courseCode.toUpperCase();
        if (!courses.containsKey(key)) {
            System.out.println("Error: Course not found!");
            return false;
        }

        List<Integer> students = enrolledStudents.get(key);
        if (students.contains(studentId)) {
            System.out.println("Student is already enrolled in " + key);
            return false;
        }
        students.add(studentId);
        return true;
    }

    public void displayCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        System.out.println("\n========== COURSE CATALOG (GRAPH) ==========");
        for (Course course : courses.values()) {
            List<String> prereqs = prerequisites.get(course.getCode());
            List<Integer> enrolled = enrolledStudents.get(course.getCode());
            System.out.printf("%-10s | %-25s | Credits: %d | Prereqs: %s | Enrolled: %d students\n",
                    course.getCode(),
                    course.getTitle(),
                    course.getCredits(),
                    (prereqs.isEmpty() ? "None" : prereqs.toString()),
                    enrolled.size());
        }
    }

    public Course getCourse(String code) {
        return courses.get(code.toUpperCase());
    }

    public List<String> getPrerequisites(String code) {
        return prerequisites.getOrDefault(code.toUpperCase(), new ArrayList<>());
    }
}
