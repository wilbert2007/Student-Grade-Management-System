import java.util.ArrayList;
import java.util.List;

public class StudentAVLTree {

    public static class AVLNode {
        Student student;
        int height;
        AVLNode left;
        AVLNode right;

        public AVLNode(Student student) {
            this.student = student;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
    }

    private AVLNode root;

    public StudentAVLTree() {
        this.root = null;
    }

    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Compare primarily by marks, secondarily by ID for uniqueness
    private int compare(Student a, Student b) {
        if (Double.compare(a.getMarks(), b.getMarks()) != 0) {
            return Double.compare(a.getMarks(), b.getMarks());
        }
        return Integer.compare(a.getId(), b.getId());
    }

    public void insert(Student student) {
        root = insertNode(root, student);
    }

    private AVLNode insertNode(AVLNode node, Student student) {
        if (node == null) {
            return new AVLNode(student);
        }

        int cmp = compare(student, node.student);
        if (cmp < 0) {
            node.left = insertNode(node.left, student);
        } else if (cmp > 0) {
            node.right = insertNode(node.right, student);
        } else {
            // Already present, update student reference
            node.student = student;
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // LL Case
        if (balance > 1 && compare(student, node.left.student) < 0) {
            return rightRotate(node);
        }
        // RR Case
        if (balance < -1 && compare(student, node.right.student) > 0) {
            return leftRotate(node);
        }
        // LR Case
        if (balance > 1 && compare(student, node.left.student) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // RL Case
        if (balance < -1 && compare(student, node.right.student) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void delete(Student student) {
        root = deleteNode(root, student);
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private AVLNode deleteNode(AVLNode root, Student student) {
        if (root == null) {
            return null;
        }

        int cmp = compare(student, root.student);
        if (cmp < 0) {
            root.left = deleteNode(root.left, student);
        } else if (cmp > 0) {
            root.right = deleteNode(root.right, student);
        } else {
            // Node found
            if ((root.left == null) || (root.right == null)) {
                AVLNode temp = (root.left != null) ? root.left : root.right;
                if (temp == null) {
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                AVLNode temp = minValueNode(root.right);
                root.student = temp.student;
                root.right = deleteNode(root.right, temp.student);
            }
        }

        if (root == null) return null;

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balance = getBalance(root);

        // LL Case
        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }
        // LR Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        // RR Case
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }
        // RL Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    // Descending order traversal (Highest marks = Rank 1)
    public List<Student> getRankedStudents() {
        List<Student> list = new ArrayList<>();
        inOrderDescending(root, list);
        return list;
    }

    private void inOrderDescending(AVLNode node, List<Student> list) {
        if (node != null) {
            inOrderDescending(node.right, list);
            list.add(node.student);
            inOrderDescending(node.left, list);
        }
    }
}
