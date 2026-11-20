import java.util.List;
import java.util.Scanner;

/** Console menu for the Student Information and Grade Tracker. */
public class Main {
    private static final Scanner sc = new Scanner(System.in);

    /** Keeps asking until the input matches the given pattern. */
    private static String readValid(String prompt, String regex, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches(regex)) return input;
            System.out.println("  Invalid input: " + errorMsg);
        }
    }

    /** Reads all student details with basic validation and adds the student. */
    private static void addStudent(StudentManager manager) {
        System.out.println("\n--- Add Student ---");
        String usn   = readValid("USN                : ", "[A-Za-z0-9]{5,15}", "USN must be 5-15 letters/digits.").toUpperCase();
        String name  = readValid("Name               : ", "[A-Za-z .]{2,40}", "Use letters only (2-40 characters).");
        String dob   = readValid("DOB (dd-MM-yyyy)   : ", "\\d{2}-\\d{2}-\\d{4}", "Use format dd-MM-yyyy.");
        String phone = readValid("Phone (10 digits)  : ", "\\d{10}", "Phone must be exactly 10 digits.");
        String addr  = readValid("Primary Address    : ", ".{3,100}", "Address cannot be empty.");
        String email = readValid("Email              : ", "[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+", "Enter a valid email like name@mail.com.");

        boolean added = manager.addStudent(new Student(usn, name, dob, phone, addr, email));
        System.out.println(added ? "\n  Student " + usn + " added successfully."
                                 : "\n  A student with USN " + usn + " already exists.");
    }

    /** Prints every stored student as a table, with the address under each row. */
    private static void displayAll(StudentManager manager) {
        List<Student> all = manager.getAllStudents();
        if (all.isEmpty()) {
            System.out.println("\n  No records found.");
            return;
        }
        String line = "=".repeat(98);
        System.out.println("\n" + line);
        System.out.printf("%-4s %-12s %-20s %-11s %-11s %-28s %5s%n",
                "No.", "USN", "Name", "DOB", "Phone", "Email", "CGPA");
        System.out.println(line);
        int n = 1;
        for (Student s : all) {
            System.out.printf("%-4d %-12s %-20s %-11s %-11s %-28s %5.2f%n",
                    n++, s.getId(), s.getName(), s.getDob(), s.getPhone(), s.getEmail(), s.getCgpa());
            System.out.println("     Address: " + s.getAddress());
        }
        System.out.println(line);
        System.out.println("Total students: " + all.size());
    }

    /** Prints students ranked by CGPA. */
    private static void showRankList(StudentManager manager) {
        List<Student> ranked = manager.getRankList();
        if (ranked.isEmpty()) {
            System.out.println("\n  No records found.");
            return;
        }
        String line = "=".repeat(52);
        System.out.println("\n" + line);
        System.out.printf("%-6s %-12s %-20s %8s%n", "Rank", "USN", "Name", "CGPA");
        System.out.println(line);
        int rank = 1;
        for (Student s : ranked) {
            System.out.printf("%-6d %-12s %-20s %8.2f%n", rank++, s.getId(), s.getName(), s.getCgpa());
        }
        System.out.println(line);
    }

    public static void main(String[] args) {
        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("\n========== STUDENT GRADE TRACKER ==========");
            System.out.println(" 1. Add student");
            System.out.println(" 2. Add marks");
            System.out.println(" 3. Report card");
            System.out.println(" 4. Display all records");
            System.out.println(" 5. Rank list");
            System.out.println(" 6. Exit");
            System.out.println("===========================================");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            try {
                if (choice.equals("1")) {
                    addStudent(manager);

                } else if (choice.equals("2")) {
                    System.out.print("USN: ");
                    Student s = manager.find(sc.nextLine().trim().toUpperCase());
                    System.out.print("Subject: ");
                    String subject = sc.nextLine().trim();
                    System.out.print("Marks (0-100): ");
                    s.addMarks(subject, Integer.parseInt(sc.nextLine().trim()));
                    System.out.println("  Marks saved for " + s.getName() + ".");

                } else if (choice.equals("3")) {
                    System.out.print("USN: ");
                    manager.find(sc.nextLine().trim().toUpperCase()).printReportCard();

                } else if (choice.equals("4")) {
                    displayAll(manager);

                } else if (choice.equals("5")) {
                    showRankList(manager);

                } else if (choice.equals("6")) {
                    System.out.println("Goodbye!");
                    break;
                } else {
                    System.out.println("  Invalid choice. Enter a number from 1 to 6.");
                }
            } catch (StudentNotFoundException | InvalidMarksException e) {
                System.out.println("  Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("  Error: please enter a number for marks.");
            }
        }
        sc.close();
    }
}