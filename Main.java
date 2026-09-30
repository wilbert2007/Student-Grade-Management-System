import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManagementSystem sms = new StudentManagementSystem();

        System.out.println("==================================================");
        System.out.println("  WELCOME TO STUDENT GRADE MANAGEMENT SYSTEM");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a number between 0 and 12.");
                continue;
            }

            switch (choice) {
                case 1:
                    handleAddStudent(sms, scanner);
                    break;
                case 2:
                    handleRemoveStudent(sms, scanner);
                    break;
                case 3:
                    handleSearchStudent(sms, scanner);
                    break;
                case 4:
                    sms.displayAllStudents();
                    break;
                case 5:
                    handleUpdateMarks(sms, scanner);
                    break;
                case 6:
                    sms.displayStudentRanking();
                    break;
                case 7:
                    handleTopStudents(sms, scanner);
                    break;
                case 8:
                    handleSearchByName(sms, scanner);
                    break;
                case 9:
                    handleCourseManagement(sms, scanner);
                    break;
                case 10:
                    handleAttendanceManagement(sms, scanner);
                    break;
                case 11:
                    sms.undoLastOperation();
                    break;
                case 12:
                    sms.generateReport();
                    break;
                case 0:
                    System.out.println("\nThank you for using Student Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid option. Please select from 0 to 12.");
                    break;
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("========== STUDENT MANAGEMENT SYSTEM ==========");
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
    }

    private static void handleAddStudent(StudentManagementSystem sms, Scanner scanner) {
        try {
            System.out.print("Enter Student ID (e.g. 106): ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Department: ");
            String dept = scanner.nextLine().trim();

            System.out.print("Enter Marks / Score (0-100): ");
            double marks = Double.parseDouble(scanner.nextLine().trim());

            sms.addStudent(id, name, age, dept, marks);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input data format. Please try again.");
        }
    }

    private static void handleRemoveStudent(StudentManagementSystem sms, Scanner scanner) {
        try {
            System.out.print("Enter Student ID to remove: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            sms.removeStudent(id);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    private static void handleSearchStudent(StudentManagementSystem sms, Scanner scanner) {
        try {
            System.out.print("Enter Student ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            sms.searchStudentById(id);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    private static void handleUpdateMarks(StudentManagementSystem sms, Scanner scanner) {
        try {
            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter New Marks (0 - 100): ");
            double newMarks = Double.parseDouble(scanner.nextLine().trim());

            sms.updateMarks(id, newMarks);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid numeric input.");
        }
    }

    private static void handleTopStudents(StudentManagementSystem sms, Scanner scanner) {
        try {
            System.out.print("Enter number of top students to display (K): ");
            int k = Integer.parseInt(scanner.nextLine().trim());
            sms.displayTopStudents(k);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number.");
        }
    }

    private static void handleSearchByName(StudentManagementSystem sms, Scanner scanner) {
        System.out.print("Enter Name or Prefix to search: ");
        String query = scanner.nextLine().trim();
        sms.searchStudentByName(query);
    }

    private static void handleCourseManagement(StudentManagementSystem sms, Scanner scanner) {
        System.out.println("\n--- Course Management (Graph) ---");
        System.out.println("1. View Courses & Prerequisites");
        System.out.println("2. Add New Course");
        System.out.println("3. Add Course Prerequisite");
        System.out.println("4. Enroll Student in Course");
        System.out.print("Enter choice (1-4): ");
        String opt = scanner.nextLine().trim();

        switch (opt) {
            case "1":
                sms.getCourseGraph().displayCourses();
                break;
            case "2":
                System.out.print("Enter Course Code (e.g. CS501): ");
                String code = scanner.nextLine().trim();
                System.out.print("Enter Course Title: ");
                String title = scanner.nextLine().trim();
                System.out.print("Enter Credits: ");
                int credits = Integer.parseInt(scanner.nextLine().trim());
                sms.getCourseGraph().addCourse(code, title, credits);
                System.out.println("✅ Course added successfully.");
                break;
            case "3":
                System.out.print("Enter Course Code: ");
                String cCode = scanner.nextLine().trim();
                System.out.print("Enter Prerequisite Course Code: ");
                String pCode = scanner.nextLine().trim();
                sms.getCourseGraph().addPrerequisite(cCode, pCode);
                break;
            case "4":
                try {
                    System.out.print("Enter Student ID: ");
                    int sId = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Enter Course Code: ");
                    String enCode = scanner.nextLine().trim();
                    sms.enrollStudentInCourse(sId, enCode);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid Student ID.");
                }
                break;
            default:
                System.out.println("Invalid Course option.");
        }
    }

    private static void handleAttendanceManagement(StudentManagementSystem sms, Scanner scanner) {
        System.out.println("\n--- Attendance Management (Linked List) ---");
        System.out.println("1. Mark Student Attendance");
        System.out.println("2. View Student Attendance History");
        System.out.print("Enter choice (1-2): ");
        String opt = scanner.nextLine().trim();

        try {
            if ("1".equals(opt)) {
                System.out.print("Enter Student ID: ");
                int sId = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter Date (YYYY-MM-DD): ");
                String date = scanner.nextLine().trim();
                System.out.print("Is Present? (y/n): ");
                String pres = scanner.nextLine().trim().toLowerCase();
                boolean isPresent = pres.startsWith("y");
                sms.markStudentAttendance(sId, date, isPresent);
            } else if ("2".equals(opt)) {
                System.out.print("Enter Student ID: ");
                int sId = Integer.parseInt(scanner.nextLine().trim());
                sms.viewStudentAttendance(sId);
            } else {
                System.out.println("Invalid Attendance option.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid Student ID.");
        }
    }
}
