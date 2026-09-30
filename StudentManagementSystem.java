import java.util.*;

public class StudentManagementSystem {

    // 1. Hash Table for O(1) Student ID lookup
    private final StudentHashTable idTable;

    // 2. AVL Tree for maintaining sorted student ranking by marks/grades
    private final StudentAVLTree avlTree;

    // 3. Trie for fast name & prefix search
    private final StudentTrie trie;

    // 4. Graph for Course Management & Prerequisites
    private final CourseGraph courseGraph;

    // 5. Stack for Undo operations
    private final UndoStack undoStack;

    // 6. Queue for Pending Tasks / Admission applications
    private final Queue<String> pendingTasks;

    public StudentManagementSystem() {
        this.idTable = new StudentHashTable();
        this.avlTree = new StudentAVLTree();
        this.trie = new StudentTrie();
        this.courseGraph = new CourseGraph();
        this.undoStack = new UndoStack();
        this.pendingTasks = new LinkedList<>();

        // Initialize with default sample courses
        initializeDefaultCourses();
        // Initialize with sample student data
        initializeSampleStudents();
    }

    private void initializeDefaultCourses() {
        courseGraph.addCourse("CS101", "Intro to Programming", 4);
        courseGraph.addCourse("CS201", "Data Structures & Algorithms", 4);
        courseGraph.addCourse("CS301", "Database Systems", 3);
        courseGraph.addCourse("CS401", "Artificial Intelligence", 4);

        courseGraph.addPrerequisite("CS201", "CS101");
        courseGraph.addPrerequisite("CS301", "CS201");
        courseGraph.addPrerequisite("CS401", "CS201");
    }

    private void initializeSampleStudents() {
        addStudentInternal(new Student(101, "Alice Smith", 20, "Computer Science", 94.5), false);
        addStudentInternal(new Student(102, "Bob Johnson", 21, "Information Tech", 82.0), false);
        addStudentInternal(new Student(103, "Charlie Brown", 19, "Computer Science", 76.5), false);
        addStudentInternal(new Student(104, "David Miller", 22, "Electronics", 89.0), false);
        addStudentInternal(new Student(105, "Emma Watson", 20, "Computer Science", 98.0), false);

        // Add sample attendance
        Student s1 = idTable.get(101);
        if (s1 != null) {
            s1.getAttendance().markAttendance("2026-09-01", true);
            s1.getAttendance().markAttendance("2026-09-02", true);
            s1.getAttendance().markAttendance("2026-09-03", false);
            s1.enrollCourse("CS101");
            s1.enrollCourse("CS201");
        }
    }

    // Convert numeric marks into Letter Grade and GPA
    public static String calculateGrade(double marks) {
        if (marks >= 90) return "A+ (4.0 GPA)";
        if (marks >= 80) return "A  (3.7 GPA)";
        if (marks >= 70) return "B  (3.0 GPA)";
        if (marks >= 60) return "C  (2.0 GPA)";
        if (marks >= 50) return "D  (1.0 GPA)";
        return "F  (0.0 GPA)";
    }

    // 1. ADD STUDENT
    public boolean addStudent(int id, String name, int age, String department, double marks) {
        if (idTable.containsKey(id)) {
            System.out.println("❌ Error: Student with ID " + id + " already exists!");
            return false;
        }

        Student student = new Student(id, name, age, department, marks);
        addStudentInternal(student, true);
        System.out.println("✅ Student added successfully: " + student);
        return true;
    }

    private void addStudentInternal(Student student, boolean recordUndo) {
        idTable.put(student.getId(), student);
        avlTree.insert(student);
        trie.insert(student);

        if (recordUndo) {
            undoStack.push(new UndoStack.Action(UndoStack.ActionType.ADD_STUDENT, student));
        }
    }

    // 2. REMOVE STUDENT
    public boolean removeStudent(int id) {
        Student student = idTable.get(id);
        if (student == null) {
            System.out.println("❌ Error: Student ID " + id + " not found!");
            return false;
        }

        idTable.remove(id);
        avlTree.delete(student);
        trie.remove(student);

        undoStack.push(new UndoStack.Action(UndoStack.ActionType.REMOVE_STUDENT, student));
        System.out.println("✅ Removed student: " + student.getName() + " (ID: " + id + ")");
        return true;
    }

