import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTrie {

    private static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;
        List<Student> students; // List of students sharing this name/prefix

        public TrieNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
            this.students = new ArrayList<>();
        }
    }

    private final TrieNode root;

    public StudentTrie() {
        this.root = new TrieNode();
    }

    public void insert(Student student) {
        if (student == null || student.getName() == null) return;
        String name = student.getName().trim().toLowerCase();

        TrieNode current = root;
        for (char ch : name.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
            // Add student reference at prefix nodes for fast prefix matching
            if (!current.students.contains(student)) {
                current.students.add(student);
            }
        }
        current.isEndOfWord = true;
    }

    public void remove(Student student) {
        if (student == null || student.getName() == null) return;
        String name = student.getName().trim().toLowerCase();
        removeHelper(root, name, student, 0);
    }

    private void removeHelper(TrieNode current, String name, Student student, int index) {
        if (current == null) return;
        current.students.remove(student);

        if (index == name.length()) {
            return;
        }

        char ch = name.charAt(index);
        TrieNode nextNode = current.children.get(ch);
        if (nextNode != null) {
            removeHelper(nextNode, name, student, index + 1);
        }
    }

    // Search students with exact name or matching prefix
    public List<Student> searchByPrefix(String prefix) {
        List<Student> results = new ArrayList<>();
        if (prefix == null || prefix.trim().isEmpty()) return results;

        String query = prefix.trim().toLowerCase();
        TrieNode current = root;

        for (char ch : query.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return results; // No students match prefix
            }
            current = current.children.get(ch);
        }

        results.addAll(current.students);
        return results;
    }
}
