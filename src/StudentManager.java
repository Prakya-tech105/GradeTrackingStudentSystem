import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Stores all students and provides search, listing and ranking features. */
public class StudentManager {
    // Collection: USN -> Student for fast lookup (insertion order is kept for listing)
    private final Map<String, Student> students = new LinkedHashMap<>();

    /** Adds a student. Returns false if the USN already exists. */
    public boolean addStudent(Student s) {
        if (students.containsKey(s.getId())) {
            return false;
        }
        students.put(s.getId(), s);
        return true;
    }

    /** Finds a student by USN or throws StudentNotFoundException. */
    public Student find(String usn) throws StudentNotFoundException {
        Student s = students.get(usn);
        if (s == null) {
            throw new StudentNotFoundException("No student with USN " + usn);
        }
        return s;
    }

    /** Returns all students in the order they were added. */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    /**
     * Returns students ranked by CGPA, highest first.
     * DSA: our own insertion sort (no Collections.sort / streams).
     */
    public List<Student> getRankList() {
        List<Student> list = getAllStudents();
        for (int i = 1; i < list.size(); i++) {
            Student key = list.get(i);
            int j = i - 1;
            // shift smaller-CGPA students one place to the right
            while (j >= 0 && list.get(j).getCgpa() < key.getCgpa()) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
        return list;
    }
}