    // 3. SEARCH STUDENT BY ID (Hash Table - O(1))
    public Student searchStudentById(int id) {
        Student student = idTable.get(id);
        if (student != null) {
            System.out.println("✅ Found Student [Hash Table Lookup]:");
            System.out.println(student);
            System.out.println("   Grade: " + calculateGrade(student.getMarks()));
            System.out.println("   Courses: " + student.getEnrolledCourses());
        } else {
            System.out.println("❌ Student with ID " + id + " not found.");
        }
        return student;
    }

    // 4. DISPLAY ALL STUDENTS
    public void displayAllStudents() {
        List<Student> students = idTable.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n=========================== ALL REGISTERED STUDENTS ===========================");
        for (Student s : students) {
            System.out.printf("%s | Grade: %-13s\n", s.toString(), calculateGrade(s.getMarks()));
        }
        System.out.println("Total Students: " + students.size());
    }

    // 5. UPDATE MARKS / GRADES
    public boolean updateMarks(int id, double newMarks) {
        Student student = idTable.get(id);
        if (student == null) {
            System.out.println("❌ Error: Student ID " + id + " not found!");
            return false;
        }

        double oldMarks = student.getMarks();
        // Remove from AVL Tree with old marks, update marks, reinsert
        avlTree.delete(student);

        student.setMarks(newMarks);
        avlTree.insert(student);

        undoStack.push(new UndoStack.Action(UndoStack.ActionType.UPDATE_MARKS, student, oldMarks, newMarks));
        System.out.printf("✅ Updated ID %d (%s) Marks: %.2f -> %.2f [Grade: %s]\n",
                id, student.getName(), oldMarks, newMarks, calculateGrade(newMarks));
        return true;
    }

    // 6. STUDENT RANKING (AVL Tree Traversal - Descending Order)
    public void displayStudentRanking() {
        List<Student> ranked = avlTree.getRankedStudents();
        if (ranked.isEmpty()) {
            System.out.println("No students available for ranking.");
            return;
        }

        System.out.println("\n======================== STUDENT RANKING (AVL TREE) ========================");
        int rank = 1;
        for (Student s : ranked) {
            System.out.printf("Rank #%-2d | ID: %-4d | %-16s | Marks: %-5.2f | Grade: %-13s\n",
                    rank++, s.getId(), s.getName(), s.getMarks(), calculateGrade(s.getMarks()));
        }
    }

