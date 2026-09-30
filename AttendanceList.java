public class AttendanceList {

    public static class Node {
        String date;
        boolean isPresent;
        Node next;

        public Node(String date, boolean isPresent) {
            this.date = date;
            this.isPresent = isPresent;
            this.next = null;
        }
    }

    private Node head;
    private int totalClasses;
    private int attendedClasses;

    public AttendanceList() {
        this.head = null;
        this.totalClasses = 0;
        this.attendedClasses = 0;
    }

    public void markAttendance(String date, boolean isPresent) {
        Node newNode = new Node(date, isPresent);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }

        totalClasses++;
        if (isPresent) {
            attendedClasses++;
        }
    }

    public double getAttendancePercentage() {
        if (totalClasses == 0) return 100.0;
        return ((double) attendedClasses / totalClasses) * 100.0;
    }

    public void displayAttendance() {
        if (head == null) {
            System.out.println("No attendance records found.");
            return;
        }
        System.out.println("----- Attendance Log (Linked List) -----");
        Node current = head;
        while (current != null) {
            System.out.println("Date: " + current.date + " -> Status: " + (current.isPresent ? "PRESENT" : "ABSENT"));
            current = current.next;
        }
        System.out.printf("Summary: %d/%d classes attended (%.2f%%)\n", attendedClasses, totalClasses, getAttendancePercentage());
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public int getAttendedClasses() {
        return attendedClasses;
    }
}
