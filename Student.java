import java.util.HashSet;
import java.util.Set;

/**
 * Represents a student profile.
 */
public class Student {
    private int id;
    private String name;
    private float cgpa;
    private String branch;
    private Set<String> skills = new HashSet<>();

    public Student(int id, String name, float cgpa, String branch) {
        this.id = id;
        this.name = name;
        this.cgpa = cgpa;
        this.branch = branch;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public float getCgpa() { return cgpa; }
    public String getBranch() { return branch; }
    public Set<String> getSkills() { return skills; }

    public void addSkill(String skill) {
        skills.add(skill.toLowerCase());
    }

    public boolean hasSkill(String skill) {
        return skills.contains(skill.toLowerCase());
    }

    @Override
    public String toString() {
        return name + " (CGPA: " + cgpa + ", Branch: " + branch + ", Skills: " + skills + ")";
    }
}
