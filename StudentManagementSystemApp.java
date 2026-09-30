import java.util.*;

// ==========================================
// 1. STUDENT MODEL
// ==========================================
class StudentRecord {
    private int id;
    private String name;
    private int age;
    private String department;
    private double marks;
    private AttendanceLinkedList attendance;
    private List<String> enrolledCourses;

    public StudentRecord(int id, String name, int age, String department, double marks) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
        this.marks = marks;
        this.attendance = new AttendanceLinkedList();
        this.enrolledCourses = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }
    public AttendanceLinkedList getAttendance() { return attendance; }
    public List<String> getEnrolledCourses() { return enrolledCourses; }

    public void enrollCourse(String code) {
        if (!enrolledCourses.contains(code)) enrolledCourses.add(code);
    }

    @Override
    public String toString() {
        return String.format("[ID: %-4d | Name: %-14s | Age: %-2d | Dept: %-12s | Marks: %-5.2f | Att: %-3.0f%%]",
                id, name, age, department, marks, attendance.getAttendancePercentage());
    }
}

// ==========================================
// 2. ATTENDANCE (LINKED LIST)
// ==========================================
class AttendanceLinkedList {
    static class Node {
        String date;
        boolean isPresent;
        Node next;

        Node(String date, boolean isPresent) {
            this.date = date;
            this.isPresent = isPresent;
            this.next = null;
        }
    }

    private Node head;
    private int total = 0;
    private int attended = 0;

    public void markAttendance(String date, boolean isPresent) {
        Node newNode = new Node(date, isPresent);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        total++;
        if (isPresent) attended++;
    }

    public double getAttendancePercentage() {
        return (total == 0) ? 100.0 : ((double) attended / total) * 100.0;
    }

    public void display() {
        if (head == null) {
            System.out.println("No attendance records found.");
            return;
        }
        Node temp = head;
        while (temp != null) {
            System.out.println("Date: " + temp.date + " -> " + (temp.isPresent ? "PRESENT" : "ABSENT"));
            temp = temp.next;
        }
        System.out.printf("Total: %d/%d classes attended (%.1f%%)\n", attended, total, getAttendancePercentage());
    }
}

// ==========================================
// 3. STUDENT ID LOOKUP (HASH TABLE)
// ==========================================
class CustomHashTable {
    static class HashNode {
        int key;
        StudentRecord value;
        HashNode next;

        HashNode(int key, StudentRecord value) {
            this.key = key;
            this.value = value;
        }
    }

    private HashNode[] buckets;
    private int capacity;
    private int size;

    public CustomHashTable() {
        this.capacity = 16;
        this.buckets = new HashNode[capacity];
        this.size = 0;
    }

    private int hash(int key) {
        return Math.abs(key) % capacity;
    }

    public void put(int key, StudentRecord value) {
        int idx = hash(key);
        HashNode curr = buckets[idx];
        while (curr != null) {
            if (curr.key == key) {
                curr.value = value;
                return;
            }
            curr = curr.next;
        }
        HashNode node = new HashNode(key, value);
        node.next = buckets[idx];
        buckets[idx] = node;
        size++;
    }

    public StudentRecord get(int key) {
        int idx = hash(key);
        HashNode curr = buckets[idx];
        while (curr != null) {
            if (curr.key == key) return curr.value;
            curr = curr.next;
        }
        return null;
    }

    public StudentRecord remove(int key) {
        int idx = hash(key);
        HashNode curr = buckets[idx];
        HashNode prev = null;
        while (curr != null) {
            if (curr.key == key) {
                if (prev != null) prev.next = curr.next;
                else buckets[idx] = curr.next;
                size--;
                return curr.value;
            }
            prev = curr;
            curr = curr.next;
        }
        return null;
    }

    public List<StudentRecord> getAll() {
        List<StudentRecord> list = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            HashNode curr = buckets[i];
            while (curr != null) {
                list.add(curr.value);
                curr = curr.next;
            }
        }
        return list;
    }

    public int size() { return size; }
}

// ==========================================
// 4. STUDENT RANKING (AVL TREE)
// ==========================================
class CustomAVLTree {
    static class Node {
        StudentRecord student;
        int height;
        Node left, right;

        Node(StudentRecord student) {
            this.student = student;
            this.height = 1;
        }
    }

