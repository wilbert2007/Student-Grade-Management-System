import java.util.ArrayList;
import java.util.List;

public class StudentHashTable {
    private static class HashNode {
        int key; // Student ID
        Student value;
        HashNode next;

        public HashNode(int key, Student value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private HashNode[] buckets;
    private int capacity;
    private int size;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    public StudentHashTable() {
        this(16);
    }

    public StudentHashTable(int capacity) {
        this.capacity = capacity;
        this.buckets = new HashNode[capacity];
        this.size = 0;
    }

    private int hash(int key) {
        return Math.abs(Integer.hashCode(key)) % capacity;
    }

    public void put(int id, Student student) {
        int bucketIndex = hash(id);
        HashNode head = buckets[bucketIndex];

        // Check if student already exists and update
        HashNode current = head;
        while (current != null) {
            if (current.key == id) {
                current.value = student;
                return;
            }
            current = current.next;
        }

        // Insert new node at beginning of bucket
        HashNode newNode = new HashNode(id, student);
        newNode.next = head;
        buckets[bucketIndex] = newNode;
        size++;

        // Rehash if load factor exceeds threshold
        if ((1.0 * size) / capacity >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
    }

    public Student get(int id) {
        int bucketIndex = hash(id);
        HashNode current = buckets[bucketIndex];
        while (current != null) {
            if (current.key == id) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public Student remove(int id) {
        int bucketIndex = hash(id);
        HashNode current = buckets[bucketIndex];
        HashNode prev = null;

        while (current != null) {
            if (current.key == id) {
                if (prev != null) {
                    prev.next = current.next;
                } else {
                    buckets[bucketIndex] = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(int id) {
        return get(id) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            HashNode current = buckets[i];
            while (current != null) {
                list.add(current.value);
                current = current.next;
            }
        }
        return list;
    }

    private void rehash() {
        HashNode[] oldBuckets = buckets;
        capacity = capacity * 2;
        buckets = new HashNode[capacity];
        size = 0;

        for (HashNode headNode : oldBuckets) {
            HashNode current = headNode;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
}