    // 7. TOP STUDENTS (Max Heap)
    public void displayTopStudents(int k) {
        List<Student> all = idTable.getAllStudents();
        if (all.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        StudentMaxHeap maxHeap = StudentMaxHeap.buildHeap(all);
        List<Student> topStudents = maxHeap.getTopK(k);

        System.out.printf("\n======================== TOP %d STUDENTS (MAX HEAP) ========================\n", k);
        int pos = 1;
        for (Student s : topStudents) {
            System.out.printf("Top #%-2d | %s | Grade: %s\n", pos++, s.toString(), calculateGrade(s.getMarks()));
        }
    }

    // 8. SEARCH STUDENT BY NAME (Trie Prefix Matching)
    public void searchStudentByName(String prefix) {
        List<Student> matches = trie.searchByPrefix(prefix);
        System.out.println("\n==================== NAME SEARCH RESULTS (TRIE) ====================");
        System.out.println("Query: \"" + prefix + "\"");
        if (matches.isEmpty()) {
            System.out.println("No matching students found.");
        } else {
            System.out.println("Matches found: " + matches.size());
            for (Student s : matches) {
                System.out.println("-> " + s.toString() + " | Grade: " + calculateGrade(s.getMarks()));
            }
        }
    }

    // 9. COURSE MANAGEMENT (Graph)
    public CourseGraph getCourseGraph() {
        return courseGraph;
    }

    public void enrollStudentInCourse(int studentId, String courseCode) {
        Student s = idTable.get(studentId);
        if (s == null) {
            System.out.println("❌ Student not found!");
            return;
        }
        if (courseGraph.enrollStudent(courseCode, studentId)) {
            s.enrollCourse(courseCode.toUpperCase());
            System.out.println("✅ Student " + s.getName() + " enrolled into course " + courseCode.toUpperCase());
        }
    }

    // 10. ATTENDANCE MANAGEMENT (Linked List)
    public void markStudentAttendance(int studentId, String date, boolean isPresent) {
        Student s = idTable.get(studentId);
        if (s == null) {
            System.out.println("❌ Student not found!");
            return;
        }
        s.getAttendance().markAttendance(date, isPresent);
        System.out.printf("✅ Attendance recorded for %s on %s: %s\n",
                s.getName(), date, (isPresent ? "PRESENT" : "ABSENT"));
    }

    public void viewStudentAttendance(int studentId) {
        Student s = idTable.get(studentId);
        if (s == null) {
            System.out.println("❌ Student not found!");
            return;
        }
        System.out.println("\n--- Attendance for " + s.getName() + " (ID: " + studentId + ") ---");
        s.getAttendance().displayAttendance();
    }

    // 11. UNDO LAST OPERATION (Stack)
    public boolean undoLastOperation() {
        if (undoStack.isEmpty()) {
            System.out.println("⚠️ Nothing to undo. Undo stack is empty.");
            return false;
        }

        UndoStack.Action action = undoStack.pop();
        switch (action.type) {
            case ADD_STUDENT:
                idTable.remove(action.student.getId());
                avlTree.delete(action.student);
                trie.remove(action.student);
                System.out.println("⏪ Undone ADD: Removed student " + action.student.getName());
                break;

            case REMOVE_STUDENT:
                addStudentInternal(action.student, false);
                System.out.println("⏪ Undone REMOVE: Restored student " + action.student.getName());
                break;

            case UPDATE_MARKS:
                avlTree.delete(action.student);
                action.student.setMarks(action.oldMarks);
                avlTree.insert(action.student);
                System.out.printf("⏪ Undone UPDATE: Restored marks of %s to %.2f\n",
                        action.student.getName(), action.oldMarks);
                break;
        }
        return true;
    }

    // 12. GENERATE REPORT
    public void generateReport() {
        List<Student> students = idTable.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No data available to generate report.");
            return;
        }

        double totalMarks = 0;
        double highestMarks = Double.MIN_VALUE;
        double lowestMarks = Double.MAX_VALUE;
        Student topStudent = null;

        for (Student s : students) {
            totalMarks += s.getMarks();
            if (s.getMarks() > highestMarks) {
                highestMarks = s.getMarks();
                topStudent = s;
            }
            if (s.getMarks() < lowestMarks) {
                lowestMarks = s.getMarks();
            }
        }

        double avgMarks = totalMarks / students.size();

        System.out.println("\n======================== SYSTEM ANALYTICS & REPORT ========================");
        System.out.println(" Total Enrolled Students : " + students.size());
        System.out.printf(" Class Average Marks    : %.2f (%s)\n", avgMarks, calculateGrade(avgMarks));
        System.out.printf(" Highest Score           : %.2f (By: %s)\n", highestMarks, (topStudent != null ? topStudent.getName() : "N/A"));
        System.out.printf(" Lowest Score            : %.2f\n", lowestMarks);
        System.out.println(" Pending Task Queue Size : " + pendingTasks.size());
        System.out.println(" Undo Stack History Size : " + undoStack.size());
        System.out.println("==========================================================================");
    }

    // Pending Tasks Queue Helpers
    public void addPendingTask(String task) {
        pendingTasks.offer(task);
        System.out.println("📥 Task enqueued: " + task);
    }

    public void processNextPendingTask() {
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks in queue.");
            return;
        }
        String task = pendingTasks.poll();
        System.out.println("⚙️ Processed pending task: " + task);
    }

    public void displayPendingTasks() {
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks in queue.");
            return;
        }
        System.out.println("\n--- Pending Tasks (Queue FIFO) ---");
        int index = 1;
        for (String task : pendingTasks) {
            System.out.println(index++ + ". " + task);
        }
    }
}
