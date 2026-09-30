import java.util.ArrayList;
import java.util.List;

public class StudentMaxHeap {
    private Student[] heap;
    private int size;
    private int capacity;

    public StudentMaxHeap() {
        this(50);
    }

    public StudentMaxHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new Student[capacity];
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int leftChild(int i) { return 2 * i + 1; }
    private int rightChild(int i) { return 2 * i + 2; }

    private void swap(int i, int j) {
        Student temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void ensureCapacity() {
        if (size >= capacity) {
            capacity *= 2;
            Student[] newHeap = new Student[capacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    public void insert(Student student) {
        ensureCapacity();
        heap[size] = student;
        int current = size;
        size++;

        // Bubble up
        while (current > 0 && heap[current].getMarks() > heap[parent(current)].getMarks()) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public Student extractMax() {
        if (size <= 0) return null;
        if (size == 1) {
            size--;
            return heap[0];
        }

        Student root = heap[0];
        heap[0] = heap[size - 1];
        size--;
        maxHeapify(0);

        return root;
    }

    private void maxHeapify(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        int largest = i;

        if (left < size && heap[left].getMarks() > heap[largest].getMarks()) {
            largest = left;
        }
        if (right < size && heap[right].getMarks() > heap[largest].getMarks()) {
            largest = right;
        }

        if (largest != i) {
            swap(i, largest);
            maxHeapify(largest);
        }
    }

    public Student peek() {
        return size > 0 ? heap[0] : null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Build heap from list of students
    public static StudentMaxHeap buildHeap(List<Student> students) {
        StudentMaxHeap maxHeap = new StudentMaxHeap(Math.max(students.size() + 10, 16));
        for (Student s : students) {
            maxHeap.insert(s);
        }
        return maxHeap;
    }

    // Get Top K students without destroying original heap
    public List<Student> getTopK(int k) {
        List<Student> result = new ArrayList<>();
        // Clone current heap to extract
        StudentMaxHeap tempHeap = new StudentMaxHeap(this.size + 1);
        for (int i = 0; i < this.size; i++) {
            tempHeap.insert(this.heap[i]);
        }

        int count = Math.min(k, tempHeap.size());
        for (int i = 0; i < count; i++) {
            result.add(tempHeap.extractMax());
        }
        return result;
    }
}
