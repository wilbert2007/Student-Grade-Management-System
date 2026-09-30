# 🎓 Student Grade Management System

A Java-based **Student Grade Management System** designed to efficiently manage student information, grades, attendance, rankings, courses, and academic performance using **Advanced Data Structures and Algorithms**.

The main objective of this project is to demonstrate how different data structures can be applied to solve real-world student management problems efficiently.

---

## 🚀 Features

- 👨‍🎓 Add and manage student records
- 🔍 Search students efficiently
- 📊 Manage student grades and marks
- 🏆 Generate student rankings
- 🥇 Find top-performing students
- 📚 Manage courses and prerequisites
- 📅 Manage student attendance
- 🔎 Search students by name
- ↩️ Undo recent operations
- 📈 Generate academic performance reports
- 🗑️ Update and delete student records

---

## 🧠 Advanced Data Structures Used

This project implements custom data structures instead of depending completely on Java's built-in collections.

| Data Structure | Usage |
|---|---|
| **Hash Table** | Fast student ID-based searching |
| **AVL Tree** | Maintaining students in sorted order |
| **Max Heap** | Finding top-performing students |
| **Trie** | Fast student name/prefix searching |
| **Graph** | Course and prerequisite management |
| **Linked List** | Attendance record management |
| **Stack** | Undoing previous operations |
| **Queue** | Processing students/tasks sequentially |

---

## 🏗️ Project Structure

```text
StudentGradeManagement/
│
├── Main.java
├── Student.java
│
├── CustomHashTable.java
├── CustomAVLTree.java
├── CustomMaxHeap.java
├── CustomTrie.java
│
├── CourseGraph.java
├── CustomCourseGraph.java
│
├── AttendanceList.java
├── AttendanceLinkedList.java
│
├── StudentAVLTree.java
└── CustomUndoStack.java
