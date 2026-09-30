public class UndoStack {

    public enum ActionType {
        ADD_STUDENT,
        REMOVE_STUDENT,
        UPDATE_MARKS
    }

    public static class Action {
        ActionType type;
        Student student;
        double oldMarks;
        double newMarks;

        public Action(ActionType type, Student student) {
            this.type = type;
            this.student = student;
        }

        public Action(ActionType type, Student student, double oldMarks, double newMarks) {
            this.type = type;
            this.student = student;
            this.oldMarks = oldMarks;
            this.newMarks = newMarks;
        }
    }

    private static class StackNode {
        Action action;
        StackNode next;

        public StackNode(Action action) {
            this.action = action;
            this.next = null;
        }
    }

    private StackNode top;
    private int size;

    public UndoStack() {
        this.top = null;
        this.size = 0;
    }

    public void push(Action action) {
        StackNode newNode = new StackNode(action);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public Action pop() {
        if (isEmpty()) return null;
        Action action = top.action;
        top = top.next;
        size--;
        return action;
    }

    public Action peek() {
        return isEmpty() ? null : top.action;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }
}
