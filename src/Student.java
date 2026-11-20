import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A student with marks per subject. Inherits personal details from Person.
 * Knows how to compute its own CGPA and print its report card.
 */
public class Student extends Person {
    // Collection: subject name -> marks (LinkedHashMap keeps the order subjects were added)
    private final Map<String, Integer> marks = new LinkedHashMap<>();

    public Student(String usn, String name, String dob, String phone, String address, String email) {
        super(usn, name, dob, phone, address, email);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    /** Adds or updates marks for a subject; rejects marks outside 0-100. */
    public void addMarks(String subject, int score) throws InvalidMarksException {
        if (score < 0 || score > 100) {
            throw new InvalidMarksException("Marks must be between 0 and 100, got " + score);
        }
        marks.put(subject, score);
    }

    /** Converts marks (out of 100) to a grade point on a 10 point scale. */
    private int gradePoint(int score) {
        if (score >= 90) return 10;
        if (score >= 80) return 9;
        if (score >= 70) return 8;
        if (score >= 60) return 7;
        if (score >= 50) return 6;
        if (score >= 40) return 5;
        return 0; // fail
    }

    /** CGPA = average grade point over all subjects (0 if no marks yet). */
    public double getCgpa() {
        if (marks.isEmpty()) return 0;
        int total = 0;
        for (int score : marks.values()) {
            total += gradePoint(score);
        }
        return (double) total / marks.size();
    }

    /** Sum of marks in all subjects. */
    private int getTotal() {
        int total = 0;
        for (int score : marks.values()) total += score;
        return total;
    }

    /** Prints the report card in a table layout. */
    public void printReportCard() {
        String line = "=".repeat(54);
        String thin = "-".repeat(54);

        System.out.println("\n" + line);
        System.out.println("                    REPORT CARD");
        System.out.println(line);
        System.out.printf("%-10s: %s%n", "USN", getId());
        System.out.printf("%-10s: %s%n", "Name", getName());
        System.out.printf("%-10s: %s%n", "DOB", getDob());
        System.out.printf("%-10s: %s%n", "Phone", getPhone());
        System.out.printf("%-10s: %s%n", "Email", getEmail());
        System.out.printf("%-10s: %s%n", "Address", getAddress());
        System.out.println(thin);

        if (marks.isEmpty()) {
            System.out.println("No marks entered yet.");
            System.out.println(line);
            return;
        }

        System.out.printf("%-20s %8s %12s %8s%n", "Subject", "Marks", "Grade Point", "Result");
        System.out.println(thin);
        for (Map.Entry<String, Integer> e : marks.entrySet()) {
            int gp = gradePoint(e.getValue());
            System.out.printf("%-20s %8d %12d %8s%n",
                    e.getKey(), e.getValue(), gp, gp == 0 ? "FAIL" : "PASS");
        }
        System.out.println(thin);
        int total = getTotal();
        System.out.printf("%-20s %8d / %d%n", "Total", total, marks.size() * 100);
        System.out.printf("%-20s %7.2f%%%n", "Percentage", (double) total / marks.size());
        System.out.printf("%-20s %8.2f%n", "CGPA", getCgpa());
        System.out.println(line);
    }
}