    private Node root;

    private int height(Node n) { return n == null ? 0 : n.height; }
    private int balance(Node n) { return n == null ? 0 : height(n.left) - height(n.right); }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node t = x.right;
        x.right = y;
        y.left = t;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node t = y.left;
        y.left = x;
        x.right = t;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    private int compare(StudentRecord a, StudentRecord b) {
        if (Double.compare(a.getMarks(), b.getMarks()) != 0) {
            return Double.compare(a.getMarks(), b.getMarks());
        }
        return Integer.compare(a.getId(), b.getId());
    }

    public void insert(StudentRecord s) { root = insert(root, s); }

    private Node insert(Node node, StudentRecord s) {
        if (node == null) return new Node(s);
        int cmp = compare(s, node.student);
        if (cmp < 0) node.left = insert(node.left, s);
        else if (cmp > 0) node.right = insert(node.right, s);
        else return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int bal = balance(node);

        if (bal > 1 && compare(s, node.left.student) < 0) return rightRotate(node);
        if (bal < -1 && compare(s, node.right.student) > 0) return leftRotate(node);
        if (bal > 1 && compare(s, node.left.student) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (bal < -1 && compare(s, node.right.student) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    public void delete(StudentRecord s) { root = delete(root, s); }

    private Node minValueNode(Node node) {
        Node curr = node;
        while (curr.left != null) curr = curr.left;
        return curr;
    }

    private Node delete(Node node, StudentRecord s) {
        if (node == null) return null;
        int cmp = compare(s, node.student);
        if (cmp < 0) node.left = delete(node.left, s);
        else if (cmp > 0) node.right = delete(node.right, s);
        else {
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node temp = minValueNode(node.right);
                node.student = temp.student;
                node.right = delete(node.right, temp.student);
            }
        }
        if (node == null) return null;

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        int bal = balance(node);

        if (bal > 1 && balance(node.left) >= 0) return rightRotate(node);
        if (bal > 1 && balance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (bal < -1 && balance(node.right) <= 0) return leftRotate(node);
        if (bal < -1 && balance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    public List<StudentRecord> getRanked() {
        List<StudentRecord> list = new ArrayList<>();
        inOrderDesc(root, list);
        return list;
    }

    private void inOrderDesc(Node node, List<StudentRecord> list) {
        if (node != null) {
            inOrderDesc(node.right, list);
            list.add(node.student);
            inOrderDesc(node.left, list);
        }
    }
}

// ==========================================
// 5. TOP STUDENTS (MAX HEAP)
// ==========================================
class CustomMaxHeap {
    private StudentRecord[] heap;
    private int size;

    public CustomMaxHeap(int cap) {
        heap = new StudentRecord[cap];
        size = 0;
    }

    public void insert(StudentRecord s) {
        heap[size] = s;
        int curr = size++;
        while (curr > 0 && heap[curr].getMarks() > heap[(curr - 1) / 2].getMarks()) {
            StudentRecord tmp = heap[curr];
            heap[curr] = heap[(curr - 1) / 2];
            heap[(curr - 1) / 2] = tmp;
            curr = (curr - 1) / 2;
        }
    }

    public StudentRecord extractMax() {
        if (size == 0) return null;
        StudentRecord max = heap[0];
        heap[0] = heap[--size];
        heapify(0);
        return max;
    }

    private void heapify(int i) {
        int l = 2 * i + 1, r = 2 * i + 2, max = i;
        if (l < size && heap[l].getMarks() > heap[max].getMarks()) max = l;
        if (r < size && heap[r].getMarks() > heap[max].getMarks()) max = r;
        if (max != i) {
            StudentRecord tmp = heap[i];
            heap[i] = heap[max];
            heap[max] = tmp;
            heapify(max);
        }
    }

    public static List<StudentRecord> getTopK(List<StudentRecord> students, int k) {
        CustomMaxHeap h = new CustomMaxHeap(students.size() + 5);
        for (StudentRecord s : students) h.insert(s);
        List<StudentRecord> res = new ArrayList<>();
        int limit = Math.min(k, students.size());
        for (int i = 0; i < limit; i++) res.add(h.extractMax());
        return res;
    }
}

// ==========================================
// 6. NAME SEARCH (TRIE)
// ==========================================
class CustomTrie {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<StudentRecord> students = new ArrayList<>();
    }

    private final TrieNode root = new TrieNode();

    public void insert(StudentRecord s) {
        TrieNode curr = root;
        String name = s.getName().toLowerCase();
        for (char c : name.toCharArray()) {
            curr.children.putIfAbsent(c, new TrieNode());
            curr = curr.children.get(c);
            if (!curr.students.contains(s)) curr.students.add(s);
        }
    }

    public void remove(StudentRecord s) {
        TrieNode curr = root;
        String name = s.getName().toLowerCase();
        for (char c : name.toCharArray()) {
            if (!curr.children.containsKey(c)) return;
            curr = curr.children.get(c);
            curr.students.remove(s);
        }
    }

    public List<StudentRecord> searchPrefix(String prefix) {
        TrieNode curr = root;
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!curr.children.containsKey(c)) return Collections.emptyList();
            curr = curr.children.get(c);
        }
        return curr.students;
    }
}

// ==========================================
// 7. COURSE PREREQUISITES (GRAPH)
// ==========================================
class CustomCourseGraph {
    private Map<String, String> courses = new LinkedHashMap<>();
    private Map<String, List<String>> prereqs = new HashMap<>();
    private Map<String, List<Integer>> enrollments = new HashMap<>();

    public void addCourse(String code, String title) {
        courses.put(code, title);
        prereqs.putIfAbsent(code, new ArrayList<>());
        enrollments.putIfAbsent(code, new ArrayList<>());
    }

    public void addPrerequisite(String code, String prereq) {
        if (courses.containsKey(code) && courses.containsKey(prereq)) {
            prereqs.get(code).add(prereq);
        }
    }

    public boolean enroll(String code, int sId) {
        if (!courses.containsKey(code)) return false;
        List<Integer> list = enrollments.get(code);
        if (!list.contains(sId)) {
            list.add(sId);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\n========== COURSE CATALOG (GRAPH) ==========");
        for (var entry : courses.entrySet()) {
            System.out.printf("%-8s: %-25s | Prereqs: %s | Enrolled: %d\n",
                    entry.getKey(), entry.getValue(), prereqs.get(entry.getKey()), enrollments.get(entry.getKey()).size());
        }
    }
}

// ==========================================
// 8. UNDO STACK
// ==========================================
class CustomUndoStack {
    enum Type { ADD, REMOVE, UPDATE }

    static class Action {
        Type type;
        StudentRecord student;
        double oldMarks;

        Action(Type type, StudentRecord student) {
            this.type = type;
            this.student = student;
        }

        Action(Type type, StudentRecord student, double oldMarks) {
            this.type = type;
            this.student = student;
            this.oldMarks = oldMarks;
        }
    }

    private final Deque<Action> stack = new ArrayDeque<>();

    public void push(Action a) { stack.push(a); }
    public Action pop() { return stack.isEmpty() ? null : stack.pop(); }
    public boolean isEmpty() { return stack.isEmpty(); }
    public int size() { return stack.size(); }
}

// ==========================================
// 9. MAIN INTERACTIVE APPLICATION
// ==========================================
public class StudentManagementSystemApp {

    private final CustomHashTable idTable = new CustomHashTable();
    private final CustomAVLTree avlTree = new CustomAVLTree();
    private final CustomTrie trie = new CustomTrie();
    private final CustomCourseGraph courseGraph = new CustomCourseGraph();
    private final CustomUndoStack undoStack = new CustomUndoStack();
    private final Queue<String> pendingTasks = new LinkedList<>();

    public StudentManagementSystemApp() {
        // Sample Courses
        courseGraph.addCourse("CS101", "Intro to Programming");
        courseGraph.addCourse("CS201", "Data Structures");
        courseGraph.addCourse("CS301", "Algorithms & AI");
        courseGraph.addPrerequisite("CS201", "CS101");
        courseGraph.addPrerequisite("CS301", "CS201");

        // Sample Students
        addStudentInternal(new StudentRecord(101, "Alice Smith", 20, "CS", 94.5), false);
        addStudentInternal(new StudentRecord(102, "Bob Johnson", 21, "IT", 82.0), false);
        addStudentInternal(new StudentRecord(103, "Charlie Brown", 19, "CS", 76.5), false);
        addStudentInternal(new StudentRecord(104, "David Miller", 22, "ECE", 89.0), false);
        addStudentInternal(new StudentRecord(105, "Emma Watson", 20, "CS", 98.0), false);
    }

    public static String gradeOf(double m) {
        if (m >= 90) return "A+ (4.0 GPA)";
        if (m >= 80) return "A  (3.7 GPA)";
        if (m >= 70) return "B  (3.0 GPA)";
        if (m >= 60) return "C  (2.0 GPA)";
        if (m >= 50) return "D  (1.0 GPA)";
        return "F  (0.0 GPA)";
    }

    private void addStudentInternal(StudentRecord s, boolean recordUndo) {
        idTable.put(s.getId(), s);
        avlTree.insert(s);
        trie.insert(s);
        if (recordUndo) undoStack.push(new CustomUndoStack.Action(CustomUndoStack.Type.ADD, s));
    }

    public void addStudent(int id, String name, int age, String dept, double marks) {
        if (idTable.get(id) != null) {
            System.out.println("❌ Student ID " + id + " already exists!");
            return;
        }
        StudentRecord s = new StudentRecord(id, name, age, dept, marks);
        addStudentInternal(s, true);
        System.out.println("✅ Student Added: " + s);
    }

    public void removeStudent(int id) {
        StudentRecord s = idTable.remove(id);
        if (s == null) {
            System.out.println("❌ Student not found.");
            return;
        }
        avlTree.delete(s);
        trie.remove(s);
        undoStack.push(new CustomUndoStack.Action(CustomUndoStack.Type.REMOVE, s));
        System.out.println("✅ Student removed: " + s.getName());
    }

    public void searchStudent(int id) {
        StudentRecord s = idTable.get(id);
        if (s != null) {
            System.out.println("✅ Found: " + s + " | Grade: " + gradeOf(s.getMarks()));
        } else {
            System.out.println("❌ Student not found.");
        }
    }

    public void displayAll() {
        List<StudentRecord> all = idTable.getAll();
        System.out.println("\n=========================== ALL REGISTERED STUDENTS ===========================");
        for (StudentRecord s : all) {
            System.out.println(s + " | Grade: " + gradeOf(s.getMarks()));
        }
        System.out.println("Total: " + all.size());
    }

    public void updateMarks(int id, double marks) {
        StudentRecord s = idTable.get(id);
        if (s == null) {
            System.out.println("❌ Student not found.");
            return;
        }
        double old = s.getMarks();
        avlTree.delete(s);
        s.setMarks(marks);
        avlTree.insert(s);
        undoStack.push(new CustomUndoStack.Action(CustomUndoStack.Type.UPDATE, s, old));
        System.out.printf("✅ Updated ID %d marks: %.2f -> %.2f (Grade: %s)\n", id, old, marks, gradeOf(marks));
    }

    public void displayRanking() {
        List<StudentRecord> ranked = avlTree.getRanked();
        System.out.println("\n======================== STUDENT RANKING (AVL TREE) ========================");
        int r = 1;
        for (StudentRecord s : ranked) {
            System.out.printf("Rank #%-2d | ID: %-4d | %-14s | Marks: %-5.2f | Grade: %s\n",
                    r++, s.getId(), s.getName(), s.getMarks(), gradeOf(s.getMarks()));
        }
    }

    public void displayTopStudents(int k) {
        List<StudentRecord> top = CustomMaxHeap.getTopK(idTable.getAll(), k);
        System.out.printf("\n======================== TOP %d STUDENTS (MAX HEAP) ========================\n", k);
        int pos = 1;
        for (StudentRecord s : top) {
            System.out.printf("Top #%-2d | %s | Grade: %s\n", pos++, s, gradeOf(s.getMarks()));
        }
    }

    public void searchByName(String prefix) {
        List<StudentRecord> res = trie.searchPrefix(prefix);
        System.out.println("\n==================== NAME SEARCH (TRIE) ====================");
        if (res.isEmpty()) System.out.println("No students found.");
        for (StudentRecord s : res) System.out.println("-> " + s + " | Grade: " + gradeOf(s.getMarks()));
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("⚠️ Nothing to undo.");
            return;
        }
        CustomUndoStack.Action a = undoStack.pop();
        if (a.type == CustomUndoStack.Type.ADD) {
            idTable.remove(a.student.getId());
            avlTree.delete(a.student);
            trie.remove(a.student);
            System.out.println("⏪ Undone ADD: " + a.student.getName());
        } else if (a.type == CustomUndoStack.Type.REMOVE) {
            addStudentInternal(a.student, false);
            System.out.println("⏪ Undone REMOVE: " + a.student.getName());
        } else if (a.type == CustomUndoStack.Type.UPDATE) {
            avlTree.delete(a.student);
            a.student.setMarks(a.oldMarks);
            avlTree.insert(a.student);
            System.out.printf("⏪ Undone UPDATE: marks restored to %.2f\n", a.oldMarks);
        }
    }

    public void generateReport() {
        List<StudentRecord> all = idTable.getAll();
        if (all.isEmpty()) return;
        double sum = 0, max = -1, min = 101;
        String topName = "";
        for (StudentRecord s : all) {
            sum += s.getMarks();
            if (s.getMarks() > max) { max = s.getMarks(); topName = s.getName(); }
            if (s.getMarks() < min) min = s.getMarks();
        }
        double avg = sum / all.size();
        System.out.println("\n======================== SYSTEM REPORT & ANALYTICS ========================");
        System.out.println(" Total Enrolled Students : " + all.size());
        System.out.printf(" Class Average Marks    : %.2f (%s)\n", avg, gradeOf(avg));
        System.out.printf(" Highest Score           : %.2f (By: %s)\n", max, topName);
        System.out.printf(" Lowest Score            : %.2f\n", min);
        System.out.println(" Undo Stack History Size : " + undoStack.size());
        System.out.println("==========================================================================");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManagementSystemApp app = new StudentManagementSystemApp();

        while (true) {
            System.out.println("\n========== STUDENT MANAGEMENT SYSTEM ==========");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Update Marks");
            System.out.println("6. Student Ranking");
            System.out.println("7. Top Students");
            System.out.println("8. Search Student by Name");
            System.out.println("9. Course Management");
            System.out.println("10. Attendance Management");
            System.out.println("11. Undo Last Operation");
            System.out.println("12. Generate Report");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String input = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("❌ Enter a valid number.");
                continue;
            }

            if (choice == 0) {
                System.out.println("Goodbye!");
                break;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter ID: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Enter Age: ");
                    int age = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter Dept: ");
                    String dept = sc.nextLine().trim();
                    System.out.print("Enter Marks: ");
                    double marks = Double.parseDouble(sc.nextLine().trim());
                    app.addStudent(id, name, age, dept, marks);
                }
                case 2 -> {
                    System.out.print("Enter ID to remove: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    app.removeStudent(id);
                }
                case 3 -> {
                    System.out.print("Enter ID to search: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    app.searchStudent(id);
                }
                case 4 -> app.displayAll();
                case 5 -> {
                    System.out.print("Enter ID: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter New Marks: ");
                    double marks = Double.parseDouble(sc.nextLine().trim());
                    app.updateMarks(id, marks);
                }
                case 6 -> app.displayRanking();
                case 7 -> {
                    System.out.print("Enter K: ");
                    int k = Integer.parseInt(sc.nextLine().trim());
                    app.displayTopStudents(k);
                }
                case 8 -> {
                    System.out.print("Enter name/prefix: ");
                    app.searchByName(sc.nextLine().trim());
                }
                case 9 -> {
                    app.courseGraph.display();
                }
                case 10 -> {
                    System.out.print("Enter Student ID: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    StudentRecord s = app.idTable.get(id);
                    if (s != null) {
                        System.out.print("1. Mark Attendance  2. View Attendance: ");
                        String opt = sc.nextLine().trim();
                        if ("1".equals(opt)) {
                            System.out.print("Date (YYYY-MM-DD): ");
                            String date = sc.nextLine().trim();
                            System.out.print("Present? (y/n): ");
                            s.getAttendance().markAttendance(date, sc.nextLine().trim().equalsIgnoreCase("y"));
                            System.out.println("Attendance recorded.");
                        } else {
                            s.getAttendance().display();
                        }
                    } else System.out.println("❌ Student not found.");
                }
                case 11 -> app.undo();
                case 12 -> app.generateReport();
                default -> System.out.println("❌ Invalid choice.");
            }
        }
        sc.close();
    }
}
